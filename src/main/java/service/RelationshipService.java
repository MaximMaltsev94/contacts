package service;

import exceptions.DaoException;
import model.Relationship;

import java.util.List;

public interface RelationshipService {
    List<Relationship> getAll() throws DaoException;
    Relationship getByID(int relationshipID) throws DaoException;
}
