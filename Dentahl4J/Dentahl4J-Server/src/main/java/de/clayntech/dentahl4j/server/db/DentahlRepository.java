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

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.clayntech.dentahl4j.domain.Element;
import de.clayntech.dentahl4j.domain.Ninja;
import de.clayntech.dentahl4j.server.db.util.DentahlMapper;
import de.clayntech.dentahl4j.server.db.util.ElementMapper;
import de.clayntech.dentahl4j.server.db.util.NinjaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
@Repository
public abstract class DentahlRepository<T>
{

    private static final Map<Element, String> ICON_URLS = new HashMap<>();
    private static final Map<Element, Ninja> MAINS = new HashMap<>();

    static {
        ICON_URLS.put(Element.FIRE,"include/images/sim/huo_icon.png"
                );
        ICON_URLS.put(Element.WIND,"include/images/sim/feng_icon.png"
                );
        ICON_URLS.put(Element.LIGHTNING,"include/images/sim/lei_icon.png"
                );
        ICON_URLS.put(Element.EARTH,"include/images/sim/tu_icon.png"
                );
        ICON_URLS.put(Element.WATER,"include/images/sim/shui_icon.png"
                );

        try {
            Ninja FIRE=new Ninja();
            FIRE.setImage(new URL("https://en.konohaproxy.com.br/include/images/ninja/10000201.png"));
            FIRE.setName("Feuer-Main");
            FIRE.setElement(Element.FIRE.ordinal());
            FIRE.setMain(FIRE.getElement()+1);
            FIRE.setId(-1*FIRE.getElement());
            MAINS.put(Element.FIRE,FIRE);
            Ninja WIND=new Ninja();
            WIND.setImage(new URL("https://en.konohaproxy.com.br/include/images/ninja/10000301.png"));
            WIND.setName("Wind-Main");
            WIND.setElement(Element.WIND.ordinal());
            WIND.setMain(WIND.getElement()+1);
            WIND.setId(-1*WIND.getElement());
            MAINS.put(Element.WIND,WIND);
            Ninja LIGHTNING=new Ninja();
            LIGHTNING.setImage(new URL("https://en.konohaproxy.com.br/include/images/ninja/10000401.png"));
            LIGHTNING.setName("Blitz-Main");
            LIGHTNING.setElement(Element.LIGHTNING.ordinal());
            LIGHTNING.setMain(LIGHTNING.getElement()+1);
            LIGHTNING.setId(-1*LIGHTNING.getElement());
            MAINS.put(Element.LIGHTNING,LIGHTNING);
            Ninja EARTH=new Ninja();
            EARTH.setImage(new URL("https://en.konohaproxy.com.br/include/images/ninja/10000501.png"));
            EARTH.setName("Erd-Main");
            EARTH.setElement(Element.EARTH.ordinal());
            EARTH.setMain(EARTH.getElement()+1);
            EARTH.setId(-1*EARTH.getElement());
            MAINS.put(Element.EARTH,EARTH);
            Ninja WATER=new Ninja();
            WATER.setImage(new URL("https://en.konohaproxy.com.br/include/images/ninja/10000101.png"));
            WATER.setName("Wasser-Main");
            WATER.setElement(Element.WATER.ordinal());
            WATER.setMain(WATER.getElement()+1);
            WATER.setId(-1*WATER.getElement());
            MAINS.put(Element.WATER,WATER);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    protected final Logger LOG= LoggerFactory.getLogger(getClass());
    @Autowired
    private JdbcTemplate template;
    {
        Thread t=new Thread(this::prepareElements);
        t.setDaemon(true);
        t.setName("dbPreparation-"+getClass().getSimpleName());
        t.start();
    }
    void prepareElements() {
        while(template==null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOG.error("",e);
            }
        }
        synchronized (DentahlRepository.class) {
            List<Element> existing=getDBAccess().query("SELECT * FROM Element",(RowMapper<Element>)new ElementMapper());
            for(Element el:Element.values()) {
                LOG.info("Checking if {} already exists",el.name());
                if(!existing.contains(el)) {
                    LOG.info("Not found. Add it");
                    getDBAccess().update("INSERT INTO `Element` (`id`, `name`, `image`) VALUES (?, ?, ?)", new PreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement preparedStatement) throws SQLException {
                            preparedStatement.setInt(1,el.ordinal());
                            preparedStatement.setString(2,el.name());
                            preparedStatement.setString(3,ICON_URLS.get(el));
                        }
                    });
                }else {
                    LOG.info("Found it");
                }
            }
            List<Ninja> ninjas=getDBAccess().query("SELECT * FROM Ninja",(RowMapper<Ninja>)new NinjaMapper());
            for(Map.Entry<Element,Ninja> entry:MAINS.entrySet()) {
                Ninja n=entry.getValue();
                LOG.info("Checking if {} already exists",n);
                if(!ninjas.contains(n)) {
                    LOG.info("Not found. Add it");
                    getDBAccess().update("INSERT INTO `Ninja` (`id`, `name`, `image`,`element`,`main`) VALUES (?, ?, ?,?,?)", new PreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement preparedStatement) throws SQLException {
                            preparedStatement.setInt(1,-1*n.getElement());
                            preparedStatement.setString(2,n.getName());
                            preparedStatement.setString(3,n.getImage().toString());
                            preparedStatement.setInt(4,n.getElement());
                            preparedStatement.setInt(5,n.getMain());
                        }
                    });
                }else {
                    LOG.info("Found it");
                }
            }
        }
    }
    protected final JdbcTemplate getDBAccess() {
        return template;
    }
    
    protected abstract DentahlMapper<T> getMapper();
    
    protected RowMapper<T> getRowMapper() {
        return getMapper();
    }
    
    protected ResultSetExtractor<List<T>> getRSExtractor() {
        return getMapper();
    }
    
    public abstract List<T> findAll();
    
    public List<T> findAll(Predicate<T> filter) {
        return findAll().stream().filter(filter)
                .collect(Collectors.toList());
    }
}
