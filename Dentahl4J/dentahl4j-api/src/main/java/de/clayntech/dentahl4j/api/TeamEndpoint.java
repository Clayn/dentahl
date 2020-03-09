package de.clayntech.dentahl4j.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.MultipartBody;
import kong.unirest.Unirest;
import de.clayntech.dentahl4j.domain.ErrorMessage;
import de.clayntech.dentahl4j.domain.Ninja;
import de.clayntech.dentahl4j.domain.Team;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class TeamEndpoint extends ServiceEndPoint
{

    private final Map<Integer, Ninja> ninjas = new HashMap<>();
    protected final String hostBase;

    public TeamEndpoint(String baseUrl)
    {
        super(Objects.requireNonNull(baseUrl) + "/team/");
        hostBase = baseUrl;
    }

    public TeamEndpoint(URL baseUrl)
    {
        super(Objects.requireNonNull(baseUrl).toString() + "/team/");
        hostBase = baseUrl.toString();

    }

    public List<Team> getTeams() throws IOException
    {
        return getTeams(null);
    }

    public List<Team> getTeams(List<Ninja> ninjaList) throws IOException
    {
        LOG.debug("Getting the teams using: {}/list",getSafeURL());
        LOG.debug("Host url: {}",hostBase);
        Map<Integer, Ninja> tmpNinjas = new HashMap<>();
        if (ninjaList == null || ninjaList.isEmpty())
        {
            if (ninjas.isEmpty())
            {
                ninjas.putAll(
                        new NinjaServiceEndpoint(hostBase).getNinjaList().stream().collect(
                                Collectors.toMap(Ninja::getId,
                                        Function.identity())));
            }
            tmpNinjas.putAll(ninjas);
        } else
        {
            tmpNinjas.putAll(ninjaList.stream().collect(
                    Collectors.toMap(Ninja::getId,
                            Function.identity())));
        }
        List<Team> teams = new ArrayList<>();
        LOG.debug("Loading local teams");
        loadLocalTeams(tmpNinjas, teams);
        HttpResponse<JsonNode> response = Unirest.get(getSafeURL() + "list")
                .asJson();
        JsonNode node = response.getBody();

        if (node != null && node.isArray())
        {
            LOG.debug("Team response: {}",node.toString());
            final Type type = new TypeToken<List<Team>>()
            {
            }.getType();
            teams.addAll(new Gson().fromJson(node.toString(),type));
            LOG.debug("Got Teams: {}",teams);
            return teams;
        }
        LOG.debug("No response found");
        return Collections.emptyList();
    }

    private void loadLocalTeams(Map<Integer, Ninja> tmpNinjas, List<Team> teams) throws NumberFormatException, IOException
    {
        File teamDir = new File("data/teams");
        if (teamDir.exists())
        {
            File files[] = teamDir.listFiles(new FilenameFilter()
            {
                @Override
                public boolean accept(File dir, String name)
                {
                    return name.endsWith(".team");
                }
            });
            if (files != null)
            {
                for (File f : files)
                {

                    Properties prop = new Properties();
                    try (InputStream in = new FileInputStream(f))
                    {
                        prop.load(in);
                    }
                    if (validateProperty(prop))
                    {
                        continue;
                    }
                    Team t = new Team();
                    t.setName(prop.getProperty("team.name"));
                    t.setDescription(prop.getProperty("team.desc"));
                    for (int i = 0; i < 9; ++i)
                    {
                        if (prop.containsKey("position." + i))
                        {
                            t.getPositions().put(i, tmpNinjas.getOrDefault(
                                    Integer.parseInt(prop.getProperty(
                                            "position." + i)),
                                    null));
                        }
                    }
                    teams.add(t);
                }
            }
        }
    }

    public ErrorMessage uploadTeam(Team team, String token) throws IOException
    {
        HttpResponse<JsonNode> response = Unirest.post(
                getSafeURL() + "upload")
                .body(team)
                .header("Content-Type", "application/json")
                .asJson();
        JsonNode node=response.getBody();
        return new Gson().fromJson(node.toString(),ErrorMessage.class);
    }

    public ErrorMessage saveTeam(Team team) throws IOException
    {
        File data = new File("data");
        File teamDir = new File(data, "teams");
        if (!teamDir.exists())
        {
            if (!teamDir.mkdirs())
            {
                throw new IOException();
            }
        }
        File teamFile = new File(teamDir, team.getName() + ".team");
        if (teamFile.exists())
        {
            return new ErrorMessage("multiple");
        }
        Properties prop = new Properties();
        prop.setProperty("team.name", team.getName());
        prop.setProperty("team.desc", team.getDescription());
        for (Map.Entry<Integer, Ninja> entry : team.getPositions().entrySet())
        {
            prop.setProperty("position." + entry.getKey(),
                    entry.getValue() == null ? "-1" : entry.getValue().getId() + "");
        }
        try (OutputStream out = new FileOutputStream(teamFile))
        {
            prop.store(out, "");
        }
        return new ErrorMessage("0");
    }

    private boolean validateProperty(Properties prop)
    {
        if (!prop.containsKey("team.name"))
        {
            return false;
        }
        if (!prop.containsKey("team.desc"))
        {
            return false;
        }
        int count = 0;
        for (int i = 0; i < 9; ++i)
        {
            if (prop.containsKey("position." + i))
            {
                count++;
            }
        }
        return !(count <= 0 || count > 4);
    }

}
