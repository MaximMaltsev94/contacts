package service;

import dao.implementation.RelationshipDaoImpl;
import dao.interfaces.RelationshipDao;
import exceptions.DaoException;
import model.Relationship;

import java.sql.Connection;
import java.util.List;

public class RelationshipServiceImpl implements RelationshipService {

    private RelationshipDao relationshipDao;

    public RelationshipServiceImpl(Connection connection) {
        relationshipDao = new RelationshipDaoImpl(connection);
    }

    @Override
    public List<Relationship> getAll() throws DaoException {
        return relationshipDao.getAll();
    }

    @Override
    public Relationship getByID(int relationshipID) throws DaoException {
        return relationshipDao.getByID(relationshipID);
    }
}
