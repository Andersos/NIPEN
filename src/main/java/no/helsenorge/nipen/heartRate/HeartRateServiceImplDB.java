package no.helsenorge.nipen.heartRate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Timestamp;

@Component
public final class HeartRateServiceImplDB implements HeartRateService {

    //private DatabaseDataModelImpl databaseDataModel;
    private JdbcTemplate jdbcTemplate;

    @Resource
    @Required
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
    @Resource
    @Required
    public void setDatamodel(DatabaseDataModelImpl dbmodal) {
        databaseDataModel = dbmodal;
    } */

    @Override
    public void insertHeartRate(HeartRate heartRate) throws DataAccessException {
        long userId = heartRate.getId();
        Timestamp timestamp = heartRate.getTimestamp();
        long value = heartRate.getValue();
        String unit = heartRate.getUnit();

        jdbcTemplate.update("INSERT INTO heart_rate (user_id, value, timestamp, unit) " +
                "VALUES (?,?,?,?)", userId, value, timestamp, unit);
    }

    @Override
    public List<HeartRate> getHeartRates(int userId) {
        return jdbcTemplate.query("SELECT id, userId, timestamp, value " +
                "FROM heart_rate ORDER BY timestamp desc", new RowMapper<HeartRate>() {
            @Override
            public HeartRate mapRow(ResultSet resultSet, int i) throws SQLException {
                long id = resultSet.getLong(1);
                long userId = resultSet.getLong(2);
                Timestamp timestamp = resultSet.getTimestamp(3);
                long value = resultSet.getLong(4);
                String unit = resultSet.getString(5);
                return new HeartRate(id, userId, timestamp, value, unit);
            }
        });
    }

    @Override
    public void deleteHeartRate(long heartRateId) {
        jdbcTemplate.update("DELETE FROM heart_rate WHERE id=?", heartRateId);
    }
}
