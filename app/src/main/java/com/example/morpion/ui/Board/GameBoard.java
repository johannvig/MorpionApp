package com.example.morpion.ui.Board;

import com.example.morpion.ai.LocalAI;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.morpion.R;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

public class GameBoard extends AppCompatActivity {

    private GameViewModel viewModel;
    private TextView scorePlayerOne;
    private TextView scorePlayerTwo;
    private TextView whoPlayTextView;
    private String currentDifficulty;

    private String gameMode;




    /**
     * Appelée lors du démarrage de l'activité. Initialise le plateau de jeu.
     * @param savedInstanceState Si l'activité est réinitialisée après avoir été précédemment fermée, ce Bundle contient les données qu'elle avait fournies dans onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);


        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        String startingPlayer = getIntent().getStringExtra("STARTING_PLAYER");


        String playerName1 = getIntent().getStringExtra("PLAYER_NAME_1");
        String playerName2 = getIntent().getStringExtra("PLAYER_NAME_2");


        TextView textViewPseudo1 = findViewById(R.id.pseudo1);
        TextView textViewPseudo2 = findViewById(R.id.pseudo2);


        scorePlayerOne = findViewById(R.id.score1);
        scorePlayerTwo = findViewById(R.id.score2);

        whoPlayTextView = findViewById(R.id.who_play);


        currentDifficulty = getIntent().getStringExtra("DIFFICULTY");
        int difficultyLevel = convertDifficultyToLevel(currentDifficulty);


        if (startingPlayer != null) {
            viewModel.setStartingPlayer(startingPlayer);
        }

        if (currentDifficulty != null) {
            viewModel.startGame(currentDifficulty);
        } else {
            viewModel.startGame(null); // Pas de difficulté pour le mode multijoueur
        }



        if (playerName1 != null && playerName2 != null) {
            Log.d("GameBoard", "Both players are not null");
            textViewPseudo1.setText(playerName1);
            textViewPseudo2.setText(playerName2);
            viewModel.setPlayerNames(playerName1, playerName2);
            viewModel.setStartingPlayer(startingPlayer);


        }

        else if(playerName1 != null &&  currentDifficulty!= null){
            Log.d("GameBoard", "Single player with difficulty");
            textViewPseudo1.setText(playerName1);
            viewModel.setPlayerNames(playerName1, "IA");
            viewModel.setStartingPlayer(startingPlayer);
            Log.e("GameViewModel","AI set up");


            // Déclencher le tour de l'IA si nécessaire
            if ("IA".equals(startingPlayer) || "Player 2".equals(startingPlayer)) {
                Log.e("GameViewModel","AI turn");
                viewModel.playAITurn();
            }
        }
        else{
            Log.d("GameBoard", "Some other condition");
        }


        viewModel.getCurrentTurn().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String currentTurnText) {
                whoPlayTextView.setText(currentTurnText);
            }
        });

        viewModel.getPlayerOneScore().observe(this, score -> {
            scorePlayerOne.setText(String.valueOf(score));
        });

        viewModel.getPlayerTwoScore().observe(this, score -> {
            scorePlayerTwo.setText(String.valueOf(score));
        });

        viewModel.getAiMoveLiveData().observe(this, aiMove -> {
            if (aiMove != null) {
                updateUIForAIMove(aiMove[0], aiMove[1]);
            }
        });






        viewModel.getGameLiveData().observe(this, game -> {
            // Mettre à jour l'interface utilisateur en fonction de l'état du jeu
        });

        viewModel.getPlayerNames().observe(this, playerNames -> {
            textViewPseudo1.setText(playerNames[0]);
            textViewPseudo2.setText(playerNames[1]);
        });



        setupGameBoardClickListeners();

        viewModel.getGameStateLiveData().observe(this, gameState -> {
            Log.e("GameViewModel","terminer");
            if (gameState.equals("finished")) {  // Modifié pour correspondre à la valeur mise à jour dans le LiveData
                Log.e("GameViewModel","pop up");
                String message = "La partie est terminée. Voulez-vous rejouer ?";
                showEndGameDialog("Partie terminée", message);
            }
        });



        gameMode = getIntent().getStringExtra("GAME_MODE");

       if ("SOLO".equals(gameMode)) {

            setupSoloGame();
        } else if ("MULTIPLAYER_LOCAL".equals(gameMode)) {
            setupLocalMultiplayerGame(); // Aucun argument supplémentaire nécessaire pour le multijoueur local
        }




    }



    /**
     * Configure le jeu en mode solo contre l'IA. Initialise le jeu avec la difficulté de l'IA.
     */
    private void setupSoloGame() {
        // Configuration pour le mode solo contre IA
        String playerName = getIntent().getStringExtra("PLAYER_NAME");
        String difficulty = getIntent().getStringExtra("DIFFICULTY");

        viewModel.startGame(difficulty);
        viewModel.setPlayerNames(playerName, "IA");
    }

    /**
     * Configure le jeu pour le mode multijoueur local.
     */
    private void setupLocalMultiplayerGame() {
        // Configuration pour le multijoueur local
        String playerName1 = getIntent().getStringExtra("PLAYER_NAME_1");
        String playerName2 = getIntent().getStringExtra("PLAYER_NAME_2");

        viewModel.startGame(null); // Pas de difficulté spécifique pour le mode multijoueur local
        viewModel.setPlayerNames(playerName1, playerName2);
    }



