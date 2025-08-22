// src/game/Game.java
package game;

import java.util.ArrayList;
import java.util.List;

import deck.Card;
import deck.ShufflingDeck;
import player.ComputerPlayer;
import player.GuestPlayer;
import player.HumanPlayer;

public class Game {

	private String username; // The player username who saves the game. HOST
	private String mode; // game mode: single,two players
	private boolean resumed = false; // is game resumed
	private List<Card> user1Cards;
	private List<Card> user2Cards;
	// private Scanner scan= new Scanner(System.in);
	private HumanPlayer humanPlayer;
	private ComputerPlayer computerPlayer;
	private GuestPlayer guestPlayer;
	private GuestPlayer humanGuestPlayer;
	private GamesList gamesList;

	public Game(HumanPlayer player, String mode, GamesList gamesList) {
		this.username = player.getId();
		this.humanPlayer = player;
		this.mode = mode;
		this.gamesList = gamesList;

		ShufflingDeck shuffledDeck = new ShufflingDeck();
		List<Card> deck1 = shuffledDeck.getPlayerDeck();
		List<Card> deck2 = shuffledDeck.getGuestDeck();

		humanPlayer.setDeck(new ArrayList<>(deck1));
		if (isSingleMode()) {
			computerPlayer = new ComputerPlayer();
			computerPlayer.setDeck(new ArrayList<>(deck2));
		} else {
			guestPlayer = new GuestPlayer();
			guestPlayer.setDeck(new ArrayList<>(deck2));
		}
	}

	public Game() {
		ShufflingDeck shuffledDeck = new ShufflingDeck();
		this.user1Cards = shuffledDeck.getPlayerDeck();
		this.user2Cards = shuffledDeck.getGuestDeck();
		this.humanGuestPlayer = new GuestPlayer();
	}

	// getting username which is saved for game
	public String getUsername() {
		return username;
	}

	// is single?
	public boolean isSingleMode() {
		return "SINGLE".equals(mode);
	}

	// getting player 1 deck size
	public int getPlayerDeckSize() {
		return humanPlayer.getDeck().size();
	}

	// getting player 2(opponent) deck size
	public int getOpponentDeckSize() {
		if (isSingleMode()) {
			return computerPlayer.getDeck().size();
		} else {
			return guestPlayer.getDeck().size();
		}
	}

	// getting games list
	public GamesList getGamesList() {
		return this.gamesList;
	}

	// Player 1 drawing a card
	public Card drawPlayerCard() {
		Card card = humanPlayer.getFirstCardOfDeck();
		humanPlayer.removeCardFromDeck(card);
		return card;
	}

	// Player 2 (opponent) drawing a card
	public Card drawOpponentCard() {
		// if the game is single player, its against automatically computer
		if (mode.equals("SINGLE")) {
			Card c = computerPlayer.getFirstCardOfDeck();
			computerPlayer.removeCardFromDeck(c);
			return c;
		}

		else {
			Card c = guestPlayer.getFirstCardOfDeck();
			guestPlayer.removeCardFromDeck(c);
			return c;
		}
	}

	// enum, who wins or tie, fixed set of named constants. there is no other option
	// for this game (such as gender of human, or colours(red))
	public enum Outcome {
		PLAYER_WIN, OPPONENT_WIN, TIE
	}

	// computing battle result, who wins or tie
	public Outcome evaluateRound(Card p, Card o) {
		if (p.getRank() > o.getRank())
			return Outcome.PLAYER_WIN;
		if (p.getRank() < o.getRank())
			return Outcome.OPPONENT_WIN;
		return Outcome.TIE;
	}

	// reward the winner after battle
	public void awardBattleCards(Outcome resultBattle, Card p, Card o) {
		if (resultBattle == Outcome.PLAYER_WIN) {
			humanPlayer.addCardToDeck(p);
			humanPlayer.addCardToDeck(o);
		} else if (resultBattle == Outcome.OPPONENT_WIN) {
			if (isSingleMode()) {
				computerPlayer.addCardToDeck(p);
				computerPlayer.addCardToDeck(o);
			} else {
				guestPlayer.addCardToDeck(p);
				guestPlayer.addCardToDeck(o);
			}
		}
	}

