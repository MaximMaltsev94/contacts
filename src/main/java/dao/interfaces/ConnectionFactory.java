package dao.interfaces;

import exceptions.ConnectionException;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {
    Connection getConnection() throws ConnectionException;
}
