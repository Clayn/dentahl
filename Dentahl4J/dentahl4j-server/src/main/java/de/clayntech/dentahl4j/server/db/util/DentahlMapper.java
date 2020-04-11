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
package de.clayntech.dentahl4j.server.db.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @param <T>
 */
public abstract class DentahlMapper<T> implements RowMapper<T>, ResultSetExtractor<List<T>>
{

    @Override
    public T mapRow(ResultSet rs, int i) throws SQLException
    {
        return map(rs, i);
    }

    protected void getString(ResultSet rs, String name, String def, Consumer<String> setter) {
        String val=def;
        try {
            val=rs.getString(name);
        } catch (SQLException e) {
        }
        setter.accept(val);
    }

    protected void getInt(ResultSet rs, String name, int def, Consumer<Integer> setter) {
        int val=def;
        try {
            val=rs.getInt(name);
        } catch (SQLException e) {
        }
        setter.accept(val);
    }

    @Override
    public List<T> extractData(ResultSet rs) throws SQLException, DataAccessException
    {
        int i=0;
        List<T> back=new ArrayList<>();
        while(rs.next()) {
            back.add(mapRow(rs, i));
        }
        return back;
    }
    
    protected abstract T map(ResultSet res, int index) throws SQLException;
}