	// reward the war winner
	public void awardWarCards(Outcome resultWar, List<Card> pile) {
		for (Card card : pile) {
			if (resultWar == Outcome.PLAYER_WIN) {
				humanPlayer.addCardToDeck(card);
			} else {
				if (isSingleMode()) {
					computerPlayer.addCardToDeck(card);
				} else {
					guestPlayer.addCardToDeck(card);
				}
			}
		}
	}

	// Saving progress of the game
	public void saveState() {
		GameState state = new GameState(username, mode);
		state.setPlayerDeck(humanPlayer.getDeck());
		if (mode.equals("SINGLE")) { // Single player
			state.setOpponentDeck(computerPlayer.getDeck());
		} else { // Two players
			state.setOpponentDeck(guestPlayer.getDeck());
		}
		gamesList.add(state);
	}

	// Loading progress of the game
	public void loadState(GameState state) {
		humanPlayer.setDeck(new ArrayList<>(state.getPlayerDeck()));
		if (state.getMode().equals("SINGLE")) {
			if (computerPlayer == null)
				computerPlayer = new ComputerPlayer();
			computerPlayer.setDeck(new ArrayList<>(state.getOpponentDeck()));
		} else {
			if (guestPlayer == null)
				guestPlayer = new GuestPlayer();
			guestPlayer.setDeck(new ArrayList<>(state.getOpponentDeck()));
		}
		this.resumed = true;
	}

	/*
	 * public void singlePlayerGame() {
	 * 
	 * // if game is resumed, dont create and set new cards, continue from the
	 * previous ones
	 * if(!resumed) {
	 * this.computerPlayer = new ComputerPlayer();
	 * computerPlayer.setDeck(user2Cards);
	 * 
	 * humanPlayer.setDeck(user1Cards);
	 * }
	 * 
	 * resumed =false;
	 * System.out.println("WELCOME '"+username+"' TO THE SINGLE PLAYER GAME");
	 * System.out.println("THE GAME STARTS");
	 * 
	 * while (true) {
	 * System.out.println("Click 'A' to draw a card.. (Click 'Q' to save and quit)"
	 * );
	 * String option = scan.nextLine();
	 * while (!option.equals("A")) {
	 * if (option.equals("Q")) {
	 * saveState();
	 * System.out.println("Saved Single Player Game.. Total Logs: " +
	 * gamesList.getAll().size());
	 * return;
	 * }
	 * System.out.println("Please click only 'A'");
	 * option = scan.nextLine();
	 * }
	 * 
	 * // Im assigning them to the card objects
	 * Card playerCard = humanPlayer.getFirstCardOfDeck();
	 * Card computerCard = computerPlayer.getFirstCardOfDeck();
	 * 
	 * System.out.print("Player '"+username+"' Card -> " );
	 * playerCard.printCardInfo();
	 * System.out.print("Computer Card -> "); computerCard.printCardInfo();
	 * 
	 * int a = playerCard.getRank();
	 * int b = computerCard.getRank();
	 * 
	 * if (a == b) {
	 * System.out.println("------------------- War Begins --------------------");
	 * // When the cards tied, remove them before go to the war method
	 * humanPlayer.removeCardFromDeck(playerCard);
	 * computerPlayer.removeCardFromDeck(computerCard);
	 * warSinglePlayer(playerCard,computerCard);
	 * System.out.println("------------------- War Ends --------------------");
	 * printDeckSizesSinglePlayer();
	 * } else {
	 * // First remove both cards from their deck
	 * humanPlayer.removeCardFromDeck(playerCard);
	 * computerPlayer.removeCardFromDeck(computerCard);
	 * 
	 * if (a > b) {
	 * // If player wins take both cards
	 * humanPlayer.addCardToDeck(playerCard);
	 * humanPlayer.addCardToDeck(computerCard);
	 * System.out.println("Player '"+username+" 'wins the battle");
	 * } else {
	 * // If computer wins take both cards
	 * computerPlayer.addCardToDeck(playerCard);
	 * computerPlayer.addCardToDeck(computerCard);
	 * System.out.println("Computer wins the battle");
	 * }
	 * printDeckSizesSinglePlayer();
	 * }
	 * 
	 * // Checking for the gameOver part
	 * if (humanPlayer.getDeck().isEmpty()) {
	 * printDeckSizesSinglePlayer();
	 * System.out.println("Computer Wins !!!");
	 * break;
	 * } else if (computerPlayer.getDeck().isEmpty()) {
	 * printDeckSizesSinglePlayer();
	 * System.out.println("Player '"+username + "' Wins !!!");
	 * break;
	 * }
	 * }
	 * }
	 */ // method for playing game in terminal

