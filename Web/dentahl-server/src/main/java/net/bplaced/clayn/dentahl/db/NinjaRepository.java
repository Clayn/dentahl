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
package net.bplaced.clayn.dentahl.db;

import java.util.List;
import net.bplaced.clayn.d4j.domain.Ninja;
import net.bplaced.clayn.dentahl.db.util.DentahlMapper;
import net.bplaced.clayn.dentahl.db.util.NinjaMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 */
@Repository
public class NinjaRepository extends DentahlRepository<Ninja>
{

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
    
    
}
