import dao.interfaces.ConnectionFactory;
import dao.mysqlimplementation.MySqlConnectionFactory;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestConnection {
    @Test
    public void testConnection() {
        ConnectionFactory f = MySqlConnectionFactory.getInstance();
        try(Connection c = f.getConnection(); PreparedStatement statement = c.prepareStatement("SELECT * FROM `contact` WHERE ID = ?")) {
            Assert.assertNotNull(c);
            Assert.assertNotNull(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
