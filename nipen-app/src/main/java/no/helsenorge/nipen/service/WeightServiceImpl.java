package no.helsenorge.nipen.service;

import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

import no.helsenorge.nipen.database.WeightDAO;
import no.helsenorge.nipen.model.Weight;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

public class WeightServiceImpl implements WeightService {
    private WeightDAO weightDAO;

    @Resource
    @Required
    public void setDataSource(DataSource dataSource) {
        weightDAO = new WeightDAO(new JdbcTemplate(dataSource));
    }

    @Override
    public void insertWeight(Weight weight) {
        weightDAO.insertWeight(weight);
    }

    @Override
    public List<Weight> getWeights() {
        return weightDAO.getWeights();
    }

    @Override
    public void deleteWeight(long id) {
        weightDAO.deleteWeight(id);
    }
}
