package no.helsenorge.nipen.service;

import no.helsenorge.nipen.models.HeartRate;

import java.util.List;

public interface HeartRateService {
    void insertHeartRate(HeartRate heartRate);

    List<HeartRate> getHeartRates();

    void deleteHeartRate(long id);
}
