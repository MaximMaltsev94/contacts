package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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


    public static String appendWhereInSQL(String sql, int size) {
        StringBuilder sqlBuilder = new StringBuilder(sql);
        for(int i = 0; i < size; ++i) {
            sqlBuilder.append(" ?");
            if(i != size - 1) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }

}
