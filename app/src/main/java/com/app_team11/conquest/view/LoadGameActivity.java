package com.app_team11.conquest.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.app_team11.conquest.R;
import com.app_team11.conquest.adapter.MapSelectionAdapter;
import com.app_team11.conquest.global.Constants;
import com.app_team11.conquest.model.MapFile;
import com.app_team11.conquest.utility.FileManager;

import java.util.List;

/**
 * This activity loads the saved game
 * Created by RADHEY on 11/28/2017.
 */

public class LoadGameActivity extends Activity {
    private Bundle bundle;
    private List<MapFile> savedGames;
    private MapSelectionAdapter mapSelectionAdapter;
    private ListView listSavedGames;
    private Intent intent = null;

    /**
     * {@inheritDoc}
     *  This method is called on creation of the activity which allows user to load the saved game
     * @param savedInstanceState When activity is reopened , this parameter is used for resuming to the resumed state
     *
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);
        bundle = getIntent().getExtras();
        initializeView();
        initialization();
        FileManager.getInstance().writeLog("Map selected.");
    }

    /**
     *Saved game initialization from the the root map directory
     */
    private void initialization() {

        savedGames = FileManager.getInstance().getFileFromRootMapDir(Constants.ROOT_GAME_DIR);
        mapSelectionAdapter = new MapSelectionAdapter(savedGames, this);
        listSavedGames.setAdapter(mapSelectionAdapter);

    }

    /**
     * Initialize the view based on the layout defined in XML
     */
    private void initializeView() {
        listSavedGames = (ListView) findViewById(R.id.list_saved_games);
        listSavedGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putString(Constants.KEY_LOAD_SAVED_MAP_PATH, savedGames.get(position).getMapFiles().getPath());
                intent = new Intent(LoadGameActivity.this, GamePlayActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

}