	/*
	 * public void twoPlayersGame(String username) {
	 * 
	 * // if game is resumed, dont create and set new cards, continue from the
	 * previous ones
	 * if (!resumed) {
	 * humanPlayer.setDeck(user1Cards);
	 * 
	 * this.guestPlayer=new GuestPlayer();
	 * guestPlayer.setDeck(user2Cards);
	 * }
	 * 
	 * 
	 * System.out.println("WELCOME TO THE TWO PLAYERS GAME");
	 * System.out.println("THE GAME STARTS");
	 * 
	 * while (true) {
	 * System.out.println("Click 'A' to draw a card.. (Click 'Q' to quit)");
	 * String optionPlayer1 = scan.nextLine();
	 * while (!optionPlayer1.equals("A")) {
	 * if (optionPlayer1.equals("Q")) {
	 * saveState();
	 * System.out.println("Saved Two Players Game.. Total Logs: " +
	 * gamesList.getAll().size());
	 * return;
	 * }
	 * System.out.println("Please click only 'A'");
	 * optionPlayer1 = scan.nextLine();
	 * }
	 * humanPlayer.setReady(true);
	 * 
	 * 
	 * System.out.println("Click 'L' to draw a card.. ");
	 * String optionPlayer2 = scan.nextLine();
	 * while (!optionPlayer2.equals("L")) {
	 * System.out.println("Please click only 'L'");
	 * optionPlayer2 = scan.nextLine();
	 * }
	 * guestPlayer.setReady(true);
	 * 
	 * // assigning them to the card objects
	 * Card playerCard = humanPlayer.getFirstCardOfDeck();
	 * Card guestCard = guestPlayer.getFirstCardOfDeck();
	 * 
	 * System.out.print("Player '"+username+"' Card -> " );
	 * playerCard.printCardInfo();
	 * System.out.print("Guest Player Card -> "); guestCard.printCardInfo();
	 * 
	 * int a = playerCard.getRank();
	 * int b = guestCard.getRank();
	 * 
	 * if (a == b) {
	 * System.out.println("------------------- War Begins --------------------");
	 * // When the cards tied, remove them before go to the war method
	 * humanPlayer.removeCardFromDeck(playerCard);
	 * guestPlayer.removeCardFromDeck(guestCard);
	 * warTwoPlayers(playerCard,guestCard);
	 * System.out.println("------------------- War Ends --------------------");
	 * printDeckSizesTwoPlayers(username);
	 * } else {
	 * // First remove both cards from their deck
	 * humanPlayer.removeCardFromDeck(playerCard);
	 * guestPlayer.removeCardFromDeck(guestCard);
	 * 
	 * if (a > b) {
	 * // If player wins take both cards
	 * humanPlayer.addCardToDeck(playerCard);
	 * humanPlayer.addCardToDeck(guestCard);
	 * System.out.println("Player '"+username+" 'wins the battle");
	 * } else {
	 * // If computer wins take both cards
	 * guestPlayer.addCardToDeck(playerCard);
	 * guestPlayer.addCardToDeck(guestCard);
	 * System.out.println("Guest Player wins the battle");
	 * }
	 * printDeckSizesTwoPlayers(username);
	 * }
	 * 
	 * // Checking for the gameOver part
	 * if (humanPlayer.getDeck().isEmpty()) {
	 * printDeckSizesTwoPlayers(username);
	 * System.out.println("Guest Player Wins !!!");
	 * break;
	 * } else if (guestPlayer.getDeck().isEmpty()) {
	 * printDeckSizesTwoPlayers(username);
	 * System.out.println("Player '"+username + "' Wins !!!");
	 * break;
	 * }
	 * }
	 * }
	 */ // method for playing game in terminal

