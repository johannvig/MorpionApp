package com.example.morpion.data;

import android.util.Log;


public class Game {
    private char[][] board;
    private static final char EMPTY = ' ';

    private String gameState;


    /**
     * Retourne le plateau de jeu actuel.
     * @return Le plateau de jeu sous forme de tableau bidimensionnel de caractères où chaque cellule représente un état de case du jeu.
     */
    public char[][] getBoard() {
        return board;
    }



    /**
     * Constructeur pour créer un nouveau plateau de jeu de la taille spécifiée.
     * @param size La taille du plateau (nombre de lignes et de colonnes).
     */
    public Game(int size) {
        board = new char[size][size];
        initializeBoard(size);
    }


    /**
     * Initialise le plateau de jeu en remplissant chaque case avec un caractère vide.
     * @param size La taille du plateau de jeu.
     */
    public void initializeBoard(int size) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    /**
     * Met à jour le plateau de jeu à la position spécifiée avec le symbole du joueur.
     * @param x L'indice de la ligne.
     * @param y L'indice de la colonne.
     * @param symbol Le symbole du joueur ('X' ou 'O').
     * @return true si la mise à jour a été effectuée, false sinon.
     */
    public boolean updateBoard(int x, int y, char symbol) {
        if (board[x][y] == EMPTY) {
            board[x][y] = symbol;
            Log.e("Game", "return true");
            return true;
        }
        Log.e("Game", "return false");
        return false;
    }



    public void setGameState(String gameState) {
        this.gameState = gameState;
    }




    /**
     * Vérifie si un joueur a gagné la partie.
     * @return true si un joueur a remporté la partie, false sinon.
     */
    public boolean checkForWin() {
        // Les conditions de victoire pour un jeu de morpion standard
        for (int i = 0; i < 3; i++) {
            // Vérifie les lignes
            if (board[i][0] != EMPTY && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                return true;
            }

            // Vérifie les colonnes
            if (board[0][i] != EMPTY && board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
                return true;
            }
        }

        // Vérifie les diagonales
        if (board[0][0] != EMPTY && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            return true;
        }
        if (board[0][2] != EMPTY && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            return true;
        }

        // Aucune condition de victoire n'est remplie
        return false;
    }

    /**
     * Vérifie si le plateau de jeu est complet.
     * @return true si toutes les cases sont remplies, false sinon.
     */
    public boolean isBoardFull() {
        for (char[] row : board) {
            for (int cell : row) {
                if (cell == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Retourne la taille d'une dimension du plateau de jeu.
     * @return La taille d'une dimension du plateau.
     */

    public int getSize() {
        return board.length;
    }

    /**
     * Réinitialise le plateau de jeu à son état initial.
     * Elle utilise `initializeBoard` pour configurer le plateau avec la taille actuelle.
     */

    public void resetBoard() {
        initializeBoard(getSize());
    }




}
