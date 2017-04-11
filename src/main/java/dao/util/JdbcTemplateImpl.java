package dao.util;

import exceptions.DaoException;
import model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DaoUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplateImpl<T> implements JdbcTemplate<T> {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcTemplateImpl.class);
    private Connection connection;

    public JdbcTemplateImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void update(String sql, Object... args) throws DaoException {
        try(PreparedStatement statement = DaoUtils.getPreparedStatement(connection, sql, Statement.RETURN_GENERATED_KEYS, args)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("error while performing sql query - {}", sql, e);
            throw new DaoException("error while performing sql query - " + sql, e);
        }
    }

    @Override
    public void update(String sql, List<Integer> generatedKeys, Object... args) throws DaoException {
        try(PreparedStatement statement = DaoUtils.getPreparedStatement(connection, sql, Statement.RETURN_GENERATED_KEYS, args)) {
            statement.executeUpdate();
            try(ResultSet rs = statement.getGeneratedKeys();){
                while (rs.next()) {
                    generatedKeys.add(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("error while performing sql query - {}", sql, e);
            throw new DaoException("error while performing sql query - " + sql, e);
        }
    }

    @Override
    public void batchUpdate(String sql, List<Object[]> args) throws DaoException {
        try(PreparedStatement statement = DaoUtils.getPreparedStatement(connection, sql, Statement.RETURN_GENERATED_KEYS, args.get(0))) {
            statement.addBatch();
            for(int i = 1; i < args.size(); ++i) {
                DaoUtils.fillPreparedStatement(statement, args.get(i));
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            LOG.error("error while performing sql query - {}", sql, e);
            throw new DaoException("error while performing sql query - " + sql, e);
        }
    }

    @Override
    public T queryForObject(ResultSetMapper<T> rsMapper, String sql, Object... args) throws DaoException {
        T result = null;
        try(PreparedStatement statement = DaoUtils.getPreparedStatement(connection, sql, Statement.RETURN_GENERATED_KEYS, args);
            ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                result = rsMapper.parseResultSet(rs);
            }
        } catch (SQLException e) {
            LOG.error("error while performing sql query - {}", sql, e);
            throw new DaoException("error while performing sql query - " + sql, e);
        }
        return result;
    }

    @Override
    public List<T> queryForList(ResultSetMapper<T> rsMapper, String sql, Object... args) throws DaoException {
        List<T> result = new ArrayList<>();
        try(PreparedStatement statement = DaoUtils.getPreparedStatement(connection, sql, Statement.RETURN_GENERATED_KEYS, args);
            ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                result.add(rsMapper.parseResultSet(rs));
            }
        } catch (SQLException e) {
            LOG.error("error while performing sql query - {}", sql, e);
            throw new DaoException("error while performing sql query - " + sql, e);
        }
        return result;
    }

    @Override
    public Page<T> queryForPage(ResultSetMapper<T> rsMapper, String sql, int pageNumber, Object... args) throws DaoException {
        List<T> contactList = new ArrayList<>();
        int totalRowCount = 1;
        try (PreparedStatement preparedStatement = DaoUtils.getPreparedStatement(connection, sql, Statement.RETURN_GENERATED_KEYS, args);
             ResultSet rs = preparedStatement.executeQuery();
             PreparedStatement statement = connection.prepareStatement("SELECT found_rows()");
             ResultSet found_rows = statement.executeQuery()) {
            while (rs.next()) {
                contactList.add(rsMapper.parseResultSet(rs));
            }
            if(found_rows.next()) {
                totalRowCount = found_rows.getInt(1);
            }
        } catch (SQLException e) {
            LOG.error("error while performing sql query - {}", sql, e);
            throw new DaoException("error while performing sql query - " + sql, e);
        }
        return new Page<>(contactList, pageNumber, totalRowCount);
    }
}
