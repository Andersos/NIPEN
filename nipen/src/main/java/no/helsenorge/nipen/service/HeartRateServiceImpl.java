package no.helsenorge.nipen.service;

import no.helsenorge.nipen.database.HeartRateDAO;
import no.helsenorge.nipen.models.HeartRate;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

@Component
public class HeartRateServiceImpl implements HeartRateService {

    private HeartRateDAO heartRateDAO;

    @Resource(name = "dataSource")
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
}
