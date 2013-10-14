package no.helsenorge.nipen.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import no.helsenorge.nipen.model.HeartRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public final class HeartRateDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public HeartRateDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertHeartRate(HeartRate heartRate) throws DataAccessException {
        long userId = heartRate.getUserId();
        String timestamp = heartRate.getTimestamp();
        long value = heartRate.getValue();
        String unit = heartRate.getUnit();

        jdbcTemplate.update("INSERT INTO heart_rate (user_id, value, timestamp, unit) " +
                "VALUES (?,?,?,?)", userId, value, timestamp, unit);
    }

    public List<HeartRate> getHeartRates() {
        return jdbcTemplate.query("SELECT * FROM heart_rate ORDER BY timestamp ASC", new RowMapper<HeartRate>() {
            @Override
            public HeartRate mapRow(ResultSet resultSet, int i) throws SQLException {
                long id = resultSet.getLong(1);
                long userId = resultSet.getLong(2);
                int value = resultSet.getInt(3);
                String timestamp = resultSet.getTimestamp(4).toString();
                String unit = resultSet.getString(5);
                HeartRate hr = new HeartRate(userId, timestamp, value, unit);
                hr.setId(id);
                return hr;
            }
        });
    }

    public void deleteHeartRate(long heartRateId) {
        jdbcTemplate.update("DELETE FROM heart_rate WHERE id=?", heartRateId);
    }
}
