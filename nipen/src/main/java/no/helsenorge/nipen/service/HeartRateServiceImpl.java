package no.helsenorge.nipen.service;

import no.helsenorge.nipen.database.HeartRateDAO;
import no.helsenorge.nipen.model.HeartRate;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

public class HeartRateServiceImpl implements HeartRateService {
    private HeartRateDAO heartRateDAO;

    @Resource
    @Required
    public void setDataSource(DataSource dataSource) {
        heartRateDAO = new HeartRateDAO(new JdbcTemplate(dataSource));
    }

    @Override
    public void insertHeartRate(HeartRate heartRate) {
        heartRateDAO.insertHeartRate(heartRate);
    }

    @Override
    public List<HeartRate> getHeartRates() {
        return heartRateDAO.getHeartRates();
    }

    @Override
    public void deleteHeartRate(long id) {
        heartRateDAO.deleteHeartRate(id);
    }

    @Override
    public void deleteHeartRates() {
        heartRateDAO.deleteHeartRates();
    }
}
