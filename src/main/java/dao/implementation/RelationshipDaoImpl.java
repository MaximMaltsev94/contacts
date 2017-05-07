package dao.implementation;

import dao.interfaces.RelationshipDao;
import dao.util.JdbcTemplate;
import dao.util.JdbcTemplateImpl;
import dao.util.ResultSetMapper;
import exceptions.DaoException;
import model.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

public class RelationshipDaoImpl implements RelationshipDao {
    private static final Logger LOG = LoggerFactory.getLogger(RelationshipDaoImpl.class);
    private final String TABLE_NAME = "\"relationship\"";
    private ResultSetMapper<Relationship> rsMapper;
    private JdbcTemplate<Relationship> jdbcTemplate;

    public RelationshipDaoImpl(Connection connection) {
        this.jdbcTemplate = new JdbcTemplateImpl<>(connection);

        rsMapper = rs -> {
            Relationship relationship = new Relationship();
            relationship.setId(rs.getInt("id"));
            relationship.setName(rs.getString("name"));
            return relationship;
        };
    }

    @Override
    public List<Relationship> getAll() throws DaoException {
        LOG.info("selecting all relationship types");
        String sql = String.format("SELECT * FROM %s", TABLE_NAME);
        return jdbcTemplate.queryForList(rsMapper, sql);
    }

    @Override
    public Relationship getByID(int relationshipID) throws DaoException {
        LOG.info("selecting relationship by id - {}", relationshipID);
        String sql = String.format("SELECT * FROM %s WHERE \"id\" = ?", TABLE_NAME);
        return jdbcTemplate.queryForObject(rsMapper, sql, relationshipID);
    }
}
