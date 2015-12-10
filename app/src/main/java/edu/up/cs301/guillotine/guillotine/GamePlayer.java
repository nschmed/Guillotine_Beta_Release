package edu.up.cs301.guillotine.guillotine;

/**
 * A player who plays a (generic) game. Each class that implements a player for
 * a particular game should implement this interface.
 *
 * @author Muhammed Acar
 * @author Linnea Bair
 * @author Melanie Martinell
 * @author Nathan Schmedake
 * @version November 2015
 */

public interface GamePlayer {

	//plays a card, as decided by the class
	public GuillotineState takeTurn(GuillotineState state);
	public GuillotineState playCard(GuillotineState state);

}// interface GamePlayer
