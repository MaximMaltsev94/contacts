package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DaoUtils {
    private static final Logger LOG = LoggerFactory.getLogger(DaoUtils.class);
    public static PreparedStatement getPreparedStatement(Connection connection, String sql, Object ...args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for(int i = 0; i < args.length; ++i) {
            statement.setObject(i + 1, args[i]);
        }
        LOG.info("generated statement: {}", statement);
        return statement;
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


    public static PreparedStatement createDynamicWhereInSQL(Connection connection, String prefixSql, String suffixSql, List<?> params, int paramsBeginIndex) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder(prefixSql);
        for(int i = 0; i < params.size(); ++i) {
            sqlBuilder.append(" ?");
            if(i != params.size() - 1) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(") ");
        sqlBuilder.append(suffixSql);

        PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString());
        for (int i = 0; i < params.size(); i++) {
            statement.setObject(i + paramsBeginIndex, params.get(i));
        }

        LOG.info("generated sql query: {}", statement);
        return statement;
    }

}
