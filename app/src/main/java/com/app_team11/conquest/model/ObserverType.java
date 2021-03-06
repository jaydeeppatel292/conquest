package com.app_team11.conquest.model;

/**
 * World domination view and phase view constant definition
 * Created by Jaydeep9101 on 08-Nov-17.
 */

public class ObserverType {
    public static final int WORLD_DOMINATION_TYPE=1;
    public static final int PHASE_VIEW_UPDATE_TYPE=2;
    private int observerType;

    /**
     * Returns the observer type
     * @return observerType : type of observer is returned
     */
    public int getObserverType() {
        return observerType;
    }

    /**
     * Sets the observer type
     * @param observerType : type of observer is defined
     */
    public void setObserverType(int observerType) {
        this.observerType = observerType;
    }
}
