package file;

import game.GameState;
import game.GamesList;
import deck.Card;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveGame {
    private String fileName = "savegame.txt";

    // getting all gamestates inside gamelist and change it to the line by line than save them in a txt file
    public void save(GamesList gamesList) {
        File file = new File(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (GameState game : gamesList.getAll()) {

                // player deck string
                StringBuilder playerDeck = new StringBuilder();
                for (Card card : game.getPlayerDeck()) {
                    if (playerDeck.length() > 0) playerDeck.append(";");
                    playerDeck.append(card.getRank()).append("-").append(card.getSuit());
                }
                // opponent deck string
                StringBuilder opponentDeck = new StringBuilder();
                for (Card card : game.getOpponentDeck()) {
                    if (opponentDeck.length() > 0) opponentDeck.append(";");
                    opponentDeck.append(card.getRank()).append("-").append(card.getSuit());
                }
                // write 1 line
                writer.write(game.getUsername()
                        + " " + game.getMode()
                        + " " + playerDeck.toString()
                        + " " + opponentDeck.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}