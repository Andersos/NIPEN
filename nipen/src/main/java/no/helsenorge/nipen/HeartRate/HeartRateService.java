package no.helsenorge.nipen.HeartRate;

import java.util.List;

public interface HeartRateService {
    void insertHeartRate(HeartRate heartRate);
    List<HeartRate> getHeartRates();
    void deleteHeartRate(long id);
}
