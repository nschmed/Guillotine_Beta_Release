package edu.up.cs301.guillotine.guillotine;

import android.view.View;
import android.view.View.OnClickListener;

import edu.up.cs301.guillotine.R;

/**
 * This class describes what a human player can do/how it works
 *
 * @author Muhammed Acar
 * @author Linnea Bair
 * @author Melanie Martinell
 * @author Nathan Schmedake
 * @version November 2015
 */
public class GuillotineHumanPlayer implements GamePlayer {

	/* instance variables */
	// the most recent game state
	private GuillotineState state;

	private String playerName;
	
	/**
	 * constructor
	 * @param name
	 * 		the player's name
	 */
	public GuillotineHumanPlayer(String name) {
		playerName = name;
	}


	@Override
	public GuillotineState takeTurn(GuillotineState state) {
		return null;
	}

	@Override
	public GuillotineState playCard(GuillotineState state) {
		return null;
	}
}// class GuillotineHumanPlayer

