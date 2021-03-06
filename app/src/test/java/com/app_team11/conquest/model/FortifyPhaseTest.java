package com.app_team11.conquest.model;

import com.app_team11.conquest.global.Constants;
import com.app_team11.conquest.model.Cards;
import com.app_team11.conquest.model.Continent;
import com.app_team11.conquest.model.GameMap;
import com.app_team11.conquest.model.Player;
import com.app_team11.conquest.model.ReinforcementType;
import com.app_team11.conquest.model.Territory;
import com.app_team11.conquest.utility.ConfigurableMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Nigel on 19-Oct-17.
 * invalid Fortify Phase Test
 */

public class FortifyPhaseTest {
    private List<Territory> territoryList;
    private List<Player> playerList;
    private List<Continent> continentList;
    private Continent continent;
    private Territory territory;
    private Player player;
    private int i;

    /**
     * Initalizes variables for the test
     */
    @Before
    public void setup()
    {
        continentList=new ArrayList<Continent>();
        territoryList=new ArrayList<Territory>();
        playerList=new ArrayList<Player>();
        continent=new Continent("Test Continent 1",7);
        continentList.add(continent);

        for(int i=1;i<=2;i++) {
            player = new Player();
            player.setPlayerId(i);
            player.setCardTradeIn(true);
            playerList.add(player);
        }

        for(i=1;i<=2;i++)
        {
            territory=new Territory("Test Territory "+i,0,i,continent);
            territory.setTerritoryOwner(playerList.get(i-1));
            territoryList.add(territory);
        }

        //assign territories to players
        territoryList.get(0).setTerritoryOwner(playerList.get(0));
        territoryList.get(0).setArmyCount(15);
        territoryList.get(1).setTerritoryOwner(playerList.get(0));
    }

    /**
     * test for invalid fortification phase scenario
     */
    @Test
    public void invalidFortifyPhase()
    {
       assertEquals(0,playerList.get(0).fortifyTerritory(territoryList.get(0), territoryList.get(1), 10).getMsgCode());
    }

    /**
     * Clean up the test data
     */
    @After
    public void cleanup()
    {
        territoryList=null;
       territory=null;
        continent=null;
        continentList=null;
        player=null;
        playerList=null;
    }
}
