package com.example.morpion.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.morpion.R;

public class GameMode extends AppCompatActivity {


    /**
     * Initialise l'activité avec le layout correspondant et configure les écouteurs d'événements pour les boutons.
     * @param savedInstanceState Si l'activité est réinitialisée après avoir été précédemment fermée
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);

        // Configuration des écouteurs pour les boutons qui permettent de naviguer vers les différentes configurations de jeu
        findViewById(R.id.solo_mode).setOnClickListener(v -> navigateToGameSetting("SOLO"));
        findViewById(R.id.multiplayer2).setOnClickListener(v -> navigateToGameSetting("MULTIPLAYER"));
        findViewById(R.id.rules).setOnClickListener(v -> navigateToGameSetting("RULES"));
    }

    /**
     * Navigue vers l'activité appropriée en fonction du mode de jeu sélectionné.
     * @param modeType Le type de mode de jeu sélectionné ('SOLO', 'MULTIPLAYER', ou 'RULES').
     * - 'SOLO' et 'MULTIPLAYER' naviguent vers GameSetting avec un extra spécifiant le mode.
     * - 'RULES' navigue directement vers l'activité Rules.
     */
    private void navigateToGameSetting(String modeType) {
        if ("RULES".equals(modeType)) {
            Intent intent = new Intent(GameMode.this, Rules.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(GameMode.this, GameSetting.class);
            intent.putExtra("MODE_TYPE", modeType);
            startActivity(intent);
        }
    }









}