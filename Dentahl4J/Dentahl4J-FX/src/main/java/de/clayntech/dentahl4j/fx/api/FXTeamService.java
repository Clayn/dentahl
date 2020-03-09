package de.clayntech.dentahl4j.fx.api;

import de.clayntech.config4j.Config4J;
import de.clayntech.config4j.rt.Runtime4J;
import de.clayntech.dentahl4j.api.TeamEndpoint;
import de.clayntech.dentahl4j.config.Keys;
import de.clayntech.dentahl4j.domain.Ninja;
import de.clayntech.dentahl4j.domain.Team;
import de.clayntech.dentahl4j.tooling.TaskManager;
import javafx.application.Platform;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class FXTeamService<T> extends TeamEndpoint {
    private Consumer<T> callback;
    public FXTeamService(String baseUrl) {
        super(baseUrl);
    }

    public FXTeamService(URL baseUrl) {
        super(baseUrl);
    }
    public FXTeamService() {
        super(Config4J.getConfiguration().get(Keys.REST_BASE));
    }
    private void callCallback(Object val) {
        if(callback!=null) {
            Platform.runLater(()->callback.accept((T)val));
        }
    }

    public <R> FXTeamService<R> setCallback(Consumer<R> callback) {
        FXTeamService<R> newService=new FXTeamService<R>(hostBase);
        newService.callback=callback;
        return newService;
    }

    @Override
    public List<Team> getTeams() throws IOException {
        if(Platform.isFxApplicationThread()) {
            TaskManager man = Runtime4J.getRuntime().getObject(Keys.TASK_MANAGER);
            man.execute(()-> {
                try {
                    getTeams(null);
                } catch (IOException e) {
                    LOG.error("",e);
                }
            });
            return Collections.emptyList();
        }
        List<Team> list=super.getTeams(null);
        callCallback(list);
        return list;
    }

    @Override
    public List<Team> getTeams(List<Ninja> ninjaList) throws IOException {
        if(Platform.isFxApplicationThread()) {
            TaskManager man = Runtime4J.getRuntime().getObject(Keys.TASK_MANAGER);
            man.execute(()-> {
                try {
                    getTeams(ninjaList);
                } catch (IOException e) {
                    LOG.error("",e);
                }
            });
            return Collections.emptyList();
        }
        List<Team> list=super.getTeams(ninjaList);
        callCallback(list);
        return list;
    }
}
