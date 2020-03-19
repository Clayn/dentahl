package de.clayntech.dentahl4j.server.db.util;

import de.clayntech.dentahl4j.domain.Element;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ElementMapper extends DentahlMapper<Element> {
    @Override
    protected Element map(ResultSet res, int index) throws SQLException {
        int id=res.getInt("id");
        return id<0||id>=Element.values().length?null:Element.values()[id];
    }
}
