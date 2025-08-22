package Gui;

import file.ReadUsers;
import file.SaveUsers;
import player.HumanPlayer;
import player.User;
import player.UserList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FirstMenu extends JFrame {
    private ReadUsers readUsers = new ReadUsers();
    private UserList userList = new UserList();
    private SaveUsers saveUsers = new SaveUsers();

    private JPanel PanelFirstMenu;
    private JLabel Title;
    private JButton SignInButton;
    private JButton SignUpButton;
    private JButton ContinueGuestButton;
    private JButton GoMainMenu;
    private JLabel Background;

    public FirstMenu() {
        buildUI();

        // calling loadusers method
        loadUsers();
        setVisible(true);
        setSize(1280, 860);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // loading users from the txt file
    private void loadUsers() {
        for (String line : readUsers.load()) {
            String[] parts = line.split(" ", 2);
            if (parts.length == 2) {
                userList.addUser(new HumanPlayer(parts[0], parts[1]));
            }
        }
    }

    // go to main menu button
    private void GoMainMenu(ActionEvent e) {
        dispose();
        new MainMenu();
    }

    // Sign in button
    private void SignIn(ActionEvent e) {

        String username;
        String name;
        while (true) {
            username = JOptionPane.showInputDialog(
                    this,
                    "Enter your username:",
                    "Sign In",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (username == null) return;

            if (userList.idExists(username)) {
                break;
            }
            JOptionPane.showMessageDialog(
                    this,
                    "That username does not exist!\nTry again or Cancel to go back.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        String realName = "";
        for (User user : userList.getUsers()) {
            if (user.getId().equals(username)) {
                realName = user.getName();
                break;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Welcome, " + username + "!",
                "Signed In",
                JOptionPane.INFORMATION_MESSAGE
        );

        this.dispose();
        // go to signedinmenu
        new SignedInMenu(username, realName).setVisible(true);
    }

    // Sign up button
    private void SignUpButton(ActionEvent e) {
        String username;
        while (true) {
            username = JOptionPane.showInputDialog(
                    this,
                    "Enter a username you want to create:",
                    "Sign Up",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (username == null) {
                return;
            }

            if (!userList.idExists(username)) {
                break;
            }
            JOptionPane.showMessageDialog(
                    this,
                    "That username already exists!\nPlease choose another or Cancel to go back.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        String name = JOptionPane.showInputDialog(
                this,
                "Enter your name:",
                "Sign Up",
                JOptionPane.QUESTION_MESSAGE
        );
        if (name == null) {
            return;
        }

        // creating user after controls and adding to the txt file and game memory
        HumanPlayer newUser = new HumanPlayer(username, name);
        saveUsers.save(newUser); // Saving user in the txt file
        userList.addUser(newUser); // Saving user in the userList means memory

        JOptionPane.showMessageDialog(
                this,
                "User created successfully!\nUsername: " + username + "\nName: " + name,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // continue as a guest button
    private void ContinueGuestButton(ActionEvent e) {
        new GuestGame().setVisible(true);
        this.dispose();
    }

    private void buildUI() {
        PanelFirstMenu = new JPanel();
        Title = new JLabel();
        SignInButton = new JButton();
        SignUpButton = new JButton();
        ContinueGuestButton = new JButton();
        GoMainMenu    = new JButton();
        Background = new JLabel();

        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        PanelFirstMenu.setLayout(null);

        //*** Title ***
        Title.setText("MENU");
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        Title.setFont(new Font("Lucida Handwriting", Font.BOLD | Font.ITALIC, 163));
        PanelFirstMenu.add(Title);
        Title.setBounds(205, 140, 890, 185);

        //*** SignInButton ***
        SignInButton.setText("Sign In");
        SignInButton.setFont(new Font("Lucida Handwriting", Font.PLAIN, 45));
        SignInButton.setBackground(new Color(0xb28769));
        SignInButton.setBorderPainted(false);
        SignInButton.addActionListener(e -> {SignIn(e);});
        PanelFirstMenu.add(SignInButton);
        SignInButton.setBounds(390, 370, 550, 70);

        //*** SignUpButton ***
        SignUpButton.setText("Sign Up");
        SignUpButton.setFont(new Font("Lucida Handwriting", Font.PLAIN, 45));
        SignUpButton.setBackground(new Color(0xb28769));
        SignUpButton.setBorderPainted(false);
        SignUpButton.addActionListener(e -> SignUpButton(e));
        PanelFirstMenu.add(SignUpButton);
        SignUpButton.setBounds(390, 475, 550, 70);

        //*** Continue As A Guest ***
        ContinueGuestButton.setText("Continue As A Guest");
        ContinueGuestButton.setFont(new Font("Lucida Handwriting", Font.PLAIN, 45));
        ContinueGuestButton.setBackground(new Color(0xb28769));
        ContinueGuestButton.setBorderPainted(false);
        ContinueGuestButton.addActionListener(e -> ContinueGuestButton(e));
        PanelFirstMenu.add(ContinueGuestButton);
        ContinueGuestButton.setBounds(390, 575, 550, 70);

        //*** GoMainMenu ***
        GoMainMenu.setText("Main Menu");
        GoMainMenu.setFont(new Font("Lucida Handwriting", Font.PLAIN, 45));
        GoMainMenu.setBackground(new Color(0xb28769));
        GoMainMenu.setBorderPainted(false);
        GoMainMenu.addActionListener(e -> GoMainMenu(e));
        PanelFirstMenu.add(GoMainMenu);
        GoMainMenu.setBounds(390, 675, 550, 70);
        
        //*** Background ***
        Background.setIcon(new ImageIcon(getClass().getResource("/OtherImages/background.png")));
        PanelFirstMenu.add(Background);
        Background.setBounds(0, 30, 1280, 830);

        contentPane.add(PanelFirstMenu);
        PanelFirstMenu.setBounds(0, -30, 1280, 860);
    }
}
