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
        LOG.info(statement.toString());
        return statement;
    }


    public static PreparedStatement createDynamicWhereInSQL(Connection connection, String sql, List<?> params, int paramsBeginIndex) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder(sql);
        for(int i = 0; i < params.size(); ++i) {
            sqlBuilder.append(" ?");
            if(i != params.size() - 1) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(")");

        PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString());
        for (int i = 0; i < params.size(); i++) {
            statement.setObject(i + paramsBeginIndex, params.get(i));
        }

        LOG.info("generated sql query: {}", statement.toString());
        return statement;
    }

}
