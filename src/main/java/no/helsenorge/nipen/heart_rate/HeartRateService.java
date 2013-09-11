package main.java.no.helsenorge.nipen.heart_rate;

import java.util.List;

public interface HeartRateService {
    void insertHeartRate(HeartRate heartRate);
    List<HeartRate> getHeartRates(int userId);
    void deleteHeartRate(HeartRate heartRate);
}
