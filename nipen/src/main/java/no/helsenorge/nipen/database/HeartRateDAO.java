package no.helsenorge.nipen.database;

import no.helsenorge.nipen.models.HeartRate;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class HeartRateDAO {

    private JdbcTemplate jdbcTemplate;

    public HeartRateDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertHeartRate(HeartRate heartRate) throws DataAccessException {
        int userId = heartRate.getUserId();
        int value = heartRate.getValue();
        String timestamp = heartRate.getTimestamp();
        String unit = heartRate.getUnit();

        jdbcTemplate.update("INSERT INTO heart_rate (user_id, value, timestamp, unit) " +
                "VALUES (?,?,?,?)", userId, value, timestamp, unit);
    }

    public List<HeartRate> getHeartRates() {
        return jdbcTemplate.query("SELECT * FROM heart_rate ORDER BY timestamp ASC", new RowMapper<HeartRate>() {
            @Override
            public HeartRate mapRow(ResultSet resultSet, int i) throws SQLException {
                int id = resultSet.getInt(1);
                int userId = resultSet.getInt(2);
                int value = resultSet.getInt(3);
                String timestamp = resultSet.getString(4);
                String unit = resultSet.getString(5);
                HeartRate hr = new HeartRate(userId, value, timestamp, unit);
                hr.setId(id);
                return hr;
            }
        });
    }

    public void deleteHeartRate(long heartRateId) {
        jdbcTemplate.update("DELETE FROM heart_rate WHERE id=?", heartRateId);
    }
}
