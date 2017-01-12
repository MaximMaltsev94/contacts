package dao.interfaces;

import exceptions.DaoException;
import model.Relationship;

import java.util.List;

/**
 * Created by maxim on 19.09.2016.
 */
public interface RelationshipDao {
    List<Relationship> getAll() throws DaoException;
    Relationship getByID(int relationshipID) throws DaoException;
}
