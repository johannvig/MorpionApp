package com.example.morpion.data;

import android.util.Log;

public class ScoreManager {

    private int[] scores;

    /**
     * Constructeur de ScoreManager qui initialise les scores pour deux joueurs.
     */
    public ScoreManager() {
        scores = new int[2]; // Initialiser le tableau pour 2 joueurs
        resetScores(); // Réinitialiser les scores au démarrage
    }

    /**
     * Incrémente le score du joueur spécifié.
     *
     * @param player Le joueur dont le score doit être incrémenté.
     */

    public void incrementScore(int player) {
        if (player >= 0 && player < scores.length) {
            scores[player]++;
            Log.d("ScoreManager", "Score incremented for player: " + player + ". New score: " + scores[player]);
        }
    }


    /**
     * Obtient le score actuel du joueur spécifié.
     *
     * @param player Le joueur dont on souhaite obtenir le score.
     * @return Le score actuel du joueur.
     */
    public int getScore(int player) {
        if (player >= 0 && player < scores.length) {
            return scores[player];
        }
        return 0; // Retourner 0 si le joueur n'est pas valide
    }

    /**
     * Réinitialise les scores de tous les joueurs à zéro.
     */
    public void resetScores() {
        for (int i = 0; i < scores.length; i++) {
            scores[i] = 0;
        }
    }
}
