package edu.up.cs301.guillotine.guillotine;

/**
 * A player who plays a game. Each class that implements a player for
 * a particular game should implement this interface.
 *
 * @author Muhammed Acar
 * @author Linnea Bair
 * @author Melanie Martinell
 * @author Nathan Schmedake
 * @version November 2015
 */

public interface GamePlayer {

	//takes a turn, which should also involve playing a card.
	public GuillotineState takeTurn(GuillotineState state);
	//plays a card, as decided by the class
	public GuillotineState playCard(GuillotineState state);

}// interface GamePlayer
