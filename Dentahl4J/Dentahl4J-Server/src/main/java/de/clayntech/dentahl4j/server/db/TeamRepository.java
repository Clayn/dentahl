/*
 * The MIT License
 *
 * Copyright 2020 Clayn <clayn_osmato@gmx.de>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.clayntech.dentahl4j.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import de.clayntech.dentahl4j.domain.Ninja;
import de.clayntech.dentahl4j.domain.Team;
import de.clayntech.dentahl4j.util.Cache;
import de.clayntech.dentahl4j.server.db.util.DentahlMapper;
import de.clayntech.dentahl4j.server.db.util.TeamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
@Repository
public class TeamRepository extends DentahlRepository<Team>
{

    private final Cache cache=new Cache();
    
    @Autowired
    private NinjaRepository repository;
    
    {
        cache.registerSupplier("ninjas", this::loadNinjas);
    
    }
    private Map<Integer,Ninja> loadNinjas() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(Ninja::getId, Function.identity()));
    }
    
    private Map<Integer,Ninja> getNinjas() {
        return cache.get("ninjas");
    }

    @Override
    protected DentahlMapper<Team> getMapper()
    {
        return new TeamMapper();
    }

    @Override
    public List<Team> findAll()
    {
        Map<Integer,Ninja> ninjas=getNinjas();
        List<Team> teams=getDBAccess().query("SELECT * FROM Team", getRSExtractor());
        
        for(Team t:teams) {
            getDBAccess().query((Connection cnctn) ->
            {
                PreparedStatement ps=cnctn.prepareStatement("SELECT * FROM TeamNinja WHERE team_id=?");
                ps.setInt(1, t.getId());
                return ps;
            }, (ResultSet rs) ->
            {
                while(rs.next()) {
                    int nId=rs.getInt("ninja_id");
                    int pos=rs.getInt("position");
                    if(ninjas.containsKey(nId)) {
                        t.getPositions().put(pos, ninjas.get(nId));
                    }
                }
                return null;
            });
        }
        return teams;
    }
    
}