    /**
     * Met à jour l'interface utilisateur pour refléter le mouvement de l'IA.
     * @param row La ligne de la cellule où l'IA a joué.
     * @param col La colonne de la cellule où l'IA a joué.
     */

    private void updateUIForAIMove(int row, int col) {
        Log.d("GameBoard", "Updating UI for AI move at row: " + row + ", col: " + col);
        runOnUiThread(() -> {
            char aiSymbol = 'O';
            updateCellImage(row, col, aiSymbol);
        });
    }



    /**
     * Configure les écouteurs de clic pour chaque cellule du plateau de jeu.
     */
    private void setupGameBoardClickListeners() {
        ImageView[] cells = new ImageView[9];
        cells[0] = findViewById(R.id.imageView1);
        cells[1] = findViewById(R.id.imageView2);
        cells[2] = findViewById(R.id.imageView3);
        cells[3] = findViewById(R.id.imageView4);
        cells[4] = findViewById(R.id.imageView5);
        cells[5] = findViewById(R.id.imageView6);
        cells[6] = findViewById(R.id.imageView7);
        cells[7] = findViewById(R.id.imageView8);
        cells[8] = findViewById(R.id.imageView9);



        for (int i = 0; i < cells.length; i++) {
            int finalI = i;
            cells[i].setOnClickListener(v -> onCellClicked(finalI / 3, finalI % 3));
        }
    }

    /**
     * Gère l'événement lorsqu'une cellule du plateau de jeu est cliquée.
     * @param row La ligne de la cellule cliquée.
     * @param col La colonne de la cellule cliquée.
     */
    private void onCellClicked(int row, int col) {
        try {
            Log.d("GameBoard", "Cell clicked at row: " + row + ", col: " + col);
            char currentPlayerSymbol = viewModel.getCurrentPlayerSymbol();
            boolean success = viewModel.playTurn(row, col);
            if (success) {
                updateCellImage(row, col, currentPlayerSymbol);
            }
        } catch (Exception e) {
            Log.e("GameBoard", "Error on cell click", e);
        }
    }



    /**
     * Met à jour l'image d'une cellule spécifique sur le plateau de jeu.
     * @param row La ligne de la cellule à mettre à jour.
     * @param col La colonne de la cellule à mettre à jour.
     * @param symbol Le symbole ('X' ou 'O') à afficher dans la cellule.
     */
    private void updateCellImage(int row, int col, char symbol) {
        ImageView cell = getCellImageView(row, col);
        if (symbol == 'X') {
            cell.setImageResource(R.drawable.ic_cross); // Remplacez par l'image X
        } else if (symbol == 'O') {
            cell.setImageResource(R.drawable.ic_circle); // Remplacez par l'image O
        }
    }


    /**
     * Récupère l'ImageView correspondant à une cellule spécifique du plateau de jeu.
     * @param row La ligne de la cellule.
     * @param col La colonne de la cellule.
     * @return L'ImageView correspondant à la cellule spécifiée.
     */
    private ImageView getCellImageView(int row, int col) {
        return (ImageView) findViewById(getResources().getIdentifier("imageView" + (row * 3 + col + 1), "id", getPackageName()));
    }


    /**
     * Affiche une boîte de dialogue à la fin du jeu offrant des options pour rejouer ou quitter.
     * @param title Le titre de la boîte de dialogue.
     * @param message Le message affiché dans la boîte de dialogue.
     */
    private void showEndGameDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("Rejouer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restartGameForNewRoundUI(); // Réinitialiser le jeu pour une nouvelle manche
            }
        });

        builder.setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retourner à l'écran précédent ou quitter l'application
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    /**
     * Réinitialise l'interface utilisateur du plateau de jeu à son état initial.
     */
    private void resetGameBoardUI() {
        // Réinitialiser les images des cellules à vide
        for (int i = 1; i <= 9; i++) {
            ImageView cell = (ImageView) findViewById(getResources().getIdentifier("imageView" + i, "id", getPackageName()));
            cell.setImageResource(0); // Réinitialiser l'image
        }
    }


    /**
     * Convertit une chaîne représentant la difficulté en son niveau correspondant.
     * @param difficulty Le niveau de difficulté sous forme de chaîne ("Facile", "Difficile", "Moyen").
     * @return Le niveau de difficulté correspondant sous forme d'entier.
     */
    private int convertDifficultyToLevel(String difficulty) {
        if ("Facile".equals(difficulty)) {
            return LocalAI.EASY;
        } else if ("Difficile".equals(difficulty)) {
            return LocalAI.HARD;
        } else if ("Moyen".equals(difficulty)) {
            return LocalAI.MEDIUM; // Ajout de la gestion du niveau Moyen
        } else {
            return LocalAI.EASY; // Default
        }
    }

    /**
     * Redémarre l'interface utilisateur pour une nouvelle manche du jeu.
     */
    private void restartGameForNewRoundUI() {
        viewModel.restartGameForNewRound();
        resetGameBoardUI();

    }




}




