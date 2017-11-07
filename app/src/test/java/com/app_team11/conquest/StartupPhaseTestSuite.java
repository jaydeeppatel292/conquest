package com.app_team11.conquest;

/**
 * Created by Nigel on 20-Oct-17.
 */
import com.app_team11.conquest.model.GameMapTest1;
import com.app_team11.conquest.model.InvalidTerritoryAdditionTest;
import com.app_team11.conquest.model.RemoveTerritoryTest;
import com.app_team11.conquest.model.TerritoryTest;
import com.app_team11.conquest.utility.InvalidMapTest;
import com.app_team11.conquest.utility.ReadMapUtilityTest;
import com.app_team11.conquest.utility.ValidMapTest;
import com.app_team11.conquest.model.CardTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


    @RunWith(Suite.class)

    @Suite.SuiteClasses({
            GameMapTest1.class,
            InvalidTerritoryAdditionTest.class,
            RemoveTerritoryTest.class,
            TerritoryTest.class,
            InvalidMapTest.class,
            ReadMapUtilityTest.class,
            ValidMapTest.class,
            CardTest.class
    })

    public class StartupPhaseTestSuite {
    }


