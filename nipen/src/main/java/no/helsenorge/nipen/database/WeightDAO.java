package no.helsenorge.nipen.database;

import no.helsenorge.nipen.models.Weight;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class WeightDAO {

    private JdbcTemplate jdbcTemplate;

    public WeightDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertWeight(Weight weight) {
        int userId = weight.getUserId();
        int value = weight.getValue();
        String timestamp = weight.getTimestamp();
        String unit = weight.getUnit();

        jdbcTemplate.update("INSERT INTO weight (user_id, value, timestamp, unit) " +
                "VALUES (?,?,?,?)", userId, value, timestamp, unit);
    }

    public List<Weight> getWeights() {
        return jdbcTemplate.query("SELECT * FROM weight ORDER BY timestamp ASC", new RowMapper<Weight>() {
            @Override
            public Weight mapRow(ResultSet resultSet, int i) throws SQLException {
                int id = resultSet.getInt(1);
                int userId = resultSet.getInt(2);
                int value = resultSet.getInt(3);
                String timestamp = resultSet.getString(4);
                String unit = resultSet.getString(5);
                Weight weight = new Weight(userId, value, timestamp, unit);
                weight.setId(id);
                return weight;
            }
        });
    }

    public void deleteWeight(long weightId) {
        jdbcTemplate.update("DELETE FROM weight WHERE id=?", weightId);
    }
}
