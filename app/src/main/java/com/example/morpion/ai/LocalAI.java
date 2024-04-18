package com.example.morpion.ai;

import android.util.Log;

import com.example.morpion.data.Game;

import java.util.Random;

public class LocalAI {

    private static final char AI_SYMBOL = 'O'; // Symbole pour l'IA

    private final Random random = new Random();

    // Choix du mouvement par l'IA
    public static final int EASY = 0;
    public static final int HARD = 1;

    public static final int MEDIUM = 2;

    /**
     * Choisit un mouvement pour l'IA en fonction du niveau de difficulté.
     * @param gameBoard Le plateau de jeu actuel.
     * @param difficultyLevel Le niveau de difficulté (FACILE, MOYEN, DIFFICILE).
     * @return Un tableau contenant les coordonnées du mouvement choisi.
     */
    public int[] chooseMove(Game gameBoard, int difficultyLevel) {
        char[][] board = gameBoard.getBoard(); // Assurez-vous que vous avez une méthode dans Game pour obtenir le plateau sous forme de tableau 2D

        switch (difficultyLevel) {
            case EASY:
                return randomMove(board);
            case MEDIUM: // Utilisez l'algorithme Minimax pour le niveau moyen
                return bestMove(board);
            default:
                return randomMove(board);
        }
    }

    /**
     * Effectue un mouvement aléatoire sur le plateau.
     * @param board Le plateau de jeu.
     * @return Un tableau contenant les coordonnées du mouvement choisi.
     */
    private int[] randomMove(char[][] board) {
        int x, y;
        do {
            x = random.nextInt(board.length);
            y = random.nextInt(board[0].length);
        } while (board[x][y] != ' ');
        return new int[]{x, y};
    }


    /**
     * Détermine le meilleur mouvement en utilisant l'algorithme Minimax.
     * @param board Le plateau de jeu.
     * @return Un tableau contenant les coordonnées du meilleur mouvement.
     */
    private int[] bestMove(char[][] board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] == ' ') {
                    board[x][y] = AI_SYMBOL;
                    int score = minimax(board, 0, false);
                    board[x][y] = ' '; // annuler le coup
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = x;
                        bestMove[1] = y;
                    }
                }
            }
        }

        return bestMove;
    }

    /**
     * Implémentation de l'algorithme Minimax pour l'IA.
     * @param board Le plateau de jeu.
     * @param depth La profondeur actuelle dans l'arbre de jeu.
     * @param isMaximizing Booléen indiquant si l'IA maximise ou minimise le score.
     * @return Le score calculé pour le plateau de jeu donné.
     */
    private int minimax(char[][] board, int depth, boolean isMaximizing) {
        char winner = checkWinner(board);
        if (winner != '\0') {
            return winner == 'O' ? 10 : winner == 'X' ? -10 : 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'O';
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = ' ';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'X';
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = ' ';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    /**
     * Vérifie le gagnant sur le plateau de jeu actuel.
     * @param board Le plateau de jeu.
     * @return Le symbole du gagnant ('X', 'O' ou 'D' pour match nul), ou '\0' si aucun gagnant.
     */
    private char checkWinner(char[][] board) {
        // Vérifier les lignes
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                if (board[i][0] != ' ') {
                    return board[i][0];
                }
            }
        }

        // Vérifier les colonnes
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                if (board[0][j] != ' ') {
                    return board[0][j];
                }
            }
        }

        // Vérifier les diagonales
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] ||
                board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[1][1] != ' ') {
                return board[1][1];
            }
        }

        // Vérifier si toutes les cases sont remplies pour un match nul
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return '\0'; // Le jeu continue
                }
            }
        }

        return 'D'; // Match nul
    }

}

