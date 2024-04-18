package com.example.morpion.ai;

import com.example.morpion.data.Game;
import com.example.morpion.ui.Board.GameBoard;

import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class OpenAi {

    private final String apiUrl = "https://api.example.com/getmove"; // Remplacez par l'URL de votre API

    /**
     * Interroge l'API d'intelligence artificielle externe pour obtenir le meilleur mouvement.
     *
     * @param game L'état actuel du plateau de jeu.
     * @return La réponse de l'API externe sous forme de chaîne de caractères.
     */
    public String getMoveFromAPI(Game game) {
        JSONObject json = convertGameBoardToJson(game);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (java.io.OutputStream os = connection.getOutputStream()) {
                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name())) {
                return scanner.useDelimiter("\\A").next();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convertit l'état du jeu en une représentation JSON.
     *
     * @param game L'état du jeu.
     * @return Représentation JSON de l'état du jeu.
     */
    private JSONObject convertGameBoardToJson(Game game) {
        JSONObject json = new JSONObject();
        // Ajoutez ici la logique pour convertir gameBoard en JSON
        return json;
    }

    /**
     * Analyse la réponse reçue de l'API d'intelligence artificielle externe et la convertit en un mouvement compréhensible par le jeu.
     *
     * @param response La réponse brute en chaîne de caractères reçue de l'API externe.
     * @return Un tableau d'entiers représentant les coordonnées du mouvement suggéré par l'API.
     */
    public int[] parseResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            int x = jsonResponse.getInt("x");
            int y = jsonResponse.getInt("y");
            return new int[]{x, y};
        } catch (Exception e) {
            e.printStackTrace();
            return new int[]{-1, -1};
        }
    }
}
