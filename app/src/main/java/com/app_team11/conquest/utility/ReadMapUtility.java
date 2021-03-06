package com.app_team11.conquest.utility;



import android.content.Context;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.app_team11.conquest.model.GameMap;
import com.app_team11.conquest.model.PhaseViewModel;
import com.app_team11.conquest.model.Territory;
import com.app_team11.conquest.model.Continent;
import com.app_team11.conquest.model.Player;

/**
 * Reads the map from the text file
 * Created by Nigel on 13-Oct-17.
 */
public class ReadMapUtility {
    int noOfArmies;
    String line = null;
    String[] params;
    Territory t, tConnected;
    Continent c;
    List<Territory> territoryList = new ArrayList<Territory>();
    List<Continent> continentList = new ArrayList<Continent>();
    List<Territory> connectedTerritories = new ArrayList<Territory>();
    List<Player> playerDetails = new ArrayList<Player>();
    boolean stop = false;

    GameMap gm = new GameMap();
    List<Territory> tempT = new ArrayList<Territory>();

    Context context;

    /**
     * This utility is responsible for reading the map
     * @param context the current running instance
     */
    public ReadMapUtility(Context context) {
        this.context = context;
    }

    /**
     * Default Constructor
     */
    public ReadMapUtility() {   }

    /**
     * Current List of territory is displayed
     * @return territoryList list of territory
     */
    public List<Territory> currentTerritories() {
        return territoryList;
    }

    /**
     * This method takes a file path as input and generates a GameMap object for Map creation
     * @param filePath file path
     * @return map object
     */
    public GameMap readFile(String filePath)
    {
        try {
            FileReader f = new FileReader(filePath);
            Scanner sc = new Scanner(f);

            while (sc.hasNext()) {
                line = sc.nextLine();

                switch (findCurrentPart(line)) {
                    case "map":

                        line = sc.nextLine();
                        while (!line.contains("[") && !line.isEmpty()) {
                            line = sc.nextLine();

                        }
                        break;

                    case "continent":
                        line = sc.nextLine();
                        while (!line.contains("[") && !line.isEmpty() && sc.hasNext()) {
                            params = line.split("\\=");
                            c = new Continent(params[0], Integer.parseInt(params[1]),context);
                            continentList.add(c);
                            line = sc.nextLine();
                        }
                        break;

                    case "territory":
                        line=sc.nextLine();
                        while (!line.contains("[") && !line.isEmpty()) {
                            //for neighbours
                            params = line.trim().split("\\,");
                            for (int i = 4; i < params.length; i++) {
                                if (ifTerritoryExists(params[i].trim())) {
                                    tConnected = searchTerritory(params[i].trim());
                                } else {
                                    createTerritory(params[i].trim(), 0, 0, null);
                                    tConnected = searchTerritory(params[i].trim());
                                }
                                connectedTerritories.add(tConnected);
                            }
                            //for main territory
                            if (ifTerritoryExists(params[0].trim())) {
                                updateTerritory(params[0].trim(), params[1].trim(), params[2].trim(), params[3].trim(), connectedTerritories);
                            } else {
                                createTerritory(params[0].trim(), Integer.parseInt(params[1].trim()), Integer.parseInt(params[2].trim()), setContinent(params[3].trim()));
                            }
                            connectedTerritories.clear();

                            if (sc.hasNext()) {
                                line=sc.nextLine();
                                if(line.isEmpty())
                                    line=sc.nextLine();

                            } else {
                                break;
                            }


                        }
                        break;

                }
            }

        } catch (Exception e) {
            System.out.println("Exception" + e);
            e.printStackTrace();
            return null;
        }
        gm = new GameMap();
        gm.setContinentList(continentList);
        gm.setTerritoryList(territoryList);
        String message="Map formed correctly";
        PhaseViewModel.getInstance().addPhaseViewContent(message);

        return gm;

    }

    /**
     * Sets the name of the continent
     * @param continentName name of the continent is defined
     * @return continent returns the name of the current continent
     */
    public Continent setContinent(String continentName) {

        for (Continent continent : continentList) {
            if (continentName.trim().equals(continent.getContName().trim())) {
                return continent;
            }
        }
        return null;


    }

