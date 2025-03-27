package io.github.tracingperformancelabs.dupe.services;

import io.github.tracingperformancelabs.dupe.model.DataPoint;

public interface DataStorageService<ID> {

    void store(DataPoint<ID> data);

}
