package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DaoUtils {
    private static final Logger LOG = LoggerFactory.getLogger(DaoUtils.class);
    public static PreparedStatement getPreparedStatement(Connection connection, String sql, int resultSetType, Object ...args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql, resultSetType);
        fillPreparedStatement(statement, args);
        LOG.info("generated statement: {}", statement);
        return statement;
    }

    public static void fillPreparedStatement(PreparedStatement statement, Object... args) throws SQLException {
        for(int i = 0; i < args.length; ++i) {
            statement.setObject(i + 1, args[i]);
        }
    }

    public static String generateSqlInPart(int size) {
        StringBuilder sqlBuilder = new StringBuilder(" in(");
        for(int i = 0; i < size; ++i) {
            sqlBuilder.append(" ?");
            if(i != size - 1) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(") ");
        return sqlBuilder.toString();
    }
}