    /**
     * Checks if the territory exists
     * @param tName name of the territory is checked
     * @return true returns boolean on checking
     */
    public boolean territoryExists(String tName) {
        for (int i = 0; i < territoryList.size(); i++) {
            if (tName.equals(territoryList.get(i).getTerritoryName())) {
                return true;
            }
        }
        return false;
    }

    //find whether data falls in the Map,Territory or Continent section
    public static String findCurrentPart(String line) {
        if (line.contains("[Map]")) {
            return "map";
        } else if (line.contains("[Continents]")) {
            return "continent";
        } else {
            return "territory";
        }

    }

    /**
     * Prints the list of territory as per the size
     */
    public void printTerritoryList() {
        System.out.println("TerritoryList size: " + territoryList.size());
        for (int i = 0; i < territoryList.size(); i++) {
            System.out.println("===============Territory List=========================");
            System.out.println(territoryList.get(i).getTerritoryName() + "\t"
                    + territoryList.get(i).getCenterPoint() + "\t"
                    + territoryList.get(i).getContinent().getContName() + "\t"
            );

            if (territoryList.get(i).getNeighbourList().size() > 0)
                for (int j = 0; j < territoryList.get(i).getNeighbourList().size(); j++) {
                    System.out.println("Size: " + territoryList.get(i).getNeighbourList().size());
                    System.out.println("Neighbouring " + j + ": " + territoryList.get(i).getNeighbourList().get(j).getTerritoryName());
                }
            System.out.println("=====================================================");
        }
    }

    /**
     * Prints the list of continent as per the size of the continent list
     */
    public void printContinentList() {
        System.out.println("====Continent List===");
        for (int i = 0; i < continentList.size(); i++) {
            System.out.println(continentList.get(i).getContName() + "\t"
                    + continentList.get(i).getScore() + "\t");
        }
    }

    /**
     * Checks if the territory exists or not
     * @param territoryName parameter for the name of the territory
     * @return true boolean is returned if territory exist
     */
    public boolean ifTerritoryExists(String territoryName) {
        for (int i = 0; i < territoryList.size(); i++) {
            if (territoryList.get(i).getTerritoryName().equalsIgnoreCase(territoryName))
                return true;
        }
        return false;
    }

    /**
     * This method searches for a territory from the territoryList
     * @param territoryName name of the territory
     * @return territoryList list of territory is returned
     */
    public Territory searchTerritory(String territoryName) {
        for (int i = 0; i < territoryList.size(); i++)
            if (territoryList.get(i).getTerritoryName().equalsIgnoreCase(territoryName)) {
                //System.out.println("Territory Found");
                return territoryList.get(i);
            }
        return null;
    }

    /**
     * This method searches for a continent in the continentList
     * @param continentName parameter for name of the continent
     * @return continentList list of continent is returned
     */
    public Continent searchContinent(String continentName) {
        for (int i = 0; i < continentList.size(); i++)
            if (continentList.get(i).getContName().equalsIgnoreCase(continentName)) {
                return continentList.get(i);
            }
        return null;
    }

    /**
     * This method creates a new territory
     * @param tName name of the territory
     * @param X coordinate X for the point selected
     * @param Y coordinate Y for the point selected
     * @param cont returns the continent
     */
    public void createTerritory(String tName, int X, int Y, Continent cont) {
        t = new Territory(tName.trim(), X, Y, cont);
        territoryList.add(t);
    }

    /**
     * This method updates an existing territory
     * @param tName name of the territory
     * @param X coordinate X for the point selected
     * @param Y coordinate Y for the point selected
     * @param continent defines name of the continent
     * @param connectedT parameter for the connected territory
     */
    public void updateTerritory(String tName, String X, String Y, String continent, List<Territory> connectedT) {
        Territory tUpdate = searchTerritory(tName);
        Continent cont = searchContinent(continent);
        tUpdate.setCenterPoint(Integer.parseInt(X), Integer.parseInt(Y));
        tUpdate.setContinent(cont);
        tempT = new ArrayList<Territory>();
        tempT.add(tUpdate);

        tUpdate.addNeighbourToTerr(connectedT);
    }

