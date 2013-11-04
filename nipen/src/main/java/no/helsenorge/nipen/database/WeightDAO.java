package no.helsenorge.nipen.database;

import no.helsenorge.nipen.model.Weight;
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
        long userId = weight.getUserId();
        String timestamp = weight.getTimestamp();
        long value = weight.getValue();
        String unit = weight.getUnit();

        jdbcTemplate.update("INSERT INTO weight (user_id, value, timestamp, unit) " +
                "VALUES (?,?,?,?)", userId, value, timestamp, unit);
    }

    public List<Weight> getWeights() {
        return jdbcTemplate.query("SELECT * FROM weight ORDER BY timestamp ASC", new RowMapper<Weight>() {
            @Override
            public Weight mapRow(ResultSet resultSet, int i) throws SQLException {
                long id = resultSet.getLong(1);
                long userId = resultSet.getLong(2);
                int value = resultSet.getInt(3);
                String timestamp = resultSet.getTimestamp(4).toString();
                String unit = resultSet.getString(5);
                Weight weight = new Weight(userId, timestamp, value, unit);
                weight.setId(id);
                return weight;
            }
        });
    }

    public void deleteWeight(long weightId) {
        jdbcTemplate.update("DELETE FROM weight WHERE id=?", weightId);
    }

    public void deleteWeights() {
        jdbcTemplate.update("DELETE FROM weight");
    }
}
