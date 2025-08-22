package file;

import game.GameState;
import deck.Card;
import java.io.*;
import java.util.*;

public class ReadGames {
    private String fileName="savegame.txt";
    public List<GameState> load() {
        List<GameState> list = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) {
            // if there is no file, return empty list
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {

                // saving like that: username mode 2-Clubs;5-Hearts;... 3-Spades;...
                String[] parts = line.split(" ", 4);
                if (parts.length < 4) continue;
                GameState gameState = new GameState(parts[0], parts[1]);

                // player deck
                if (!parts[2].isEmpty()) {
                    for (String cardStr : parts[2].split(";")) {
                        String[] cardParts = cardStr.split("-");
                        String rankStr = cardParts[0];
                        String suitStr = cardParts[1];
                        gameState.getPlayerDeck().add(new Card(suitStr, Integer.parseInt(rankStr)));
                    }
                }
                // opponent deck
                if (!parts[3].isEmpty()) {
                    for (String cardStr : parts[3].split(";")) {
                        String[] cardParts = cardStr.split("-");
                        String rankStr = cardParts[0];
                        String suitStr = cardParts[1];
                        gameState.getOpponentDeck().add(new Card(suitStr, Integer.parseInt(rankStr)));
                    }
                }
                list.add(gameState);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}