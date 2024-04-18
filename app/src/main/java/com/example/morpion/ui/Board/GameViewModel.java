package com.example.morpion.ui.Board;



import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.morpion.ai.LocalAI;
import com.example.morpion.ai.OpenAi;
import com.example.morpion.data.Game;
import com.example.morpion.data.GameRepository;


public class GameViewModel extends ViewModel {

    private final MutableLiveData<Game> gameLiveData = new MutableLiveData<>();
    private final MutableLiveData<String[]> playerNames = new MutableLiveData<>();
    private final MutableLiveData<String> gameStateLiveData = new MutableLiveData<>();

    private final MutableLiveData<Integer> playerOneScoreLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> playerTwoScoreLiveData = new MutableLiveData<>();

    private MutableLiveData<int[]> aiMoveLiveData = new MutableLiveData<>();

    private OpenAi openAi = new OpenAi();

    private GameRepository gameRepository;
    private String currentPlayer;
    private boolean isGameEnded;

    private final char playerOneSymbol = 'X';
    private final char playerTwoSymbol = 'O';

    private String playerOneName = "Player 1";
    private String playerTwoName = "Player 2";
    private String difficulty;

    private String startingPlayer;

    private LocalAI aiPlayer;

    private final MutableLiveData<String> currentTurnText = new MutableLiveData<>();


    /**
     * Récupère les LiveData représentant les mouvements de l'IA.
     * @return LiveData contenant les coordonnées du mouvement de l'IA.
     */
    public LiveData<int[]> getAiMoveLiveData() {
        return aiMoveLiveData;
    }

    /**
     * Récupère les LiveData représentant l'état actuel du jeu.
     * @return LiveData contenant l'état du jeu.
     */
    public LiveData<Game> getGameLiveData() {
        return gameLiveData;
    }

    /**
     * Récupère les LiveData représentant l'état actuel du jeu (par exemple, "Jeu terminé").
     * @return LiveData contenant l'état du jeu sous forme de chaîne de caractères.
     */
    public LiveData<String> getGameStateLiveData() {
        return gameStateLiveData;
    }

    /**
     * Récupère les LiveData contenant les noms des joueurs.
     * @return LiveData contenant un tableau des noms des joueurs.
     */
    public LiveData<String[]> getPlayerNames() {
        return playerNames;
    }

    /**
     * Constructeur qui initialise le jeu et configure les joueurs.
     */

    public GameViewModel() {
        gameRepository = new GameRepository(new Game(3));
        aiPlayer = new LocalAI();
        startGame(null);
        updateScores();
    }

    /**
     * Démarre une nouvelle partie avec une difficulté spécifiée.
     * @param difficulty La difficulté de la partie (peut être null).
     */
    void startGame(String difficulty) {
        this.difficulty = difficulty;
        gameRepository.initializeGameBoard(3); // Réinitialise le plateau de jeu
        isGameEnded = false; // Réinitialise l'état de fin de jeu


        updateGameState("Nouveau jeu commencé");
        updateGameBoard(); // Met à jour le plateau de jeu
        updateScores(); // Met à jour les scores
        playerNames.setValue(new String[]{playerOneName, playerTwoName}); // Met à jour les noms des joueurs


    }

    /**
     * Met à jour les scores des joueurs.
     */
    private void updateScores() {
        playerOneScoreLiveData.setValue(gameRepository.getScore(0)); // Met à jour le score du joueur 1
        playerTwoScoreLiveData.setValue(gameRepository.getScore(1)); // Met à jour le score du joueur 2
    }

    /**
     * Récupère les LiveData représentant le score du joueur 1.
     * @return LiveData contenant le score du joueur 1.
     */
    public LiveData<Integer> getPlayerOneScore() {
        return playerOneScoreLiveData;
    }

    /**
     * Récupère les LiveData représentant le score du joueur 2.
     * @return LiveData contenant le score du joueur 2.
     */
    public LiveData<Integer> getPlayerTwoScore() {
        return playerTwoScoreLiveData;
    }


    /**
     * Termine la partie en cours.
     */
    void endGame() {
        isGameEnded = true;
        Game currentGame = gameRepository.getGameBoard();
        currentGame.setGameState("finished");



        updateLiveDataState("finished"); // Mettre à jour le LiveData avec l'état "finished"


        Log.e("GameViewModel", "Next game starting player: " + startingPlayer);
    }


    /**
     * Change le joueur actuel.
     */
    void switchPlayer() {
        currentPlayer = currentPlayer.equals("Player 1") ? "Player 2" : "Player 1";
        String currentTurnName = currentPlayer.equals("Player 1") ? playerOneName : playerTwoName;
        currentTurnText.setValue("Au tour de " + currentTurnName);
        updateGameState(currentPlayer + "'s Turn");
        Log.e("GameViewModel", currentPlayer);
    }

    /**
     * Joue un tour à la position spécifiée.
     * @param x La coordonnée x sur le plateau.
     * @param y La coordonnée y sur le plateau.
     */
    public boolean playTurn(int x, int y) {

            if (!isGameEnded) {
                Log.d("GameViewModel", "Current Player: " + currentPlayer);
                Log.d("GameViewModel", "Is Solo Mode: " + isSoloMode());
                Log.d("GameViewModel", "Is Player 2's Turn: " + currentPlayer.equals("Player 2"));

                char currentSymbol = getCurrentPlayerSymbol();
                boolean success = gameRepository.makeMove(x, y, currentSymbol);
                if (success) {
                    updateGameBoard();
                    if (gameRepository.checkForWin()) {
                        updateGameState(currentPlayer + " Won!");
                        endGame();

                    } else if (gameRepository.isBoardFull()) {
                        Log.e("GameViewModel","full");
                        updateGameState("Draw!");
                        endGame();
                    } else {
                        switchPlayer();
                        // Si en mode solo et que c'est le tour de l'IA
                        if (isSoloMode() && currentPlayer.equals("Player 2")) {
                            playAITurn();
                        }
                    }
                    updateScores();
                    return true;
                }}
        return false;
    }