    /**
     * This method creates territories
     * @param tName name of the territory
     * @param X coordinate X for the point selected
     * @param Y coordinate Y for the point selected
     * @param continent defines name of the continent
     * @param connectedT parameter for the connected territory
     */
    public void createTerritory(String tName, String X, String Y, String continent, List<Territory> connectedT) {
        Continent cont1 = searchContinent(continent);
        Territory tNew = new Territory(tName, Integer.parseInt(X), Integer.parseInt(Y), cont1);
        tempT = new ArrayList<Territory>();
        tempT.add(tNew);
        tNew.setNeighbourList(connectedT);
        territoryList.add(tNew);
    }

    /**
     * This method takes an input for number of players and assigns armies accordingly.
     * @param noOfPlayers parameter for the number of players
     * @return details of player are returned
     */
    public List<Player> assignArmies(int noOfPlayers) {

        Player p = null;
        if (noOfPlayers == 2)
            noOfArmies = 40;
        else if (noOfPlayers == 3) {
            noOfArmies = 35;
        } else if (noOfPlayers == 4) {
            noOfArmies = 30;
        } else if (noOfPlayers == 5) {
            noOfArmies = 25;
        } else if (noOfPlayers == 6) {
            noOfArmies = 20;
        }
        //***Add condition when noOfPlayers=2
        for (int i = 1; i <= noOfPlayers; i++) {
            p = new Player();
            p.setPlayerId(i);
            p.setAvailableArmyCount(noOfArmies);
            playerDetails.add(p);
        }
        return playerDetails;
    }

    /**
     * Returns the first player
     * @param Players parameter for the players
     * @return Players returns the players
     */
    public static Player getFirstPlayer(List<Player> Players) {
        int rnd = new Random().nextInt(Players.size());
        return Players.get(rnd);
    }

    /**
     * This method randomly assigns countries to players
     * @param Players parameter for the players
     * @param Territories parameter or the territories are defines
     * @return returns the current player
     */
     public List<Player> randomlyAssignCountries(List<Player> Players, List<Territory> Territories) {
        int territoryCount = 0,playerCount=0;
        Collections.shuffle(Territories);
        while (Territories.size() > 0 && territoryCount<Territories.size())
        {
            while (Players.size()> 0)
            {
                Territories.get(territoryCount).setTerritoryOwner(Players.get(playerCount));
                Territories.get(territoryCount).addArmyToTerr(1,false);
                if (playerCount == Players.size()-1)
                {
                    playerCount = -1;
                }

                break;
            }
            territoryCount++;
            playerCount++;

        }
        return Players;
    }

    /**
     * This method assigns armies to the territories based on the respective user's choice
     * @param playerList the player list
     */
    public void armyAssignment(List<Player> playerList) {
        Player p = new Player();
        List<Territory> temp = new ArrayList<Territory>();
        Scanner sc = new Scanner(System.in);
        Boolean loop = true;
        int tNumber;
        boolean needToAssignArmy = true;
        while (needToAssignArmy) {
            needToAssignArmy = false;
            for (int i = 0; i < playerList.size(); i++) {
                System.out.println("==========Player " + (i + 1) + "===============");
                System.out.println("Available armies: " + playerList.get(i).getAvailableArmyCount());
                p.setPlayerId(i + 1);
                temp = gm.getTerrForPlayer(p);
                if (playerList.get(i).getAvailableArmyCount() == 0) {
                    break;
                }
                System.out.println("\nTerritories:");
                for (int k = 0; k < temp.size(); k++) {
                    System.out.println(k + 1 + "." + temp.get(k).getTerritoryName() + "\n");
                }
                System.out.println("Select territory number: ");
                tNumber = sc.nextInt();
                tNumber = tNumber - 1;

                if (playerList.get(i).getAvailableArmyCount() > 0) {
                    temp.get(tNumber).addArmyToTerr(1, false);
                    if(playerList.get(i).getAvailableArmyCount() > 0) {
                        needToAssignArmy = true;
                    }
                }
            }
        }
    }

}
