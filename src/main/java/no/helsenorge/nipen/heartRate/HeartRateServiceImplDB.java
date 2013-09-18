package no.helsenorge.nipen.heartRate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

public final class HeartRateServiceImplDB implements HeartRateService {
    private JdbcTemplate jdbcTemplate;

    @Resource
    @Required
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insertHeartRate(HeartRate heartRate) throws DataAccessException {
        long userId = heartRate.getUserId();
        String timestamp = heartRate.getTimestamp();
        long value = heartRate.getValue();
        String unit = heartRate.getUnit();

        jdbcTemplate.update("INSERT INTO heart_rate (user_id, value, timestamp, unit) " +
                "VALUES (?,?,?,?)", userId, value, timestamp, unit);
    }

    @Override
    public List<HeartRate> getHeartRates() {
        return jdbcTemplate.query("SELECT * FROM heart_rate ORDER BY timestamp ASC", new RowMapper<HeartRate>() {
            @Override
            public HeartRate mapRow(ResultSet resultSet, int i) throws SQLException {
                long id = resultSet.getLong(1);
                long userId = resultSet.getLong(2);
                long value = resultSet.getLong(3);
                String timestamp = resultSet.getTimestamp(4).toString();
                String unit = resultSet.getString(5);
                HeartRate hr = new HeartRate(userId, value, timestamp, unit);
                hr.setId(id);
                return hr;
            }
        });
    }

    @Override
    public void deleteHeartRate(long heartRateId) {
        jdbcTemplate.update("DELETE FROM heart_rate WHERE id=?", heartRateId);
    }
}
