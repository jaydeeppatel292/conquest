package com.app_team11.conquest.model;

import com.app_team11.conquest.global.Constants;
import com.app_team11.conquest.interfaces.PlayerStrategyListener;
import com.app_team11.conquest.utility.AttackPhaseUtility;
import com.app_team11.conquest.utility.ConfigurableMessage;
import com.app_team11.conquest.utility.FileManager;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Created by Jaydeep on 11/27/2017.
 */

public class AggressivePlayerStrategy extends Observable implements PlayerStrategyListener {
    @Override
    public ConfigurableMessage startupPhase(GameMap gameMap, Player player) {
        FileManager.getInstance().writeLog("Aggressive player StartupPhase started !!");
        if (gameMap.getTerrForPlayer(player) != null && gameMap.getTerrForPlayer(player).size() > 0) {
            sortList(gameMap.getTerrForPlayer(player)).get(0).addArmyToTerr(1, false);
            FileManager.getInstance().writeLog("Aggressive player StartupPhase ended !!");
            return new ConfigurableMessage(Constants.MSG_SUCC_CODE, Constants.SUCCESS);
        }
        return new ConfigurableMessage(Constants.MSG_FAIL_CODE, Constants.FAILURE);
    }

    @Override
    public ConfigurableMessage reInforcementPhase(ReinforcementType reinforcementType, GameMap gameMap, Player player) {
        FileManager.getInstance().writeLog("Aggressive player Reinforcement Phase started !!");
        sortList(gameMap.getTerrForPlayer(player)).get(0).setArmyCount(gameMap.getTerrForPlayer(player).get(0).getArmyCount() + (reinforcementType.getOtherTotalReinforcement()));
        FileManager.getInstance().writeLog("Reinforcement phase started for Player :" + gameMap.getPlayerTurn().getPlayerId());
        if (reinforcementType.getMatchedTerritoryList() != null) {
            sortList(reinforcementType.getMatchedTerritoryList()).get(0).setArmyCount(reinforcementType.getMatchedTerritoryList().get(0).getArmyCount() + reinforcementType.getMatchedTerrCardReinforcement());
        }
        FileManager.getInstance().writeLog("Aggressive player Reinforcement Phase ended !!");
        return new ConfigurableMessage(Constants.MSG_SUCC_CODE, Constants.REINFORCEMENT_SUCCESS_STRATEGY);
    }


    @Override
    public ConfigurableMessage attackPhase(GameMap gameMap, Player player) {
        FileManager.getInstance().writeLog("Aggressive player attack phase started !! ");
        boolean isPlayerWon = false;
        if (null != gameMap.getTerrForPlayer(player)) {
            boolean nextAttackerRequired = true;
            for (Territory attackerTerr : sortList(gameMap.getTerrForPlayer(player))) {
                if (null != attackerTerr.getNeighbourList()) {
                    for (Territory defenderTerr : attackerTerr.getNeighbourList()) {
                        if (defenderTerr.getTerritoryOwner() != player) {
                            boolean canContinueAttack = true;
                            while (canContinueAttack) {
                                if (AttackPhaseUtility.getInstance().validateAttackBetweenTerritories(attackerTerr, defenderTerr).getMsgCode() == Constants.MSG_SUCC_CODE) {
                                    nextAttackerRequired = false;
                                    int attackerDice = 3;
                                    if (attackerTerr.getArmyCount() <= 3) {
                                        attackerDice = attackerTerr.getArmyCount() - 1;
                                    }
                                    int defenderDice = 1 + (new Random().nextInt(2));
                                    ConfigurableMessage resultCode = AttackPhaseUtility.getInstance().attackPhase(attackerTerr, defenderTerr, attackerDice, defenderDice);
                                    if (resultCode.getMsgCode() == Constants.MSG_SUCC_CODE) {
                                        isPlayerWon = true;
                                        if (defenderTerr.getArmyCount() == 0) {
                                            AttackPhaseUtility.getInstance().captureTerritory(attackerTerr, defenderTerr, attackerDice);
                                            ObserverType observerType = new ObserverType();
                                            observerType.setObserverType(ObserverType.WORLD_DOMINATION_TYPE);
                                            setChanged();
                                            notifyObservers(observerType);
                                            canContinueAttack = false;
                                            break;
                                        }
                                    }
                                } else {
                                    canContinueAttack = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (!nextAttackerRequired) {
                        if (isPlayerWon) {
                            Cards randomCard = gameMap.getRandomCardFromDeck();
                            player.getOwnedCards().add(randomCard); //adding the card to the player
                            gameMap.removeCardFromDeck(randomCard); //removing from the deck of cards
                        }
                        FileManager.getInstance().writeLog("Aggressive player attack phase ended !! ");
                        return new ConfigurableMessage(Constants.MSG_SUCC_CODE, Constants.ATTACK_SUCCESS_STRATEGY);
                    }
                }
            }
        }
        return new ConfigurableMessage(Constants.MSG_FAIL_CODE, Constants.ATTACK_FAIL_STRATEGY);
    }

    @Override
    public ConfigurableMessage fortificationPhase(GameMap gameMap, Player player) {
        FileManager.getInstance().writeLog("Aggressive player fortification phase started !! ");
        boolean fortificationFlag = false;
        if (null != gameMap.getTerrForPlayer(player)) {
            for (Territory territory : sortList(gameMap.getTerrForPlayer(player))) {
                if (null != territory.getNeighbourList()) {
                    for (Territory neighbourTerr : sortList(territory.getNeighbourList())) {
                        if (neighbourTerr.getTerritoryOwner() == player && neighbourTerr.getArmyCount() > 1) {
                            territory.setArmyCount(territory.getArmyCount() + (neighbourTerr.getArmyCount() - 1));
                            FileManager.getInstance().writeLog("Territory fortified from --> " + neighbourTerr.getTerritoryName().toString() + "" +
                                    "to " + territory.getTerritoryName().toString());
                            FileManager.getInstance().writeLog("Number of fortified armies--> " + (neighbourTerr.getArmyCount() - 1));
                            neighbourTerr.setArmyCount(1);
                            fortificationFlag = true;
                            break;
                        }
                    }
                    if (fortificationFlag) {
                        FileManager.getInstance().writeLog("Aggressive player fortification phase ended !! ");
                        return new ConfigurableMessage(Constants.MSG_SUCC_CODE, Constants.FORTIFICATION_SUCCESS);
                    }
                }
            }
        }
        return new ConfigurableMessage(Constants.MSG_FAIL_CODE, Constants.FORTIFICATION_FAILURE_STRATEGY);
    }

    public List<Territory> sortList(List<Territory> terrList) {

        Collections.sort(terrList, new Comparator<Territory>() {
            @Override
            public int compare(Territory t1, Territory t2) {
                return t2.getArmyCount() - t1.getArmyCount();
            }
        });
        return terrList;
    }
}
