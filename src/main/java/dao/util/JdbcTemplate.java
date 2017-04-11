package dao.util;

import exceptions.DaoException;
import model.Page;

import java.util.List;

public interface JdbcTemplate<T> {
    void update(String sql, Object... args) throws DaoException;
    void update(String sql, List<Integer> generatedKeys, Object... args) throws DaoException;
    void batchUpdate(String sql, List<Object[]> args) throws DaoException;
    T queryForObject(ResultSetMapper<T> rsMapper, String sql, Object ...args) throws DaoException;
    List<T> queryForList(ResultSetMapper<T> rsMapper, String sql, Object ...args) throws DaoException;
    Page<T> queryForPage(ResultSetMapper<T> rsMapper, String sql, int pageNumber, Object ...args) throws DaoException;
}
