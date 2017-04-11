package dao.util;

import exceptions.DaoException;
import model.Page;

import java.sql.Connection;
import java.util.List;

public interface JdbcTemplate<T> {
    void update(Connection connection, String sql, Object... args) throws DaoException;
    T queryForObject(Connection connection, ResultSetMapper<T> rsMapper, String sql, Object ...args) throws DaoException;
    List<T> queryForList(Connection connection, ResultSetMapper<T> rsMapper, String sql, Object ...args) throws DaoException;
    Page<T> queryForPage(Connection connection, ResultSetMapper<T> rsMapper, String sql, int pageNumber, Object ...args) throws DaoException;
}
