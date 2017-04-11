package dao.util;

import java.sql.ResultSet;

public interface ResultSetMapper<T> {
    T parseResultSet(ResultSet rs);
}
