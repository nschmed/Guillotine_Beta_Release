package edu.up.cs301.guillotine.guillotine;

import java.util.ArrayList;

/**
 * This is a computer player that is set to hard, will take a turn and play a card.
 *
 * @author Muhammed Acar
 * @author Linnea Bair
 * @author Melanie Martinell
 * @author Nathan Schmedake
 * @version November 2015
 */
public class GuillotineComputerPlayerHard extends GameComputerPlayer implements GamePlayer {

	/**
	 * Constructor for objects of class GuillotineComputerPlayerHard
	 */
	public GuillotineComputerPlayerHard() {

	}

	/*
     * This takes a turn for a given computer player.
     * @param state
     *      This state shows whose turn it is and what all the variables are set to.
     */
	@Override
	public GuillotineState takeTurn(GuillotineState state){
		state = playCard(state);//play a card
		//if a second card can be played, play it.
		if (state.getPlaySecondAction()) {
			playCard(state);
			state.setPlaySecondAction(false); //reset the variable
		}
		state.collectNoble(state.getCurrentPlayer()); //collect a noble
		state.drawActionCard(state.getCurrentPlayer()); //draw an action card
		state.setCurrentPlayer(); //increment the current player to change turns.
		return state;
	}

	@Override
	/*
     * This plays a card for a given computer player. It is currently fairly random for the hard player,
     * needs to be made more difficult
     * @param state
     *      This state shows whose turn it is and what all the variables are set to.
     */
	public GuillotineState playCard(GuillotineState state) {
		if (state.getCurrentPlayer() == 1){
			ArrayList<ActionCard> hand = state.getComputerPlayer1Hand();
			int handSize = hand.size();

			int rand = (int)(Math.random()*handSize);
			ActionCard playingCard = hand.get(rand);
			state.addToMessage("\nPlayer 2 played " + playingCard.getName() + ".");
			hand.remove(rand);
			playingCard.hardAIAction(state);
			state.setComputerPlayer1Hand(hand);
		}
		else if(state.getCurrentPlayer() == 2) {
			ArrayList<ActionCard> hand = state.getComputerPlayer2Hand();
			int handSize = hand.size();

			int rand = (int)(Math.random()*handSize);
			ActionCard playingCard = hand.get(rand);
			state.addToMessage("\nPlayer 3 played " + playingCard.getName() + ".");
			hand.remove(rand);
			playingCard.hardAIAction(state);
			state.setComputerPlayer2Hand(hand);
		}
		else if(state.getCurrentPlayer() == 3) {
			ArrayList<ActionCard> hand = state.getComputerPlayer3Hand();
			int handSize = hand.size();

			int rand = (int)(Math.random()*handSize);
			ActionCard playingCard = hand.get(rand);
			state.addToMessage("\nPlayer 4 played " + playingCard.getName() + ".");
			hand.remove(rand);
			playingCard.hardAIAction(state);
			state.setComputerPlayer3Hand(hand);
		}
		return state;
	}
}
