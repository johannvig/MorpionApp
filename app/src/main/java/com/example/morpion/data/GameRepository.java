package com.example.morpion.data;
import android.util.Log;


public class GameRepository {

    private final Game gameBoard;
    private final ScoreManager scoreManager;

    /**
     * Constructeur de la classe GameRepository.
     * @param gameBoard Le plateau de jeu utilisé dans le dépôt.
     */
    public GameRepository(Game gameBoard) {
        this.gameBoard = gameBoard;
        this.scoreManager = new ScoreManager();
    }

    /**
     * Initialise le plateau de jeu avec une taille spécifiée.
     * @param size La taille du plateau de jeu.
     */
    public void initializeGameBoard(int size) {
        gameBoard.initializeBoard(size);

    }



    /**
     * Effectue un mouvement sur le plateau de jeu.
     * @param x La position X du mouvement.
     * @param y La position Y du mouvement.
     * @param symbol Le symbole du joueur effectuant le mouvement.
     * @return true si le mouvement a été réalisé avec succès, false sinon.
     */
    public boolean makeMove(int x, int y, char symbol) {
        boolean moveMade = gameBoard.updateBoard(x, y, symbol);
        Log.e("GameRepository", String.valueOf(moveMade));
        if (moveMade) {
            if (gameBoard.checkForWin()) {
                // Ici, vous devez identifier le joueur à incrémenter en fonction du symbole
                int player = symbol == 'X' ? 0 : 1;
                scoreManager.incrementScore(player);
            }
        }
        return moveMade;
    }


    /**
     * Vérifie si un joueur a gagné la partie.
     * @return true si un joueur a gagné, false sinon.
     */
    public boolean checkForWin() {

        return gameBoard.checkForWin();

    }

    /**
     * Vérifie si le plateau de jeu est complet.
     * @return true si le plateau est complet, false sinon.
     */
    public boolean isBoardFull() {
        return gameBoard.isBoardFull();
    }

    /**
     * Incrémente le score d'un joueur.
     * @param playerSymbol Le symbole du joueur (utilisé pour identifier le joueur).
     */
    public void incrementScore(int playerSymbol) {
        scoreManager.incrementScore(playerSymbol);
    }

    /**
     * Obtient le score d'un joueur.
     * @param playerSymbol Le symbole du joueur.
     * @return Le score du joueur.
     */
    public int getScore(int playerSymbol) {
        return scoreManager.getScore(playerSymbol);
    }



    /**
     * Renvoie le plateau de jeu.
     * @return L'objet Game représentant le plateau de jeu.
     */
    public Game getGameBoard() {
        return gameBoard;
    }






}
