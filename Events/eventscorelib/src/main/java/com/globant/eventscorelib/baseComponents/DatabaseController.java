package com.globant.eventscorelib.baseComponents;

import com.globant.eventscorelib.domainObjects.Event;

import java.util.List;

// TODO: Adapt this to be used together with a CloudController

/**
 * Created by ariel.cattaneo on 10/04/2015.
 */
abstract public class DatabaseController {
    abstract void init();

    abstract public List<Object> getObjectList(String table);

    abstract public Object getObject(String id, String table);

    abstract public void putObject(Object object, String table);

    abstract public void setObject(Object object, String id, String table);

    // TODO: Methods to access the rest of the objects
}
