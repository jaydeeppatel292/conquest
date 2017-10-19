package com.app_team11.conquest.model;


import android.graphics.Point;

import com.app_team11.conquest.utility.ConfigurableMessage;
import com.app_team11.conquest.global.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Territory model class with name of territory,position,neighbours,owner,army count,etc
 * Created by Vasu on 06-10-2017.
 */
public class Territory {

    private String territoryName;
    private Point centerPoint;
    private Continent continent;
    private List<Territory> neighbourList;
    private Player territoryOwner;
    private int armyCount;

    public Territory(String territoryName) {
        this.territoryName = territoryName;
        this.neighbourList = new ArrayList();
    }

    public Territory(String territoryName, int centerX, int centerY, Continent continent) {
        this.territoryName = territoryName;
        this.centerPoint = new Point(centerX, centerY);
        this.continent = continent;
        this.neighbourList = new ArrayList();
    }

    public Territory() {
        this.neighbourList = new ArrayList();
    }

    public Territory copyTerritory() {
        Territory territory = new Territory();
        territory.setTerritoryName(this.getTerritoryName());
        territory.setCenterPoint(this.getCenterPoint());
        territory.setContinent(this.getContinent());
        territory.setArmyCount(this.getArmyCount());
        territory.setNeighbourList(this.getNeighbourList());
        territory.setTerritoryOwner(this.getTerritoryOwner());
        return territory;
    }

    private void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }

    /**
     * to be called on click of add neighbours/connections
     * validation1 before saving a map - Validation to check if the number of neighbours not greater than 10
     *
     * @param terrObj
     * @return confirmationMessage
     */
    public ConfigurableMessage addRemoveNeighbourToTerr(Territory terrObj, char addRemoveFlag) {
        if (addRemoveFlag == 'A') {
            if (this.neighbourList.size() <= 9 && terrObj.neighbourList.size() <= 9) {
                this.neighbourList.add(terrObj);
                terrObj.neighbourList.add(this);
                return new ConfigurableMessage(Constants.MSG_SUCC_CODE, Constants.ADD_REM_TO_LIST_SUCCESS);
            } else
                return new ConfigurableMessage(Constants.MSG_FAIL_CODE, Constants.NEIGHBOUR_SIZE_VAL_FAIL);

        } else if (addRemoveFlag == 'R') {
            if (this.neighbourList.size() >= 2 && terrObj.neighbourList.size() >= 2) {
                this.neighbourList.remove(terrObj);
                terrObj.neighbourList.remove(this);
                return new ConfigurableMessage(Constants.MSG_SUCC_CODE, Constants.ADD_REM_TO_LIST_SUCCESS);
            } else
                return new ConfigurableMessage(Constants.MSG_FAIL_CODE, Constants.NEIGHBOUR_SIZE_VAL_FAIL);
        } else
            return new ConfigurableMessage(Constants.MSG_FAIL_CODE, Constants.INCORRECT_FLAG);
    }

    /**
     * validation1 before saving a map - Validation to check if the number of neighbours not greater than 10
     *
     * @param terrList
     * @return confirmationMessage
     */
    public ConfigurableMessage addNeighbourToTerr(List<Territory> terrList) {
        this.neighbourList.addAll(terrList);
        if (this.neighbourList.size() <= 10) {
            return new ConfigurableMessage(Constants.MSG_SUCC_CODE, Constants.ADD_REM_TO_LIST_SUCCESS);
        } else
            return new ConfigurableMessage(Constants.MSG_FAIL_CODE, Constants.NEIGHBOUR_SIZE_VAL_FAIL);

    }

    /**
     * Method to add armies in territory selected and remove the same count from player
     *
     * @param addedArmyCount count of armies to be added
     * @return error message
     */
    public ConfigurableMessage addArmyToTerr(int addedArmyCount) {
        if (this.getTerritoryOwner().getAvailableArmyCount() >= addedArmyCount) {
            this.armyCount += addedArmyCount;
            this.getTerritoryOwner().setAvailableArmyCount(this.getTerritoryOwner().getAvailableArmyCount() - addedArmyCount);
            return new ConfigurableMessage(Constants.MSG_SUCC_CODE, Constants.ARMY_ADDED_SUCCESS);
        } else
            return new ConfigurableMessage(Constants.MSG_FAIL_CODE, Constants.ARMY_ADDED_FAILURE);
    }

    /**
     * Method is used to implement the fortification functionality
     * @param destTerritory the destination territory for fortification
     * @param currentPlayer the player who has requested fortification
     * @param countOfArmy number of armies to be moved
     * @return response message
     */
    public ConfigurableMessage fortifyTerritory(Territory destTerritory, Player currentPlayer, int countOfArmy) {
        if (this.getArmyCount() > countOfArmy && this.getTerritoryOwner().getPlayerId() == currentPlayer.getPlayerId()) {
            Boolean neighbourFlag = false;
            for (Territory obj : this.getNeighbourList()) {
                if (obj.getTerritoryName().equalsIgnoreCase(destTerritory.getTerritoryName())) {
                    this.armyCount -= countOfArmy;
                    destTerritory.armyCount += countOfArmy;
                    neighbourFlag = true;
                    break;
                }
            }
            if (neighbourFlag == true) {
                return new ConfigurableMessage(Constants.MSG_SUCC_CODE, Constants.FORTIFICATION_SUCCESS);
            }
            else
                return new ConfigurableMessage(Constants.MSG_FAIL_CODE, Constants.FORTIFICATION_NEIGHBOUR_FAILURE);
        }
        else
        return new ConfigurableMessage(Constants.MSG_FAIL_CODE, Constants.FORTIFICATION_FAILURE);
    }

    public String getTerritoryName() {
        return territoryName;
    }

    public void setTerritoryName(String territoryName) {
        this.territoryName = territoryName;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(int centerX, int centerY) {
        this.centerPoint = new Point(centerX, centerY);
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public List<Territory> getNeighbourList() {
        return neighbourList;
    }

    public void setNeighbourList(List<Territory> neighbourList) {
        this.neighbourList = neighbourList;
    }

    public Player getTerritoryOwner() {
        return territoryOwner;
    }

    public void setTerritoryOwner(Player territoryOwner) {
        this.territoryOwner = territoryOwner;
    }

    public int getArmyCount() {
        return armyCount;
    }

    public void setArmyCount(int armyCount) {
        this.armyCount = armyCount;
    }

}
