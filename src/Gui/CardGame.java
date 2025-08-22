package Gui;

import deck.Card;
import file.SaveGame;
import game.Game;
import game.GamesList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class CardGame extends JFrame {
    private Game game;
    private GamesList gamesList;

    private boolean awaitingOpponent = false;
    private Card lastPlayerCard;
    private boolean warActive = false;
    private List<Card> warPile = new ArrayList<>();

    private JMenuBar MenuBarGame;
    private JMenu MenuGame;
    private JMenuItem MenuItemSaveGame;
    private JPanel GameTablePanel;
    private JPanel PlayerPanel;
    private JLabel PCardBack;
    private JLabel PTitle;
    private JLabel PCardDraw;
    private JLabel PTotalCard;
    private JButton PDraw;
    private JPanel GuestPanel;
    private JLabel GCardBack;
    private JLabel GTitle;
    private JLabel GCardDraw;
    private JLabel GTotalCard;
    private JButton GDraw;
    private JPanel WarPanel;
    private JLabel PWarBack1;
    private JLabel GWarBack1;
    private JLabel GWarBack2;
    private JLabel PWarBack2;
    private JLabel GWarDraw;
    private JLabel PWarDraw;
    private JPanel TitlePanel;
    private JLabel GameTitle;
    private JPanel SubTitlePanel;
    private JLabel WhoWins;
    private JLabel GameCardBackground;

    // Constructor
    public CardGame(Game game) {
        this.game = game;
        this.gamesList = game.getGamesList();
        buildUI();

        // if game mode is single, set not visible Guest Draw card button
        if (game.isSingleMode()) {
            GDraw.setVisible(false);
        }

        WarPanel.setVisible(false);

        setVisible(true);
        setSize(1280, 860);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    // displaying and updating outcome in the gui
    private void displayOutcome(Card p, Card o) {
        Game.Outcome roundResult = game.evaluateRound(p, o);
        switch (roundResult) {
            case PLAYER_WIN:
                WhoWins.setText(game.getUsername() + " wins!");
                break;
            case OPPONENT_WIN:
                WhoWins.setText(game.isSingleMode() ? "Computer wins!" : "Player 2 wins!");
                break;
            case TIE:
                WhoWins.setText("Tie!");
                WarPanel.setVisible(true);
                GameTitle.setText("WAR BEGINS");
                return;
        }
        WarPanel.setVisible(false);
        GameTitle.setText("BATTLE!");
        PTotalCard.setText("TOTAL: " + game.getPlayerDeckSize());
        GTotalCard.setText("TOTAL: " + game.getOpponentDeckSize());
    }

    // loading card icons from the cards folder with for loop easily
    private ImageIcon loadCardIcon(Card c) {
        String file = String.format("/cards/%d_of_%s.png",
                c.getRank(), c.getSuit().toLowerCase());
        return new ImageIcon(getClass().getResource(file));
    }

    // player drawing button.
    private void PDraw(ActionEvent e) {
        // if the war active means continue, like during the war again tie
        if (warActive) {
            continueWar();
            return;
        }

        // if game is single mode, go inside
        if (game.isSingleMode()) {
            Card pDrawC = game.drawPlayerCard();
            Card gDrawC = game.drawOpponentCard();

            PCardDraw.setIcon(loadCardIcon(pDrawC));
            GCardDraw.setIcon(loadCardIcon(gDrawC));
            Game.Outcome result = game.evaluateRound(pDrawC, gDrawC);
            if (result == Game.Outcome.TIE) {
                startWar(pDrawC, gDrawC, "WAR BEGINS");
            } else {
                game.awardBattleCards(result, pDrawC, gDrawC);
                displayOutcome(pDrawC, gDrawC);
            }
        } else {
            // waiting till guest player pressing button
            if (!awaitingOpponent) {
                Card p = game.drawPlayerCard();
                PCardDraw.setIcon(loadCardIcon(p));
                lastPlayerCard = p;
                awaitingOpponent = true;
                PDraw.setEnabled(false);
                GDraw.setEnabled(true);
                WhoWins.setText("Waiting for Player 2…");
            }
        }

        // checkin if game over
        checkGameOver();
    }

    // starting war method
    private void startWar(Card firstP, Card firstO, String title) {
        // if goes into a new war, cleanpile and add first two cards
        if (!warActive) {
            warActive = true;
            warPile.clear();
            warPile.add(firstP);
            warPile.add(firstO);
        }

        Card pCardDown1 = game.drawPlayerCard(), gCardDown1 = game.drawOpponentCard();
        Card pCardDown2 = game.drawPlayerCard(), gCardDown2 = game.drawOpponentCard();
        warPile.add(pCardDown1); warPile.add(gCardDown1);
        warPile.add(pCardDown2); warPile.add(gCardDown2);

        Card pCardUp = game.drawPlayerCard(), gCardUp = game.drawOpponentCard();
        warPile.add(pCardUp); warPile.add(gCardUp);

        PWarDraw.setIcon(loadCardIcon(pCardUp));
        GWarDraw.setIcon(loadCardIcon(gCardUp));
        WarPanel.setVisible(true);
        GameTitle.setText(title);
        WhoWins.setText("WAR! WAR! WAR!");
    }

    // continue war, if war still continue
    private void continueWar() {
        int sizeWarPile = warPile.size();
        Card pCardUp = warPile.get(sizeWarPile - 2), gCardUp = warPile.get(sizeWarPile - 1);
        Game.Outcome roundResult = game.evaluateRound(pCardUp, gCardUp);

        if (roundResult == Game.Outcome.TIE) {
            // still tie, war continues
            startWar(pCardUp, gCardUp, "WAR CONTINUES");
        } else {
            // war is over.
            game.awardWarCards(roundResult, warPile);
            warPile.clear();
            warActive = false;
            WarPanel.setVisible(false);

            // result and update gui
            String winner = roundResult == Game.Outcome.PLAYER_WIN ? game.getUsername() : (game.isSingleMode() ? "Computer" : "Player 2");
            WhoWins.setText(winner + " wins war!");
            displayOutcome(pCardUp, gCardUp);
        }
    }

    // draw card button for guest player
    private void GDraw(ActionEvent e) {
        // when player 1 pressed the button, guest player active
        if (!awaitingOpponent) return;

        Card gCard = game.drawOpponentCard();
        GCardDraw.setIcon(loadCardIcon(gCard));
        GTotalCard.setText("TOTAL: " + game.getOpponentDeckSize());

        Game.Outcome resultBattle = game.evaluateRound(lastPlayerCard, gCard);

        if (resultBattle == Game.Outcome.TIE) {
            warPile.clear();
            warPile.add(lastPlayerCard);
            warPile.add(gCard);

            startWar(lastPlayerCard, gCard, "WAR BEGINS");

            awaitingOpponent = false;
            PDraw.setEnabled(true);
            GDraw.setEnabled(false);
        } else {
            game.awardBattleCards(resultBattle, lastPlayerCard, gCard);
            displayOutcome(lastPlayerCard, gCard);

            awaitingOpponent = false;
            PDraw.setEnabled(true);
            GDraw.setEnabled(false);
        }
        checkGameOver();
    }

    // Saving game and going back with menubar
    private void SaveGameGoBack(ActionEvent e) {
        game.saveState();
        new SaveGame().save(gamesList);
        new FirstMenu();
        this.dispose();
    }

    // checking is game over
    private void checkGameOver() {
        boolean playerEmpty   = game.getPlayerDeckSize() == 0;
        boolean opponentEmpty = game.getOpponentDeckSize() == 0;
        if (!playerEmpty && !opponentEmpty) return;

        // deciding the winner
        String winner;
        if (playerEmpty) {
            winner = game.isSingleMode() ? "Computer" : "Player 2";
        } else {
            winner = game.getUsername();
        }

        JOptionPane.showMessageDialog(
                this,
                winner + " wins the game!\nCreate a new game if you want to play..\n\nPress OK to return to Main Menu.",
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
        );
        new FirstMenu().setVisible(true);
        dispose();
    }


    private void buildUI() {
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        MenuBarGame = new JMenuBar();
        MenuGame = new JMenu();
        MenuItemSaveGame = new JMenuItem();
        GameTablePanel = new JPanel();
        PlayerPanel = new JPanel();
        PCardBack = new JLabel();
        PTitle = new JLabel();
        PCardDraw = new JLabel();
        PTotalCard = new JLabel();
        PDraw = new JButton();
        GuestPanel = new JPanel();
        GCardBack = new JLabel();
        GTitle = new JLabel();
        GCardDraw = new JLabel();
        GTotalCard = new JLabel();
        GDraw = new JButton();
        WarPanel = new JPanel();
        PWarBack1 = new JLabel();
        GWarBack1 = new JLabel();
        GWarBack2 = new JLabel();
        PWarBack2 = new JLabel();
        GWarDraw = new JLabel();
        PWarDraw = new JLabel();
        TitlePanel = new JPanel();
        GameTitle = new JLabel();
        SubTitlePanel = new JPanel();
        WhoWins = new JLabel();
        GameCardBackground = new JLabel();

        //*** MenuBarGame ***
        MenuGame.setText("Game");
        MenuItemSaveGame.setText("Save & Exit");
        MenuItemSaveGame.addActionListener(e -> SaveGameGoBack(e));
        MenuGame.add(MenuItemSaveGame);
        MenuBarGame.add(MenuGame);
        setJMenuBar(MenuBarGame);

        //*** GameTablePanel ***
        GameTablePanel.setLayout(null);

        //*** PlayerPanel ***
        PlayerPanel.setBackground(new Color(0x00ff33));
        PlayerPanel.setOpaque(false);
        PlayerPanel.setLayout(null);
        PCardBack.setIcon(new ImageIcon(getClass().getResource("/OtherImages/Black_Card_back.png")));
        PCardBack.setBounds(80, 75, 200, 270);
        PlayerPanel.add(PCardBack);
        PTitle.setText("PLAYER 1");
        PTitle.setFont(new Font("Lucida Handwriting", Font.BOLD, 22));
        PTitle.setHorizontalAlignment(SwingConstants.CENTER);
        PTitle.setBounds(80, 35, 200, 35);
        PlayerPanel.add(PTitle);
        PCardDraw.setBounds(80, 435, 200, 270);
        PlayerPanel.add(PCardDraw);
        PTotalCard.setText("TOTAL CARD:");
        PTotalCard.setFont(new Font("Lucida Handwriting", Font.BOLD, 18));
        PTotalCard.setBounds(80, 390, 200, 45);
        PlayerPanel.add(PTotalCard);
        PDraw.setText("DRAW");
        PDraw.setBackground(new Color(0xc1b9b4));
        PDraw.setBorderPainted(false);
        PDraw.setFont(new Font("Papyrus", Font.BOLD, 35));
        PDraw.addActionListener(e -> PDraw(e));
        PDraw.setBounds(90, 740, 180, 50);
        PlayerPanel.add(PDraw);
        GameTablePanel.add(PlayerPanel);
        PlayerPanel.setBounds(0, 30, 350, 830);

        //*** GuestPanel ***
        GuestPanel.setBackground(new Color(0x006666));
        GuestPanel.setOpaque(false);
        GuestPanel.setLayout(null);
        GCardBack.setIcon(new ImageIcon(getClass().getResource("/OtherImages/Black_Card_back.png")));
        GCardBack.setBounds(70, 75, 200, 270);
        GuestPanel.add(GCardBack);
        GTitle.setText("PLAYER 2");
        GTitle.setFont(new Font("Lucida Handwriting", Font.BOLD, 22));
        GTitle.setHorizontalAlignment(SwingConstants.CENTER);
        GTitle.setBounds(70, 35, 200, 35);
        GuestPanel.add(GTitle);
        GCardDraw.setBounds(70, 435, 200, 270);
        GuestPanel.add(GCardDraw);
        GTotalCard.setText("TOTAL CARD:");
        GTotalCard.setFont(new Font("Lucida Handwriting", Font.BOLD, 19));
        GTotalCard.setBounds(75, 390, 190, 45);
        GuestPanel.add(GTotalCard);
        GDraw.setText("DRAW");
        GDraw.setBackground(new Color(0xc1b9b4));
        GDraw.setBorderPainted(false);
        GDraw.setFont(new Font("Papyrus", Font.BOLD, 35));
        GDraw.addActionListener(e -> GDraw(e));
        GDraw.setBounds(85, 740, 180, 50);
        GuestPanel.add(GDraw);
        GameTablePanel.add(GuestPanel);
        GuestPanel.setBounds(930, 30, 350, 830);

        //*** WarPanel ***
        WarPanel.setBackground(new Color(0xff33ff));
        WarPanel.setOpaque(false);
        WarPanel.setVisible(false);
        WarPanel.setLayout(null);
        PWarBack1.setIcon(new ImageIcon(getClass().getResource("/Black_Card_back.png")));
        PWarBack1.setBounds(25, 10, 200, 270);
        WarPanel.add(PWarBack1);
        GWarBack1.setIcon(new ImageIcon(getClass().getResource("/Black_Card_back.png")));
        GWarBack1.setBounds(355, 10, 200, 270);
        WarPanel.add(GWarBack1);
        GWarBack2.setIcon(new ImageIcon(getClass().getResource("/Black_Card_back.png")));
        GWarBack2.setBounds(305, 30, 200, 270);
        WarPanel.add(GWarBack2);
        PWarBack2.setIcon(new ImageIcon(getClass().getResource("/Black_Card_back.png")));
        PWarBack2.setBounds(75, 30, 200, 270);
        WarPanel.add(PWarBack2);
        GWarDraw.setBounds(335, 305, 200, 270);
        WarPanel.add(GWarDraw);
        PWarDraw.setBounds(45, 305, 200, 270);
        WarPanel.add(PWarDraw);
        GameTablePanel.add(WarPanel);
        WarPanel.setBounds(350, 175, 580, 580);

        //*** TitlePanel ***
        TitlePanel.setBackground(new Color(0x9999ff));
        TitlePanel.setOpaque(false);
        TitlePanel.setLayout(null);
        GameTitle.setText("BATTLE!");
        GameTitle.setHorizontalAlignment(SwingConstants.CENTER);
        GameTitle.setFont(new Font("Lucida Handwriting", Font.BOLD, 59));
        GameTitle.setBounds(0, 0, 580, 110);
        TitlePanel.add(GameTitle);
        GameTablePanel.add(TitlePanel);
        TitlePanel.setBounds(350, 65, 580, 115);

        //*** SubTitlePanel ***
        SubTitlePanel.setOpaque(false);
        SubTitlePanel.setLayout(null);
        WhoWins.setText("WHO WINS");
        WhoWins.setHorizontalAlignment(SwingConstants.CENTER);
        WhoWins.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 42));
        WhoWins.setBounds(0, 0, 580, 80);
        SubTitlePanel.add(WhoWins);
        GameTablePanel.add(SubTitlePanel);
        SubTitlePanel.setBounds(350, 755, 580, 105);

        //*** GameCardBackground ***
        GameCardBackground.setIcon(new ImageIcon(getClass().getResource("/OtherImages/game_card_background.png")));
        GameCardBackground.setBounds(0, 30, 1280, 830);
        GameTablePanel.add(GameCardBackground);

        contentPane.add(GameTablePanel);
        GameTablePanel.setBounds(0, -30, 1280, 860);
    }
}
