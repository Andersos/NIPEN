package no.helsenorge.nipen.service;

import no.helsenorge.nipen.model.Weight;

import java.util.List;

public interface WeightService {
    void insertWeight(Weight weight);
    List<Weight> getWeights();
    void deleteWeight(long id);
}
