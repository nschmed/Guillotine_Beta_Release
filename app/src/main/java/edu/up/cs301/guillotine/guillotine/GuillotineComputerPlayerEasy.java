package edu.up.cs301.guillotine.guillotine;

import java.util.ArrayList;

/**
 * This is a computer player that is set to easy, will take a turn and play a card.
 *
 * @author Muhammed Acar
 * @author Linnea Bair
 * @author Melanie Martinell
 * @author Nathan Schmedake
 * @version December 2015
 */
public class GuillotineComputerPlayerEasy extends GameComputerPlayer implements GamePlayer {

    /**
     * Constructor for objects of class GuillotineComputerPlayerEasy
     */
    public GuillotineComputerPlayerEasy(){
    }

    /*
     * This takes a turn for a given computer player.
     * @param state
     *      This state shows whose turn it is and what all the variables are set to.
     */
    public GuillotineState takeTurn(GuillotineState state){
        state = playCard(state); //play a card
        //if a second card can be played, play it
        if (state.getPlaySecondAction()) {
            playCard(state);
            state.setPlaySecondAction(false); //reset the variable
        }
        state.collectNoble(state.getCurrentPlayer()); //collect a noble
        state.drawActionCard(state.getCurrentPlayer()); //draw an action card
        state.setCurrentPlayer(); // increment the current player
        return state;
    }

    @Override
    /*
     * This plays a card for a given computer player.
     * @param state
     *      This state shows whose turn it is and what all the variables are set to.
     */
    public GuillotineState playCard(GuillotineState state) {
        //Decides whose turn it is, and by that plays the card as necessary.
        //Displays what card has been played
        if (state.getCurrentPlayer() == 1){
            ArrayList<ActionCard> hand = state.getComputerPlayer1Hand();
            int handSize = hand.size();

            int rand = (int)(Math.random()*handSize);//randomly play a card
            ActionCard playingCard = hand.get(rand);
            state.addToMessage("\nPlayer 2 played " + playingCard.getName() + ".");
            hand.remove(rand); //remove the card that has been played.
            playingCard.easyAIAction(state);
            state.setComputerPlayer1Hand(hand);
        }
        //players 2 and 3 are the same as 1.
        else if(state.getCurrentPlayer() == 2) {
            ArrayList<ActionCard> hand = state.getComputerPlayer2Hand();
            int handSize = hand.size();

            int rand = (int)(Math.random()*handSize);
            ActionCard playingCard = hand.get(rand);
            state.addToMessage("\nPlayer 3 played " + playingCard.getName() + ".");
            hand.remove(rand);
            playingCard.easyAIAction(state);
            state.setComputerPlayer2Hand(hand);
        }
        else if(state.getCurrentPlayer() == 3) {
            ArrayList<ActionCard> hand = state.getComputerPlayer3Hand();
            int handSize = hand.size();

            int rand = (int)(Math.random()*handSize);
            ActionCard playingCard = hand.get(rand);
            state.addToMessage("\nPlayer 4 played " + playingCard.getName() + ".");
            hand.remove(rand);
            playingCard.easyAIAction(state);
            state.setComputerPlayer3Hand(hand);
        }
        return state;
    }
}
