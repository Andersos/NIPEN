package no.helsenorge.nipen.service;

import no.helsenorge.nipen.model.HeartRate;

import java.util.List;

public interface HeartRateService {
    void insertHeartRate(HeartRate heartRate);
    List<HeartRate> getHeartRates();
    void deleteHeartRate(long id);
}
