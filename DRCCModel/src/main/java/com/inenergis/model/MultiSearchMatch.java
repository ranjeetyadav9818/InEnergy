package com.inenergis.model;

/**
 * Created by Antonio on 24/08/2017.
 */
public interface MultiSearchMatch extends SearchMatch{

    String[] getActions();
    String getAction();
    MultiSearchMatch clone(String action);
}
