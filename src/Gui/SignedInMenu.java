package Gui;

import file.ReadGames;
import file.ReadUsers;
import game.Game;
import game.GameState;
import game.GamesList;
import player.HumanPlayer;
import player.User;
import player.UserList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SignedInMenu extends JFrame {
    private String username;
    private String realName;
    private HumanPlayer humanPlayer;
    private GamesList gamesList;
    private Game currentGame;

    private JMenuBar MenuBarGame;
    private JMenu MenuGameBar;
    private JMenuItem MenuItemNewGame;
    private JMenuItem MenuItemLoadGame;
    private JPanel PanelSignedInMenu;
    private JLabel Title;
    private JButton SinglePlayerButton;
    private JButton TwoPlayersButton;
    private JButton MenuButton;
    private JLabel Background;

    // constructor
    public SignedInMenu(String username, String realName) {
        this.username = username;
        this.realName = realName;
        buildUI();
        loadUsersAndGames();

        Title.setText("Welcome, " + username + " (" + realName + ")");
        Title.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 60));

        setVisible(true);
        setSize(1280, 860);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Title.setText("Welcome, " + username);
        Title.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 80));
    }

    // load users and games info
    private void loadUsersAndGames() {
        // load all users into a UserList
        ReadUsers readUsers = new ReadUsers();
        UserList users = new UserList();
        for (String line : readUsers.load()) {
            String[] p = line.split(" ", 2);
            if (p.length == 2) {
                users.addUser(new HumanPlayer(p[0], p[1]));
            }
        }
        // find the matching HumanPlayer by id
        for (User humanPlr : users.getUsers()) {
            if (humanPlr.getId().equals(username)) {
                humanPlayer = (HumanPlayer) humanPlr;
                realName    = humanPlr.getName();
                break;
            }
        }

        // load saved games
        ReadGames readGames = new ReadGames();
        gamesList = new GamesList();
        for (GameState gs : readGames.load()) {
            gamesList.add(gs);
        }
    }

    // go to first menu
    private void Menu(ActionEvent e) {
        new FirstMenu();
        dispose();
    }

    private void SinglePlayerButton(ActionEvent e) {
        String mode = "SINGLE";

        // instead of for loop, looking for an existing single game for this user
        GameState found = gamesList.getAll().stream()
                .filter(gs -> gs.getUsername().equals(username) && gs.getMode().equals(mode))
                .findFirst()
                .orElse(null);

        if (found != null) {
            // if user has load game, ask if wants to continue
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "You have a saved Single Player game.\nDo you want to continue?",
                    "Continue Saved Game?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                resumeGame(found);
                return;
            }
        }

        // start a new single player game
        startNewGame(mode);
    }

    private void newGameMenuItem(ActionEvent e) {
        String[] modes = { "Single Player", "Two Players" };
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose mode for NEW game:",
                "New Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                modes,
                modes[0]
        );
        if (choice < 0) return;
        String mode = choice == 0 ? "SINGLE" : "TWO";

        // check if user has loaded game
        GameState found = gamesList.getAll().stream()
                .filter(gs -> gs.getUsername().equals(username) && gs.getMode().equals(mode))
                .findFirst().orElse(null);

        if (found != null) {
            int c = JOptionPane.showConfirmDialog(
                    this,
                    "You have a saved " + modes[choice] + " game.\nDo you want to continue?",
                    "Continue Saved Game?",
                    JOptionPane.YES_NO_OPTION
            );
            if (c == JOptionPane.YES_OPTION) {
                resumeGame(found);
                return;
            }
        }
        // start new game
        startNewGame(mode);
    }

    // resuming game
    private void resumeGame(GameState state) {
        currentGame = new Game(humanPlayer, state.getMode(), gamesList);
        currentGame.loadState(state);
        goToCardGame();
    }

    // starting new game
    private void startNewGame(String mode) {
        currentGame = new Game(humanPlayer, mode, gamesList);
        goToCardGame();
    }

    // go to play card game
    private void goToCardGame() {
        new CardGame(currentGame);
        dispose();
    }

    private void LoadGameMenuItem(ActionEvent e) {
        String[] modes = { "Single Player", "Two Players" };
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose mode to LOAD:",
                "Load Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                modes,
                modes[0]
        );
        if (choice < 0) return;
        String mode = choice == 0 ? "SINGLE" : "TWO";

        GameState found = gamesList.getAll().stream()
                .filter(gs -> gs.getUsername().equals(username) && gs.getMode().equals(mode))
                .findFirst().orElse(null);

        if (found == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No saved " + modes[choice] + " game found.\nPlease start a New Game first.",
                    "Not Found",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // if game found, resume it
        resumeGame(found);
    }

    private void TwoPlayersButton(ActionEvent e) {
        String mode = "TWO";

        // instead of for loop, looking for an existing two players game for this user
        GameState found = gamesList.getAll().stream()
                .filter(gs -> gs.getUsername().equals(username) && gs.getMode().equals(mode))
                .findFirst()
                .orElse(null);

        if (found != null) {
            // Ask if user wants to resume
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "You have a saved Two Players game.\nDo you want to continue?",
                    "Continue Saved Game?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                resumeGame(found);
                return;
            }
        }
        // start a new two players game
        startNewGame(mode);
    }


    private void buildUI() {
        MenuBarGame = new JMenuBar();
        MenuGameBar = new JMenu();
        MenuItemNewGame = new JMenuItem();
        MenuItemLoadGame = new JMenuItem();
        PanelSignedInMenu = new JPanel();
        Title = new JLabel();
        SinglePlayerButton = new JButton();
        TwoPlayersButton = new JButton();
        MenuButton = new JButton();
        Background = new JLabel();

        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //*** MenuBarGame ***
        MenuGameBar.setText("Game");
        //*** MenuItemNewGame ***
        MenuItemNewGame.setText("New Game");
        MenuItemNewGame.addActionListener(e -> newGameMenuItem(e));
        MenuGameBar.add(MenuItemNewGame);
        //*** MenuItemLoadGame ***
        MenuItemLoadGame.setText("Load Game");
        MenuItemLoadGame.addActionListener(e -> LoadGameMenuItem(e));
        MenuGameBar.add(MenuItemLoadGame);
        MenuBarGame.add(MenuGameBar);
        setJMenuBar(MenuBarGame);

        //*** PanelSignedInMenu ***
        PanelSignedInMenu.setLayout(null);

        //*** Title ***
        Title.setText("GAME MENU");
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        Title.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 123));
        PanelSignedInMenu.add(Title);
        Title.setBounds(225, 165, 890, 125);

        //*** SinglePlayerButton ***
        SinglePlayerButton.setText("Single Player");
        SinglePlayerButton.setFont(new Font("Lucida Handwriting", Font.PLAIN, 45));
        SinglePlayerButton.setBackground(new Color(0xb28769));
        SinglePlayerButton.setBorderPainted(false);
        SinglePlayerButton.addActionListener(e -> {SinglePlayerButton(e);});
        PanelSignedInMenu.add(SinglePlayerButton);
        SinglePlayerButton.setBounds(465, 430, 415, 70);

        //***TwoPlayersButton ***
        TwoPlayersButton.setText("Two Players");
        TwoPlayersButton.setFont(new Font("Lucida Handwriting", Font.PLAIN, 45));
        TwoPlayersButton.setBackground(new Color(0xb28769));
        TwoPlayersButton.setBorderPainted(false);
        TwoPlayersButton.addActionListener(e -> TwoPlayersButton(e));
        PanelSignedInMenu.add(TwoPlayersButton);
        TwoPlayersButton.setBounds(465, 535, 415, 70);

        //*** MenuButton ***
        MenuButton.setText("Menu");
        MenuButton.setFont(new Font("Lucida Handwriting", Font.PLAIN, 45));
        MenuButton.setBackground(new Color(0xb28769));
        MenuButton.setBorderPainted(false);
        MenuButton.addActionListener(e -> Menu(e));
        PanelSignedInMenu.add(MenuButton);
        MenuButton.setBounds(465, 635, 415, 70);

        //***Background ***
        Background.setIcon(new ImageIcon(getClass().getResource("/OtherImages/background.png")));
        PanelSignedInMenu.add(Background);
        Background.setBounds(0, 30, 1280, 810);

        contentPane.add(PanelSignedInMenu);
        PanelSignedInMenu.setBounds(0, -30, 1280, 840);
    }
}
