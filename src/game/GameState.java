package game;

import deck.Card;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private String username;
    private String mode;
    private List<Card> playerDeck;
    private List<Card> opponentDeck;

    public GameState(String username, String mode) {
        this.username = username;
        this.mode = mode;

        this.playerDeck   = new ArrayList<>();
        this.opponentDeck = new ArrayList<>();
    }


    public String getUsername() { return username; }
    public String getMode()     { return mode; }
    public List<Card> getPlayerDeck()   { return playerDeck; }
    public List<Card> getOpponentDeck() { return opponentDeck; }

    // saving the progress deck of games.
    public void setPlayerDeck(List<Card> deck) { this.playerDeck = deck; }
    public void setOpponentDeck(List<Card> deck) { this.opponentDeck = deck; }
}