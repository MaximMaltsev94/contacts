package dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetMapper<T> {
    T parseResultSet(ResultSet rs) throws SQLException;
}
