package com.app_team11.conquest.model;

import com.app_team11.conquest.global.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Player model class with cards, score and owned territories information
 * Created by Vasu on 08-10-2017.
 *
 * @version 1.0.0
 */

public class Player {

    private int playerId;
    private int availableArmyCount;
    private List<Cards> ownedCards;
    private Boolean cardTradeIn = false;
    private boolean isMyTurn;

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getAvailableArmyCount() {
        return availableArmyCount;
    }

    public void setAvailableArmyCount(int availableArmyCount) {
        this.availableArmyCount = availableArmyCount;
    }

    public List<Cards> getOwnedCards() {
        return ownedCards;
    }

    public void setOwnedCards(List<Cards> ownedCards) {
        this.ownedCards = ownedCards;
    }

    public Boolean getCardTradeIn() {
        return cardTradeIn;
    }

    public void setCardTradeIn(Boolean cardTradeIn) {
        this.cardTradeIn = cardTradeIn;
    }

    /**
     * Method to check if the player can trade in the cards
     */
    public void checkCardsForTradeIn() {
        int infantryCount = 0;
        int cavalryCount = 0;
        int artilleryCount = 0;

        for (Cards obj : this.ownedCards) {
            if (null != obj.getArmyType() && obj.getArmyType().toString().equalsIgnoreCase(Constants.ARMY_INFANTRY)) {
                infantryCount++;
            } else if (null != obj.getArmyType() && obj.getArmyType().toString().equalsIgnoreCase(Constants.ARMY_CAVALRY)) {
                cavalryCount++;
            } else if (null != obj.getArmyType() && obj.getArmyType().toString().equalsIgnoreCase(Constants.ARMY_ARTILLERY)) {
                artilleryCount++;
            }

        }
        if (infantryCount >= 3 || cavalryCount >= 3 || artilleryCount >= 3 || (infantryCount >= 1 && cavalryCount >= 1 && artilleryCount >= 1))
            this.cardTradeIn = true;

    }

    /**
     * This method is used to calculate the total number of reinforcement armies
     *
     * @param gameMap
     * @param demandedCardTrade
     * @param cardTradeCount
     * @param tradeInCards
     * @return Reinforcement Army Count
     */
    public ReinforcementType calcReinforcementArmy(GameMap gameMap, boolean demandedCardTrade, int cardTradeCount, List<Cards> tradeInCards) {
        int ownedTerritoryCount = 0;
        int territoryArmy = 3;
        int continentArmy = 0;
        int cardArmy = 0;
        ReinforcementType reinforcementCount = new ReinforcementType();
        boolean ownedTerritoryCard = false;
        for (Territory terrObj : gameMap.getTerritoryList()) {
            if (terrObj.getTerritoryOwner().getPlayerId() == this.playerId) {
                ownedTerritoryCount++;
            }
        }
        if(ownedTerritoryCount>9) //calculating territory army only when owned territory count > 9, else default value is set to 3
        territoryArmy = ownedTerritoryCount / 3;

        for (Continent contObj : gameMap.getContinentList()) {
            if (contObj.getContOwner() != null && contObj.getContOwner().getPlayerId() == this.playerId) {
                continentArmy += contObj.getScore();
            }
        }

        if (demandedCardTrade) {
            //// TODO: 08-10-2017 need to add the selected cards back to the total cards list and incrementing the card trade count in each trade
            //// TODO: 17-10-2017 what happens when the total cards in the inventory finishes and there is no card left to give to the player when he wins any territory
            switch (cardTradeCount) {
                case 1:
                    cardArmy = 4;
                    break;
                case 2:
                    cardArmy = 6;
                    break;
                case 3:
                    cardArmy = 8;
                    break;
                case 4:
                    cardArmy = 10;
                    break;
                case 5:
                    cardArmy = 12;
                    break;
                case 6:
                    cardArmy = 15;
                    break;
            }
            if (cardTradeCount > 6)
                cardArmy = 15 + (cardTradeCount - 6) * 5; //for trade count more than 6

            List<Territory> matchedTerr = new ArrayList<Territory>();
            for (Territory terrObj : gameMap.getTerritoryList()) {
                for (Cards cardObj : tradeInCards) {
                    if ((terrObj.getTerritoryOwner().getPlayerId() == this.getPlayerId()) && terrObj.getTerritoryName().equalsIgnoreCase(cardObj.getCardTerritory().getTerritoryName())) {
                        reinforcementCount.setMatchedTerrCardReinforcement(2); //adding the count of armies for cards having the territory owned by the player
                        matchedTerr.add(terrObj);
                    }
                }
                reinforcementCount.setMatchedTerritoryList(matchedTerr);
            }
            this.getOwnedCards().removeAll(tradeInCards);
        }
        reinforcementCount.setOtherTotalReinforcement(territoryArmy + continentArmy + cardArmy);
        return reinforcementCount;
    }


}