    /**
     * Vérifie si le mode de jeu est en solo.
     * @return true si le mode de jeu est solo, false sinon.
     */
    private boolean isSoloMode() {
        // Implémentez la logique pour déterminer si le mode de jeu est solo
        // Par exemple, vérifiez si le nom du deuxième joueur est "IA"
        Log.e("GameViewModel","ok1.");
        return playerTwoName.equals("IA");
    }



    /**
     * Joue un tour pour l'IA.
     */

    void playAITurn() {
        Log.e("GameViewModel", "AI");
        if (isSoloMode() && "Player 2".equals(currentPlayer)) {
            Log.e("GameViewModel", "AI think");
            int difficultyLevel = convertDifficultyToLevel(this.difficulty);
            int[] aiMove;

            if ("Difficile".equals(this.difficulty)) {
                // Utiliser l'API externe pour obtenir le mouvement
                String apiResponse = openAi.getMoveFromAPI(gameRepository.getGameBoard());
                aiMove = openAi.parseResponse(apiResponse);
            } else {
                // Utiliser l'IA locale pour obtenir le mouvement
                aiMove = aiPlayer.chooseMove(gameRepository.getGameBoard(), difficultyLevel);
            }

            if (aiMove != null && aiMove.length == 2) {
                boolean success = gameRepository.makeMove(aiMove[0], aiMove[1], playerTwoSymbol);
                if (success) {
                    aiMoveLiveData.postValue(aiMove); // Utilisez postValue pour mettre à jour sur le thread de l'UI
                    Log.e("GameViewModel", "Play AI move");

                    updateGameBoard();
                    if (gameRepository.checkForWin()) {
                        updateGameState(currentPlayer + " Won!");
                        endGame();
                        updateScores();
                    } else if (gameRepository.isBoardFull()) {
                        Log.e("GameViewModel", "full");
                        updateGameState("Draw!");
                        endGame();
                    } else {
                        switchPlayer();
                    }
                }
            }
        }
    }



    /**
     * Convertit la chaîne de difficulté en niveau de difficulté.
     * @param difficulty La chaîne représentant la difficulté.
     * @return Le niveau de difficulté correspondant.
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
     * Met à jour l'état du jeu.
     * @param state La nouvelle état du jeu.
     */
    private void updateGameState(String state) {
        Log.e("GameViewModel","up1");
        gameStateLiveData.setValue(state);
    }

    /**
     * Met à jour le plateau de jeu dans le LiveData.
     */
    private void updateGameBoard() {
        gameLiveData.setValue(gameRepository.getGameBoard());
    }

    /**
     * Récupère le symbole du joueur actuel.
     * @return Le symbole du joueur actuel ('X' ou 'O').
     */
    char getCurrentPlayerSymbol() {
        if (currentPlayer == null) {
            currentPlayer = "Player 1";
        }
        return currentPlayer.equals("Player 1") ? playerOneSymbol : playerTwoSymbol;
    }


    /**
     * Définit les noms des joueurs et met à jour les LiveData correspondants.
     * @param playerOneName Le nom du joueur 1.
     * @param playerTwoName Le nom du joueur 2.
     */
    public void setPlayerNames(String playerOneName, String playerTwoName) {
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        currentTurnText.setValue("Au tour de " + playerOneName);
        playerNames.setValue(new String[]{playerOneName, playerTwoName});
    }

    /**
     * Récupère les LiveData représentant le tour actuel.
     * @return LiveData contenant le texte du tour actuel.
     */
    public LiveData<String> getCurrentTurn() {
        return currentTurnText;
    }


    /**
     * Définit le joueur qui commence la partie.
     * Si le joueur 2 est l'IA, déclenche son tour.
     * Sinon, le joueur humain commence.
     * @param startingPlayer Le joueur qui commence (Player 1 ou Player 2).
     */
    public void setStartingPlayer(String startingPlayer) {
        this.startingPlayer = startingPlayer;
        currentPlayer = startingPlayer;
        updateCurrentTurnText();

        if (isSoloMode() && "Player 2".equals(startingPlayer)) {
            playAITurn(); // Assurez-vous que l'IA joue si elle doit commencer

        }
    }


    /**
     * Met à jour le texte indiquant quel joueur doit jouer.
     */
    private void updateCurrentTurnText() {
        currentTurnText.setValue("Au tour de " + currentPlayer);
    }


    /**
     * Réinitialise le jeu pour un nouveau round, met à jour le plateau et l'état du jeu.
     */

    public void restartGameForNewRound() {
        gameRepository.getGameBoard().resetBoard();
        updateGameBoard();
        updateGameState("Nouveau jeu commencé");
        isGameEnded = false;
        setStartingPlayer(currentPlayer); // Assurez-vous que le bon joueur commence
        updateCurrentTurnText();

    }


    /**
     * Met à jour le LiveData avec le nouvel état du jeu.
     * @param state Le nouvel état à publier.
     */

    private void updateLiveDataState(String state) {
        Log.e("GameViewModel","up2");
        gameStateLiveData.setValue(state);
    }



}

