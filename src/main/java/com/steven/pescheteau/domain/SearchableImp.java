package com.steven.pescheteau.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by steve on 01/10/2016.
 */
public abstract class SearchableImp implements Searchable {

    private Logger LOG = LoggerFactory.getLogger(SearchableImp.class);
    @Override
    public String getTable() {
        return getClass().getSimpleName();
    }

    @Override
    public abstract void insert();

    @Override
    public abstract void delete();
}
