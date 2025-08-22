package game;

import java.util.ArrayList;
import java.util.List;

public class GamesList {
    private List<GameState> games;

    public GamesList() {
        games = new ArrayList<>();
    }

    // 1) If same username and mod have a loaded progress, delete them or dont do anything
    public void add(GameState state) {
        for (int i = 0; i < games.size(); i++) {
            GameState s = games.get(i);
            if (s.getUsername().equals(state.getUsername()) && s.getMode().equals(state.getMode())) {
                games.remove(i);
                break;
            }
        }
        // add the new progress to the list
        games.add(state);
    }

    // getting the gaamestates in the games list
    public List<GameState> getAll() {
        return new ArrayList<>(games);
    }
}