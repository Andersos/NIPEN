package no.helsenorge.nipen.heartRate;

import org.springframework.stereotype.Service;

import java.util.List;

public interface HeartRateService {
    void insertHeartRate(HeartRate heartRate);
    List<HeartRate> getHeartRates();
    void deleteHeartRate(long id);
}
