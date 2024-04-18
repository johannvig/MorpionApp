package com.example.morpion.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.example.morpion.R;
import com.example.morpion.ui.Board.GameBoard;


public class GameSetting extends AppCompatActivity {


    private View pseudoName2, editName2, chooseLevel, playFirstText, playFirst ;
    private Spinner spinner;
    private RadioGroup radioGroupFirstPlayer;
    private static final String MODE_MULTIPLAYER = "MULTIPLAYER";




    /**
     * Initialisation de l'activité, configuration de l'interface utilisateur.
     * @param savedInstanceState État de l'instance sauvegardée.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setting);

        initViews();
        setupBackButton();
        configureVisibilityBasedOnMode();

        spinner = findViewById(R.id.spinner);
        setupSpinner();

        View boardView = findViewById(R.id.button);
        boardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick();
            }
        });



    }

    /**
     * Gère le clic sur le bouton pour lancer le jeu en fonction du mode sélectionné.
     */
    private void handleButtonClick() {
        String modeType = getIntent().getStringExtra("MODE_TYPE");
        radioGroupFirstPlayer = findViewById(R.id.radioGroupFirstPlayer);
        int startingPlayerId = radioGroupFirstPlayer.getCheckedRadioButtonId();
        String startingPlayer = (startingPlayerId == R.id.radioButtonPlayer1) ? "Player 1" : "Player 2";
        Log.d("GameSetting", "Mode Type: " + modeType);

        if (MODE_MULTIPLAYER.equals(modeType)) {
            startMultiplayerGame();
        } else {
            startSoloGame();
        }
    }


    /**
     * Récupère le joueur sélectionné pour commencer la partie à partir du groupe radio.
     * @return Le joueur sélectionné pour commencer la partie.
     */
    private String getStartingPlayer() {
        int startingPlayerId = radioGroupFirstPlayer.getCheckedRadioButtonId();
        return (startingPlayerId == R.id.radioButtonPlayer1) ? "Player 1" : "Player 2";
    }

    /**
     * Démarrage du jeu en mode multijoueur.
     */
    private void startMultiplayerGame() {
        String playerName1 = ((EditText) findViewById(R.id.editName)).getText().toString();
        String playerName2 = ((EditText) findViewById(R.id.editName2)).getText().toString();
        String startingPlayer = getStartingPlayer();

        Intent playActivity = new Intent(GameSetting.this, GameBoard.class);
        playActivity.putExtra("PLAYER_NAME_1", playerName1);
        playActivity.putExtra("PLAYER_NAME_2", playerName2);
        playActivity.putExtra("STARTING_PLAYER", startingPlayer);
        startActivity(playActivity);
    }

    /**
     * Démarrage du jeu en mode solo.
     */
    private void startSoloGame() {
        String playerName = ((EditText) findViewById(R.id.editName)).getText().toString();
        String difficulty = spinner.getSelectedItem().toString();
        String startingPlayer = getStartingPlayer();

        Intent playActivity = new Intent(GameSetting.this, GameBoard.class);
        playActivity.putExtra("PLAYER_NAME_1", playerName);
        playActivity.putExtra("PLAYER_NAME_2", "IA");
        playActivity.putExtra("DIFFICULTY", difficulty);
        playActivity.putExtra("STARTING_PLAYER", startingPlayer);
        startActivity(playActivity);
        Log.e("GameSetting", "passe ok");
    }




    /**
     * Configure le spinner pour sélectionner la difficulté du jeu.
     */
    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * Configuration du bouton de retour.
     */
    private void setupBackButton() {
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }



    /**
     * Initialisation des vues.
     */
    private void initViews() {
        pseudoName2 = findViewById(R.id.pseudoName2);
        editName2 = findViewById(R.id.editName2);
        spinner = findViewById(R.id.spinner);
        chooseLevel = findViewById(R.id.chooseLevel);
        playFirst = findViewById(R.id.radioGroupFirstPlayer);
        playFirstText = findViewById(R.id.whoPlayFirst);
    }

    /**
     * Configure la visibilité des composants en fonction du mode de jeu.
     */
    private void configureVisibilityBasedOnMode() {
        String modeType = getIntent().getStringExtra("MODE_TYPE");
        setAllComponentsVisibility(View.GONE);

        if ("MULTIPLAYER".equals(modeType)) {
            pseudoName2.setVisibility(View.VISIBLE);
            editName2.setVisibility(View.VISIBLE);
            playFirst.setVisibility(View.VISIBLE);
            playFirstText.setVisibility(View.VISIBLE);
        } else{
            chooseLevel.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            playFirst.setVisibility(View.VISIBLE);
            playFirstText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Définit la visibilité de tous les composants.
     * @param visibility Visibilité à appliquer (par exemple, View.GONE).
     */
    private void setAllComponentsVisibility(int visibility) {
        pseudoName2.setVisibility(visibility);
        editName2.setVisibility(visibility);
        spinner.setVisibility(visibility);
        chooseLevel.setVisibility(visibility);
        playFirst.setVisibility(visibility);
        playFirstText.setVisibility(visibility);
    }



}
