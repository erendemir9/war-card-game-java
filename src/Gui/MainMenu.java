package Gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenu extends JFrame {
    private JPanel PanelMainMenu;
    private JLabel Title;
    private JButton PlayGame;
    private JButton SeeTheRules;
    private JButton GameInfo;
    private JLabel Background;

    // constructor
    public MainMenu() {
        buildUI();
        setVisible(true);
        setSize(1280, 860);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // play game button
    private void PlayGame(ActionEvent e) {
        dispose();
        new FirstMenu();
    }

    // see the rules button
    private void SeeTheRules(ActionEvent e) {
        String rules = "WAR CARD GAME RULES:\n\n" +
                "1) The deck is divided evenly between two players\n" +
                "2) Each player draws the top card from their deck\n" +
                "3) The player with the higher card wins both cards\n" +
                "4) If the cards are equal, it's WAR!\n" +
                "5) During WAR, each player places 2 cards face down and 1 face up\n" +
                "6) The highest face-up card wins all the cards\n" +
                "7) The game continues until one player has all the cards\n" +
                "8) Ace is the highest card (value 14)\n" +
                "9) You can save your progress and resume later\n\n" +
                "Good luck and have fun!";
        JOptionPane.showMessageDialog(PanelMainMenu, rules, "Game Rules", JOptionPane.INFORMATION_MESSAGE);
    }

    // info button
    private void GameInfo(ActionEvent e) {
        String gameInfo = "War Card Game (Java Swing)\n\n" +
                "Developer: Halil Eren Demir\n" +
                "Built with: Java Swing & OOP principles\n" +
                "Features: User accounts, saving progress, multiple modes\n";

        JOptionPane.showMessageDialog(
                PanelMainMenu,
                gameInfo,
                "About Game",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void buildUI() {
        PanelMainMenu = new JPanel();
        Title = new JLabel();
        PlayGame = new JButton();
        SeeTheRules = new JButton();
        GameInfo = new JButton();
        Background = new JLabel();

        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        PanelMainMenu.setLayout(null);

        // *** Title ***
        Title.setText("WAR GAME");
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        Title.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 123));
        PanelMainMenu.add(Title);
        Title.setBounds(265, 165, 790, 125);

        // *** PlayGame ***
        PlayGame.setText("Play Game");
        PlayGame.setFont(new Font("Lucida Handwriting", Font.PLAIN, 45));
        PlayGame.setBackground(new Color(0xb28769));
        PlayGame.setBorderPainted(false);
        PlayGame.addActionListener(e -> PlayGame(e));
        PanelMainMenu.add(PlayGame);
        PlayGame.setBounds(455, 420, 425, 70);

        // *** SeeTheRules ***
        SeeTheRules.setText("See The Rules");
        SeeTheRules.setFont(new Font("Lucida Handwriting", Font.PLAIN, 45));
        SeeTheRules.setBackground(new Color(0xb28769));
        SeeTheRules.setBorderPainted(false);
        SeeTheRules.addActionListener(e -> SeeTheRules(e));
        PanelMainMenu.add(SeeTheRules);
        SeeTheRules.setBounds(455, 525, 425, 70);

        // *** GameInfo ***
        GameInfo.setText("Game Info");
        GameInfo.setFont(new Font("Lucida Handwriting", Font.PLAIN, 45));
        GameInfo.setBackground(new Color(0xb28769));
        GameInfo.setBorderPainted(false);
        GameInfo.addActionListener(e -> GameInfo(e));
        PanelMainMenu.add(GameInfo);
        GameInfo.setBounds(455, 625, 425, 70);

        // *** Background ***
        Background.setIcon(new ImageIcon(getClass().getResource("/OtherImages/background.png")));
        PanelMainMenu.add(Background);
        Background.setBounds(0, 30, 1280, 830);

        contentPane.add(PanelMainMenu);
        PanelMainMenu.setBounds(0, -30, 1280, 860);
    }
}
