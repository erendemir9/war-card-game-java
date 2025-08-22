package Gui;

import deck.Card;
import game.Game;
import game.GamesList;
import player.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class GuestGame extends JFrame {

    private Game game;       // SINGLE modda oynanan oyun
    private boolean warActive = false;
    private final List<Card> warPile = new ArrayList<>();

    private JMenuBar MenuBarExit;
    private JMenu MenuExit;
    private JMenuItem MenuItemExit;
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

    public GuestGame() {
        // Creating a HumanPlayer for playing as a guest
        HumanPlayer guest = new HumanPlayer("Player", "Guest Player");

        // empty gamelist, because the game cannot be saved during the guest
        GamesList gl = new GamesList();

        // a new game in singe player
        this.game = new Game(guest, "SINGLE", gl);

        buildUI();

        setVisible(true);
        setSize(1280, 860);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // go to main menu button
    private void GoMainMenu(ActionEvent e) {
        new FirstMenu().setVisible(true);
        this.dispose();
    }

    // Draw button for player 1
    private void PDraw(ActionEvent e) {
        if (warActive) {
            continueWar();
            return;
        }

        if (game.isSingleMode()) {

            Card p = game.drawPlayerCard();
            Card o = game.drawOpponentCard();
            PCardDraw.setIcon(loadCardIcon(p));
            GCardDraw.setIcon(loadCardIcon(o));
            Game.Outcome resultBattle = game.evaluateRound(p, o);
            if (resultBattle == Game.Outcome.TIE) {
                startWar(p, o, "WAR BEGINS");
            } else {
                game.awardBattleCards(resultBattle, p, o);
                displayOutcome(p, o);
            }
        }
    }

    // Starting war
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

    // war still continues
    private void continueWar() {
        int sizeWarPile = warPile.size();
        Card pCardUp = warPile.get(sizeWarPile - 2), gCardUp = warPile.get(sizeWarPile - 1);
        Game.Outcome roundResult = game.evaluateRound(pCardUp, gCardUp);

        if (roundResult == Game.Outcome.TIE) {
            // still tie, war continues
            startWar(pCardUp, gCardUp, "WAR CONTINUES");
        }
        else {
            // war is over.
            game.awardWarCards(roundResult, warPile);
            warPile.clear();
            warActive = false;
            WarPanel.setVisible(false);

            // result and update gui
            String winner = roundResult == Game.Outcome.PLAYER_WIN
                    ? game.getUsername() :("Computer" );
            WhoWins.setText(winner + " wins war!");
            displayOutcome(pCardUp, gCardUp);
        }
    }

    // displaying and updating outcome in gui
    private void displayOutcome(Card pCard, Card gCard) {
        Game.Outcome roundResult = game.evaluateRound(pCard, gCard);
        switch (roundResult) {
            case PLAYER_WIN:   WhoWins.setText(game.getUsername()+" wins!");     break;
            case OPPONENT_WIN: WhoWins.setText("Computer wins!"); break;
            case TIE:
                WhoWins.setText("Tie!");
                WarPanel.setVisible(true);
                GameTitle.setText("WAR BEGINS");
                return;
        }
        WarPanel.setVisible(false);
        GameTitle.setText("BATTLE!");
        PTotalCard.setText("TOTAL: "+game.getPlayerDeckSize());
        GTotalCard.setText("TOTAL: "+game.getOpponentDeckSize());
    }

    // loading card icons from cards folder
    private ImageIcon loadCardIcon(Card c) {
        String path = String.format("/cards/%d_of_%s.png", c.getRank(), c.getSuit().toLowerCase());
        return new ImageIcon(getClass().getResource(path));
    }

    private void buildUI() {
        MenuBarExit = new JMenuBar();
        MenuExit     = new JMenu();
        MenuItemExit = new JMenuItem();
        GameTablePanel = new JPanel();
        PlayerPanel    = new JPanel();
        PCardBack      = new JLabel();
        PTitle         = new JLabel();
        PCardDraw      = new JLabel();
        PTotalCard     = new JLabel();
        PDraw          = new JButton();
        GuestPanel     = new JPanel();
        GCardBack      = new JLabel();
        GTitle         = new JLabel();
        GCardDraw      = new JLabel();
        GTotalCard     = new JLabel();
        WarPanel       = new JPanel();
        PWarBack1      = new JLabel();
        GWarBack1      = new JLabel();
        GWarBack2      = new JLabel();
        PWarBack2      = new JLabel();
        GWarDraw       = new JLabel();
        PWarDraw       = new JLabel();
        TitlePanel     = new JPanel();
        GameTitle      = new JLabel();
        SubTitlePanel       = new JPanel();
        WhoWins        = new JLabel();
        GameCardBackground = new JLabel();

        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //*** MenuBarExit ***
        MenuExit.setText("Exit");
        MenuItemExit.setText("Main Menu");
        MenuItemExit.addActionListener(e -> GoMainMenu(e));
        MenuExit.add(MenuItemExit);
        MenuBarExit.add(MenuExit);
        contentPane.add(MenuBarExit);
        MenuBarExit.setBounds(0, 0, 1278, 22);

        //*** GameTablePanel ***
        GameTablePanel.setLayout(null);

        //*** PlayerPanel ***
        PlayerPanel.setBackground(new Color(0x00ff33));
        PlayerPanel.setOpaque(false);
        PlayerPanel.setLayout(null);

        //***PCardBack ***
        PCardBack.setIcon(new ImageIcon(getClass().getResource("/OtherImages/Black_Card_back.png")));
        PlayerPanel.add(PCardBack);
        PCardBack.setBounds(80, 75, 200, 270);

        //*** PTitle ***
        PTitle.setText("PLAYER 1");
        PTitle.setFont(new Font("Lucida Handwriting", Font.BOLD, 22));
        PTitle.setHorizontalAlignment(SwingConstants.CENTER);
        PlayerPanel.add(PTitle);
        PTitle.setBounds(80, 30, 200, 35);
        PlayerPanel.add(PCardDraw);
        PCardDraw.setBounds(80, 435, 200, 270);

        //*** PTotalCard ***
        PTotalCard.setText("TOTAL CARD:");
        PTotalCard.setFont(new Font("Lucida Handwriting", Font.BOLD, 18));
        PlayerPanel.add(PTotalCard);
        PTotalCard.setBounds(80, 390, 200, 45);

        //*** PDraw ***
        PDraw.setText("DRAW");
        PDraw.setBackground(new Color(0xc1b9b4));
        PDraw.setBorderPainted(false);
        PDraw.setFont(new Font("Papyrus", Font.BOLD, 35));
        PDraw.addActionListener(e -> PDraw(e));
        PlayerPanel.add(PDraw);
        PDraw.setBounds(80, 730, 200, 50);

        GameTablePanel.add(PlayerPanel);
        PlayerPanel.setBounds(0, -5, 350, 825);

        //*** GuestPanel ***
        GuestPanel.setBackground(new Color(0x006666));
        GuestPanel.setOpaque(false);
        GuestPanel.setLayout(null);

        //*** GCardBack ***
        GCardBack.setIcon(new ImageIcon(getClass().getResource("/OtherImages/Black_Card_back.png")));
        GuestPanel.add(GCardBack);
        GCardBack.setBounds(70, 75, 200, 270);

        //*** GTitle ***
        GTitle.setText("PLAYER 2");
        GTitle.setFont(new Font("Lucida Handwriting", Font.BOLD, 22));
        GTitle.setHorizontalAlignment(SwingConstants.CENTER);
        GuestPanel.add(GTitle);
        GTitle.setBounds(70, 35, 200, 35);
        GuestPanel.add(GCardDraw);
        GCardDraw.setBounds(70, 435, 200, 270);

        //*** GTotalCard ***
        GTotalCard.setText("TOTAL CARD:");
        GTotalCard.setFont(new Font("Lucida Handwriting", Font.BOLD, 19));
        GuestPanel.add(GTotalCard);
        GTotalCard.setBounds(75, 390, 190, 45);

        GameTablePanel.add(GuestPanel);
        GuestPanel.setBounds(930, -10, 350, 830);

        //***WarPanel ***
        WarPanel.setBackground(new Color(0xff33ff));
        WarPanel.setOpaque(false);
        WarPanel.setVisible(false);
        WarPanel.setLayout(null);

        //*** PWarBack1 ***
        PWarBack1.setIcon(new ImageIcon(getClass().getResource("/Black_Card_back.png")));
        WarPanel.add(PWarBack1);
        PWarBack1.setBounds(25, 10, 200, 270);

        //*** GWarBack1 ***
        GWarBack1.setIcon(new ImageIcon(getClass().getResource("/Black_Card_back.png")));
        WarPanel.add(GWarBack1);
        GWarBack1.setBounds(355, 10, 200, 270);

        //*** GWarBack2 ***
        GWarBack2.setIcon(new ImageIcon(getClass().getResource("/Black_Card_back.png")));
        WarPanel.add(GWarBack2);
        GWarBack2.setBounds(305, 30, 200, 270);

        //*** PWarBack2 ***
        PWarBack2.setIcon(new ImageIcon(getClass().getResource("/Black_Card_back.png")));
        WarPanel.add(PWarBack2);
        PWarBack2.setBounds(75, 30, 200, 270);
        WarPanel.add(GWarDraw);
        GWarDraw.setBounds(335, 305, 200, 270);
        WarPanel.add(PWarDraw);
        PWarDraw.setBounds(45, 305, 200, 270);

        GameTablePanel.add(WarPanel);
        WarPanel.setBounds(350, 115, 580, 580);

        //***TitlePanel ***
        TitlePanel.setBackground(new Color(0x9999ff));
        TitlePanel.setOpaque(false);
        TitlePanel.setLayout(null);

        //***GameTitle ***
        GameTitle.setText("BATTLE!");
        GameTitle.setHorizontalAlignment(SwingConstants.CENTER);
        GameTitle.setFont(new Font("Lucida Handwriting", Font.BOLD, 59));
        TitlePanel.add(GameTitle);
        GameTitle.setBounds(0, 0, 580, 110);

        GameTablePanel.add(TitlePanel);
        TitlePanel.setBounds(350, 5, 580, 115);

        //*** SubTitlePanel ***
        SubTitlePanel.setOpaque(false);
        SubTitlePanel.setLayout(null);
        SubTitlePanel.setBounds(350, 695, 580, 105);

        //*** WhoWins ***
        WhoWins.setText("WHO WINS");
        WhoWins.setHorizontalAlignment(SwingConstants.CENTER);
        WhoWins.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 42));
        WhoWins.setBounds(0, 20, 580, 80);
        SubTitlePanel.add(WhoWins);

        GameTablePanel.add(SubTitlePanel);
        //*** GameCardBackground ***
        GameCardBackground.setIcon(new ImageIcon(getClass().getResource("/OtherImages/game_card_background.png")));
        GameTablePanel.add(GameCardBackground);
        GameCardBackground.setBounds(0, -10, 1280, 830);

        contentPane.add(GameTablePanel);
        GameTablePanel.setBounds(0, 20, 1280, 860);
    }
}
