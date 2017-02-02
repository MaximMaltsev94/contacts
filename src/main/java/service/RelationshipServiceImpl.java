package service;

import dao.implementation.RelationshipDaoImpl;
import dao.interfaces.RelationshipDao;
import exceptions.DaoException;
import model.Relationship;

import java.sql.Connection;
import java.util.List;

public class RelationshipServiceImpl implements RelationshipService {
    private Connection connection;

    public RelationshipServiceImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Relationship> getAll() throws DaoException {
        RelationshipDao relationshipDao = new RelationshipDaoImpl(connection);
        return relationshipDao.getAll();
    }

    @Override
    public Relationship getByID(int relationshipID) throws DaoException {
        RelationshipDao relationshipDao = new RelationshipDaoImpl(connection);
        return relationshipDao.getByID(relationshipID);
    }
}
