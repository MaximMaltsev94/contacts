package dao.interfaces;

import model.Relationship;

import java.util.List;

/**
 * Created by maxim on 19.09.2016.
 */
public interface RelationshipDao {
    List<Relationship> getAll();
    Relationship getByID(int relationshipID);
}
