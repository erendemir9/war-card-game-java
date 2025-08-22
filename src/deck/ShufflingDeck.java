
package deck;

import java.util.ArrayList;
import java.util.List;

public class ShufflingDeck {
	private List<Card> tempCards;
	private List<Card> shuffledCards;
	private List<Card> shuffledCardsLast;
	private List<Card> playerDeck;
	private List<Card> guestDeck;



	public ShufflingDeck() {
		CardCatalog catalog = new CardCatalog();
		tempCards = new ArrayList<>(catalog.getDefaultCards());
		shuffledCards = new ArrayList<>();
		shuffledCardsLast = new ArrayList<>();
		playerDeck = new ArrayList<>();
		guestDeck = new ArrayList<>();
		shuffleCards();
		shuffleCardsAgain();
		distributeShuffledCards();
	}


	public void shuffleCards() {
		int randNumber=(int) (Math.random()*52);
		List<Integer> randNumberList=new ArrayList<Integer> ();
		for(int i=0;i<tempCards.size();i++) {
			for(int y=0;y<52;y++) {
				if(randNumberList.contains(randNumber)) {
					randNumber=(int) (Math.random()*52);
					y--;
					continue;
				} // if finishes
				else {
					break;
				} // Else finishes
			}// Nested For Loop Finishes
			randNumberList.add(randNumber);
			shuffledCards.add(i,tempCards.get(randNumber));
			randNumber=(int) (Math.random()*52);
		}// First For Loop Finishes
	}


	public void shuffleCardsAgain() {
		int randNumber=(int) (Math.random()*52);
		List<Integer> randNumberList=new ArrayList<Integer> ();
		for(int i=0;i<tempCards.size();i++) {
			for(int y=0;y<52;y++) {
				if(randNumberList.contains(randNumber)) {
					randNumber=(int) (Math.random()*52);
					y--;
					continue;
				} // if finishes
				else {
					break;
				} // Else finishes
			}// Nested For Loop Finishes
			randNumberList.add(randNumber);
			shuffledCardsLast.add(i,shuffledCards.get(randNumber));
			randNumber=(int) (Math.random()*52);
		}// First For Loop Finishes
	}

	public void distributeShuffledCards() {
		int half = shuffledCardsLast.size() / 2;
		for (int i = 0; i < half; i++) {
			this.playerDeck.add(shuffledCardsLast.get(i));
		}
		for (int i = half; i < shuffledCardsLast.size(); i++) {
			this.guestDeck.add(shuffledCardsLast.get(i));
		}
	}

	public void printGameDeck() {
		System.out.println("Player 1:");
		printPlayerDeck();
		System.out.println("\nPlayer 2:");
		printGuestDeck();
	}


	public void printPlayerDeck() {
		for (Card c : playerDeck) c.printCardInfo();
	}
	public void printGuestDeck() {
		for (Card c : guestDeck) c.printCardInfo();
	}

	public List<Card> getShuffledCards() {
		return new ArrayList<>(shuffledCards);
	}

	public List<Card> getShuffledCardsLast() {
		return new ArrayList<>(shuffledCardsLast);
	}

	public void printShuffledCards() {
		for (Card c : shuffledCards) c.printCardInfo();
	}

	public void printLastShuffledCards() {
		for (Card c : shuffledCardsLast) c.printCardInfo();
	}

	public List<Card> getPlayerDeck() {
		return this.playerDeck;
	}

	public List<Card> getGuestDeck() {
		return this.guestDeck;
	}
}

