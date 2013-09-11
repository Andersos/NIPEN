package no.helsenorge.nipen.heartRate;

import java.util.List;

public interface HeartRateService {
    void insertHeartRate(HeartRate heartRate);
    List<HeartRate> getHeartRates(int userId);
    void deleteHeartRate(HeartRate heartRate);
}
