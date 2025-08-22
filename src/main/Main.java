package main;

import Gui.MainMenu;
import Gui.SoundPlayer;

public class Main {
	public static void main(String[] args) {
		SoundPlayer backgroundMusic = new SoundPlayer();
		backgroundMusic.loadAndLoop("/music/background.wav");
		// Playing game with GUI
		new MainMenu();

		// new MenuLogic().mainMenu(); playing game in terminal

	}
}