	/*
	 * public void warSinglePlayer(Card playerCard, Card computerCard) {
	 * List<Card> playerWarCards = new ArrayList<>();
	 * List<Card> computerWarCards = new ArrayList<>();
	 * 
	 * playerWarCards.add(playerCard);
	 * computerWarCards.add(computerCard);
	 * 
	 * // Draw 3 cards: 2 face-down, 1 face-up
	 * for (int i = 0; i < 3; i++) {
	 * Card playerWarCard = humanPlayer.getFirstCardOfDeck();
	 * humanPlayer.removeCardFromDeck(playerWarCard);
	 * playerWarCards.add(playerWarCard);
	 * 
	 * Card computerWarCard = computerPlayer.getFirstCardOfDeck();
	 * computerPlayer.removeCardFromDeck(computerWarCard);
	 * computerWarCards.add(computerWarCard);
	 * }
	 * 
	 * Card playerLastCard = playerWarCards.get(playerWarCards.size() - 1);
	 * Card computerLastCard = computerWarCards.get(computerWarCards.size() - 1);
	 * System.out.print(">>>> War cards <<<< \n" +
	 * "Player '"+humanPlayer.getId()+"' -> " );
	 * playerLastCard.printCardInfo();
	 * System.out.print("Computer -> " );;
	 * computerLastCard.printCardInfo();
	 * 
	 * // Continue war if tie
	 * while (playerLastCard.getRank() == computerLastCard.getRank()) {
	 * System.out.println("Tie! Continuing war...");
	 * if (humanPlayer.getDeck().size() < 3 || computerPlayer.getDeck().size() < 3)
	 * {
	 * System.out.println("A player ran out of cards during war!"); // if a player's
	 * deck is done
	 * return;
	 * }
	 * for (int i = 0; i < 3; i++) {
	 * Card playerWarCard = humanPlayer.getFirstCardOfDeck();
	 * humanPlayer.removeCardFromDeck(playerWarCard);
	 * playerWarCards.add(playerWarCard);
	 * 
	 * Card computerWarCard = computerPlayer.getFirstCardOfDeck();
	 * computerPlayer.removeCardFromDeck(computerWarCard);
	 * computerWarCards.add(computerWarCard);
	 * }
	 * playerLastCard = playerWarCards.get(playerWarCards.size() - 1);
	 * computerLastCard = computerWarCards.get(computerWarCards.size() - 1);
	 * 
	 * System.out.print(">>>> War cards <<<< \n" +
	 * "Player '"+humanPlayer.getId()+"' -> " );
	 * playerLastCard.printCardInfo();
	 * System.out.print("Computer -> " );
	 * computerLastCard.printCardInfo();
	 * }
	 * 
	 * // Add the cards to the player who win the war
	 * if (playerLastCard.getRank() > computerLastCard.getRank()) {
	 * System.out.println("Player '"+humanPlayer.getId()+"' wins the war !!");
	 * for (Card c : playerWarCards) humanPlayer.addCardToDeck(c);
	 * for (Card c : computerWarCards) humanPlayer.addCardToDeck(c);
	 * } else {
	 * System.out.println("Computer wins the war !!");
	 * for (Card c : computerWarCards) computerPlayer.addCardToDeck(c);
	 * for (Card c : playerWarCards) computerPlayer.addCardToDeck(c);
	 * }
	 * }
	 * 
	 * public void warTwoPlayers(Card playerCard, Card guestCard) {
	 * List<Card> playerWarCards = new ArrayList<>();
	 * List<Card> guestWarCards = new ArrayList<>();
	 * 
	 * playerWarCards.add(playerCard);
	 * guestWarCards.add(guestCard);
	 * 
	 * // Draw 3 cards: 2 face-down, 1 face-up
	 * for (int i = 0; i < 3; i++) {
	 * Card playerWarCard = humanPlayer.getFirstCardOfDeck();
	 * humanPlayer.removeCardFromDeck(playerWarCard);
	 * playerWarCards.add(playerWarCard);
	 * 
	 * Card guestWarCard = guestPlayer.getFirstCardOfDeck();
	 * guestPlayer.removeCardFromDeck(guestWarCard);
	 * guestWarCards.add(guestWarCard);
	 * }
	 * 
	 * Card playerLastCard = playerWarCards.get(playerWarCards.size() - 1);
	 * Card guestLastCard = guestWarCards.get(guestWarCards.size() - 1);
	 * System.out.print(">>>> War cards <<<< \n" +
	 * "Player '"+humanPlayer.getId()+"' -> " );
	 * playerLastCard.printCardInfo();
	 * System.out.print("Guest Player -> " );;
	 * guestLastCard.printCardInfo();
	 * 
	 * // Continue war if tie
	 * while (playerLastCard.getRank() == guestLastCard.getRank()) {
	 * System.out.println("Tie! Continuing war...");
	 * if (humanPlayer.getDeck().size() < 3 || guestPlayer.getDeck().size() < 3) {
	 * System.out.println("A player ran out of cards during war!"); // if a player's
	 * deck is done
	 * return;
	 * }
	 * for (int i = 0; i < 3; i++) {
	 * Card playerWarCard = humanPlayer.getFirstCardOfDeck();
	 * humanPlayer.removeCardFromDeck(playerWarCard);
	 * playerWarCards.add(playerWarCard);
	 * 
	 * Card guestWarCard = guestPlayer.getFirstCardOfDeck();
	 * guestPlayer.removeCardFromDeck(guestWarCard);
	 * guestWarCards.add(guestWarCard);
	 * }
	 * playerLastCard = playerWarCards.get(playerWarCards.size() - 1);
	 * guestLastCard = guestWarCards.get(guestWarCards.size() - 1);
	 * 
	 * System.out.print(">>>> War cards <<<< \n" +
	 * "Player '"+humanPlayer.getId()+"' -> " );
	 * playerLastCard.printCardInfo();
	 * System.out.print("Guest Player -> " );
	 * guestLastCard.printCardInfo();
	 * }
	 * 
	 * // Add the cards to the player who win the war
	 * if (playerLastCard.getRank() > guestLastCard.getRank()) {
	 * System.out.println("Player '"+humanPlayer.getId()+"' wins the war !!");
	 * for (Card c : playerWarCards) humanPlayer.addCardToDeck(c);
	 * for (Card c : guestWarCards) humanPlayer.addCardToDeck(c);
	 * } else {
	 * System.out.println("Guest Player wins the war !!");
	 * for (Card c : guestWarCards) guestPlayer.addCardToDeck(c);
	 * for (Card c : playerWarCards) guestPlayer.addCardToDeck(c);
	 * }
	 * }
	 * 
	 * public void warSinglePlayerGuest(Card playerCard, Card computerCard) {
	 * List<Card> playerWarCards = new ArrayList<>();
	 * List<Card> computerWarCards = new ArrayList<>();
	 * 
	 * playerWarCards.add(playerCard);
	 * computerWarCards.add(computerCard);
	 * 
	 * // Draw 3 cards: 2 face-down, 1 face-up
	 * for (int i = 0; i < 3; i++) {
	 * Card playerWarCard = humanGuestPlayer.getFirstCardOfDeck();
	 * humanGuestPlayer.removeCardFromDeck(playerWarCard);
	 * playerWarCards.add(playerWarCard);
	 * 
	 * Card computerWarCard = computerPlayer.getFirstCardOfDeck();
	 * computerPlayer.removeCardFromDeck(computerWarCard);
	 * computerWarCards.add(computerWarCard);
	 * }
	 * 
	 * Card playerLastCard = playerWarCards.get(playerWarCards.size() - 1);
	 * Card computerLastCard = computerWarCards.get(computerWarCards.size() - 1);
	 * System.out.print(">>>> War cards <<<< \n" +
	 * "Player -> ");
	 * playerLastCard.printCardInfo();
	 * System.out.print("Computer -> " );;
	 * computerLastCard.printCardInfo();
	 * 
	 * // Continue war if tie
	 * while (playerLastCard.getRank() == computerLastCard.getRank()) {
	 * System.out.println("Tie! Continuing war...");
	 * if (humanGuestPlayer.getDeck().size() < 3 || computerPlayer.getDeck().size()
	 * < 3) {
	 * System.out.println("A player ran out of cards during war!"); // if a player's
	 * deck is done
	 * return;
	 * }
	 * for (int i = 0; i < 3; i++) {
	 * Card playerWarCard = humanGuestPlayer.getFirstCardOfDeck();
	 * humanGuestPlayer.removeCardFromDeck(playerWarCard);
	 * playerWarCards.add(playerWarCard);
	 * 
	 * Card computerWarCard = computerPlayer.getFirstCardOfDeck();
	 * computerPlayer.removeCardFromDeck(computerWarCard);
	 * computerWarCards.add(computerWarCard);
	 * }
	 * playerLastCard = playerWarCards.get(playerWarCards.size() - 1);
	 * computerLastCard = computerWarCards.get(computerWarCards.size() - 1);
	 * 
	 * System.out.print(">>>> War cards <<<< \n" +
	 * "Player -> " );
	 * playerLastCard.printCardInfo();
	 * System.out.print("Computer -> " );
	 * computerLastCard.printCardInfo();
	 * }
	 * 
	 * // Add the cards to the player who win the war
	 * if (playerLastCard.getRank() > computerLastCard.getRank()) {
	 * System.out.println("Player wins the war !!");
	 * for (Card c : playerWarCards) humanGuestPlayer.addCardToDeck(c);
	 * for (Card c : computerWarCards) humanGuestPlayer.addCardToDeck(c);
	 * } else {
	 * System.out.println("Computer wins the war !!");
	 * for (Card c : computerWarCards) computerPlayer.addCardToDeck(c);
	 * for (Card c : playerWarCards) computerPlayer.addCardToDeck(c);
	 * }
	 * }
	 * 
	 * 
	 * public void warTwoPlayersGuest(Card playerCard, Card guestCard) {
	 * List<Card> playerWarCards = new ArrayList<>();
	 * List<Card> guestWarCards = new ArrayList<>();
	 * 
	 * playerWarCards.add(playerCard);
	 * guestWarCards.add(guestCard);
	 * 
	 * // Draw 3 cards: 2 face-down, 1 face-up
	 * for (int i = 0; i < 3; i++) {
	 * Card playerWarCard = humanGuestPlayer.getFirstCardOfDeck();
	 * humanGuestPlayer.removeCardFromDeck(playerWarCard);
	 * playerWarCards.add(playerWarCard);
	 * 
	 * Card guestWarCard = guestPlayer.getFirstCardOfDeck();
	 * guestPlayer.removeCardFromDeck(guestWarCard);
	 * guestWarCards.add(guestWarCard);
	 * }
	 * 
	 * Card playerLastCard = playerWarCards.get(playerWarCards.size() - 1);
	 * Card guestLastCard = guestWarCards.get(guestWarCards.size() - 1);
	 * System.out.print(">>>> War cards <<<< \n" +
	 * "Player 1-> " );
	 * playerLastCard.printCardInfo();
	 * System.out.print("Guest Player (2) -> " );;
	 * guestLastCard.printCardInfo();
	 * 
	 * // Continue war if tie
	 * while (playerLastCard.getRank() == guestLastCard.getRank()) {
	 * System.out.println("Tie! Continuing war...");
	 * if (humanGuestPlayer.getDeck().size() < 3 || guestPlayer.getDeck().size() <
	 * 3) {
	 * System.out.println("A player ran out of cards during war!"); // if a player's
	 * deck is done
	 * return;
	 * }
	 * for (int i = 0; i < 3; i++) {
	 * Card playerWarCard = humanGuestPlayer.getFirstCardOfDeck();
	 * humanGuestPlayer.removeCardFromDeck(playerWarCard);
	 * playerWarCards.add(playerWarCard);
	 * 
	 * Card guestWarCard = guestPlayer.getFirstCardOfDeck();
	 * guestPlayer.removeCardFromDeck(guestWarCard);
	 * guestWarCards.add(guestWarCard);
	 * }
	 * playerLastCard = playerWarCards.get(playerWarCards.size() - 1);
	 * guestLastCard = guestWarCards.get(guestWarCards.size() - 1);
	 * 
	 * System.out.print(">>>> War cards <<<< \n" +
	 * "Player 1 -> " );
	 * playerLastCard.printCardInfo();
	 * System.out.print("Guest Player (2) -> " );
	 * guestLastCard.printCardInfo();
	 * }
	 * 
	 * // Add the cards to the player who win the war
	 * if (playerLastCard.getRank() > guestLastCard.getRank()) {
	 * System.out.println("Player 1 wins the war !!");
	 * for (Card c : playerWarCards) humanGuestPlayer.addCardToDeck(c);
	 * for (Card c : guestWarCards) humanGuestPlayer.addCardToDeck(c);
	 * } else {
	 * System.out.println("Guest Player (2) wins the war !!");
	 * for (Card c : guestWarCards) guestPlayer.addCardToDeck(c);
	 * for (Card c : playerWarCards) guestPlayer.addCardToDeck(c);
	 * }
	 * }
	 * public void singlePlayerGameGuest(){
	 * this.computerPlayer = new ComputerPlayer();
	 * computerPlayer.setDeck(user2Cards);
	 * 
	 * humanGuestPlayer.setDeck(user1Cards);
	 * 
	 * System.out.println("WELCOME '"+humanGuestPlayer.getName()
	 * +"' TO THE SINGLE PLAYER GAME");
	 * System.out.println("THE GAME STARTS");
	 * 
	 * while (true) {
	 * System.out.println("Click 'A' to draw a card.. (Click 'Q' to quit)");
	 * String option = scan.nextLine();
	 * while (!option.equals("A")) {
	 * if(option.equals("Q")){
	 * return;
	 * }
	 * System.out.println("Please click only 'A'");
	 * option = scan.nextLine();
	 * }
	 * 
	 * 
	 * Card playerCard = humanGuestPlayer.getFirstCardOfDeck();
	 * Card computerCard = computerPlayer.getFirstCardOfDeck();
	 * 
	 * System.out.print("Player Card -> " ); playerCard.printCardInfo();
	 * System.out.print("Computer Card -> "); computerCard.printCardInfo();
	 * 
	 * int a = playerCard.getRank();
	 * int b = computerCard.getRank();
	 * 
	 * if (a == b) {
	 * System.out.println("------------------- War Begins --------------------");
	 * // When the cards tied, remove them before go to the war method
	 * humanGuestPlayer.removeCardFromDeck(playerCard);
	 * computerPlayer.removeCardFromDeck(computerCard);
	 * warSinglePlayerGuest(playerCard,computerCard);
	 * System.out.println("------------------- War Ends --------------------");
	 * printDeckSizesSinglePlayerGuest();
	 * } else {
	 * // First remove both cards from their deck
	 * humanGuestPlayer.removeCardFromDeck(playerCard);
	 * computerPlayer.removeCardFromDeck(computerCard);
	 * 
	 * if (a > b) {
	 * // If player wins take both cards
	 * humanGuestPlayer.addCardToDeck(playerCard);
	 * humanGuestPlayer.addCardToDeck(computerCard);
	 * System.out.println("Player wins the battle");
	 * } else {
	 * // If computer wins take both cards
	 * computerPlayer.addCardToDeck(playerCard);
	 * computerPlayer.addCardToDeck(computerCard);
	 * System.out.println("Computer wins the battle");
	 * }
	 * printDeckSizesSinglePlayerGuest();
	 * }
	 * 
	 * // Checking for the gameOver part
	 * if (humanGuestPlayer.getDeck().isEmpty()) {
	 * printDeckSizesSinglePlayerGuest();
	 * System.out.println("Computer Wins !!!");
	 * break;
	 * } else if (computerPlayer.getDeck().isEmpty()) {
	 * printDeckSizesSinglePlayerGuest();
	 * System.out.println("Player Wins !!!");
	 * break;
	 * }
	 * }
	 * }
	 * public void twoPlayersGameGuest(){
	 * humanGuestPlayer.setDeck(user1Cards);
	 * 
	 * this.guestPlayer=new GuestPlayer();
	 * guestPlayer.setDeck(user2Cards);
	 * 
	 * System.out.println("WELCOME TO THE TWO PLAYERS GAME AS A GUEST");
	 * System.out.println("THE GAME STARTS");
	 * 
	 * while (true) {
	 * System.out.println("Click 'A' to draw a card.. (Click 'Q' to quit)");
	 * String optionPlayer1 = scan.nextLine();
	 * while (!optionPlayer1.equals("A")) {
	 * if(optionPlayer1.equals("Q")){
	 * return;
	 * }
	 * System.out.println("Please click only 'A'");
	 * optionPlayer1 = scan.nextLine();
	 * }
	 * humanGuestPlayer.setReady(true);
	 * 
	 * 
	 * System.out.println("Click 'L' to draw a card.. ");
	 * String optionPlayer2 = scan.nextLine();
	 * while (!optionPlayer2.equals("L")) {
	 * System.out.println("Please click only 'L'");
	 * optionPlayer2 = scan.nextLine();
	 * }
	 * guestPlayer.setReady(true);
	 * 
	 * // assigning them to the card objects
	 * Card playerCard = humanGuestPlayer.getFirstCardOfDeck();
	 * Card guestCard = guestPlayer.getFirstCardOfDeck();
	 * 
	 * System.out.print("Player 1 -> " ); playerCard.printCardInfo();
	 * System.out.print("Guest Player (2) Card -> "); guestCard.printCardInfo();
	 * 
	 * int a = playerCard.getRank();
	 * int b = guestCard.getRank();
	 * 
	 * if (a == b) {
	 * System.out.println("------------------- War Begins --------------------");
	 * // When the cards tied, remove them before go to the war method
	 * humanGuestPlayer.removeCardFromDeck(playerCard);
	 * guestPlayer.removeCardFromDeck(guestCard);
	 * warTwoPlayersGuest(playerCard,guestCard);
	 * System.out.println("------------------- War Ends --------------------");
	 * printDeckSizesSinglePlayerGuest();
	 * } else {
	 * // First remove both cards from their deck
	 * humanGuestPlayer.removeCardFromDeck(playerCard);
	 * guestPlayer.removeCardFromDeck(guestCard);
	 * 
	 * if (a > b) {
	 * // If player wins take both cards
	 * humanGuestPlayer.addCardToDeck(playerCard);
	 * humanGuestPlayer.addCardToDeck(guestCard);
	 * System.out.println("Player 1 wins the battle");
	 * } else {
	 * // If computer wins take both cards
	 * guestPlayer.addCardToDeck(playerCard);
	 * guestPlayer.addCardToDeck(guestCard);
	 * System.out.println("Guest Player (2) wins the battle");
	 * }
	 * printDeckSizesTwoPlayersGuest();
	 * }
	 * 
	 * // Checking for the gameOver part
	 * if (humanGuestPlayer.getDeck().isEmpty()) {
	 * printDeckSizesTwoPlayersGuest();
	 * System.out.println("Guest Player (2) Wins !!!");
	 * break;
	 * } else if (guestPlayer.getDeck().isEmpty()) {
	 * printDeckSizesTwoPlayersGuest();
	 * System.out.println("Player 1 Wins !!!");
	 * break;
	 * }
	 * }
	 * }
	 */ // method for playing game in terminal
	/*
	 * public void printDeckSizesSinglePlayer() {
	 * int size1 = humanPlayer.getDeck().size();
	 * int size2 = computerPlayer.getDeck().size();
	 * System.out.println("→ Deck Sizes: Player = " + size1 + " cards, Computer = "
	 * + size2 + " cards");
	 * }
	 * 
	 * 
	 * public void printDeckSizesSinglePlayerGuest(){
	 * int size1 = humanGuestPlayer.getDeck().size();
	 * int size2 = computerPlayer.getDeck().size();
	 * System.out.println("→ Deck Sizes: Player = " + size1 + " cards, Computer = "
	 * + size2 + " cards");
	 * }
	 * 
	 * 
	 * public void printDeckSizesTwoPlayers(String username) {
	 * int size1 = humanPlayer.getDeck().size();
	 * int size2 = guestPlayer.getDeck().size();
	 * System.out.println("→ Deck Sizes: Player '"+username+"' = " + size1 +
	 * " cards, Guest Player = " + size2 + " cards");
	 * }
	 * 
	 * public void printDeckSizesTwoPlayersGuest() {
	 * int size1 = humanGuestPlayer.getDeck().size();
	 * int size2 = guestPlayer.getDeck().size();
	 * System.out.println("→ Deck Sizes: Player 1 = " + size1 +
	 * " cards, Player 2 = " + size2 + " cards");
	 * }
	 * 
	 * 
	 */ // method for playing game in terminal

}
