package de.clayntech.dentahl4j.server.db.util;

import org.springframework.jdbc.core.JdbcTemplate;

public interface DBInserter<T> {
    void insert(T obj) throws Exception;
}
