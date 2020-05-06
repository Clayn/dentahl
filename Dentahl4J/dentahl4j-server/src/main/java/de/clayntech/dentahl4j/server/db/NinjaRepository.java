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

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.clayntech.dentahl4j.domain.Ninja;
import de.clayntech.dentahl4j.server.data.Grabber;
import de.clayntech.dentahl4j.server.db.util.DentahlMapper;
import de.clayntech.dentahl4j.server.db.util.NinjaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
@Repository
@Configuration
@EnableScheduling
public class NinjaRepository extends DentahlRepository<Ninja>
{

    private static final String INSERT="INSERT INTO `Ninja` (`id`, `name`, `image`, `element`, `main`) VALUES (?, ?, ?, ?,? )";
    @Autowired
    private Grabber grabber;
    {
        LOG.info("Creating the ninja repository");
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    refreshNinjas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }
    @Override
    protected DentahlMapper<Ninja> getMapper()
    {
        return new NinjaMapper();
    }

    @Override
    public List<Ninja> findAll()
    {
        return getDBAccess().query("SELECT * FROM Ninja", getRSExtractor());
    }
    
    public Ninja getNinja(int id) {
        List<Ninja> ninjas=findAll((n)->n.getId()==id);
        return ninjas.isEmpty()?null:ninjas.get(0);
    }
    
    @Scheduled(initialDelay = 5000,fixedDelay = 10000)
    protected void refreshNinjas() throws IOException, InterruptedException {
        LOG.info("Refreshing the Ninja List");
        List<Ninja> loaded=grabber.grabNinjas();

        List<Ninja> existing=findAll();
        List<Ninja> missing=loaded.stream()
                .filter((n)->!existing.contains(n))
                .collect(Collectors.toList());
        List<Ninja> notToAdd=new ArrayList<>(5);
        for(Ninja n:loaded) {
            if(n.getMain()!=0) {
                notToAdd.add(n);
            }
        }
        loaded.removeAll(notToAdd);
        NinjaMapper mapper= (NinjaMapper) getMapper();
        if(!missing.isEmpty()) {
            for(Ninja n:missing) {
                try {
                    insert(n);
                } catch (Exception e) {
                    LOG.error("Failed to add: {}",n.getName(),e);
                }
            }
        }
        else {
            LOG.info("No new ninjas found");
        }
    }

    @Override
    public void insert(Ninja obj) throws Exception {
        getDBAccess().update(INSERT, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1,obj.getId());
                preparedStatement.setString(2,obj.getName());
                preparedStatement.setString(3,obj.getImage()!=null?obj.getImage().toString():"");
                preparedStatement.setInt(4,obj.getElement());
                preparedStatement.setInt(5,obj.getMain());
            }
        });
    }
}
