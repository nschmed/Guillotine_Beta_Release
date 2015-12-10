package edu.up.cs301.guillotine.guillotine;

import java.util.ArrayList;

/**
 * This is a computer player that will switch between easy and hard based on the constructor and play a card.
 *
 * @author Muhammed Acar
 * @author Linnea Bair
 * @author Melanie Martinell
 * @author Nathan Schmedake
 * @version November 2015
 */
public class GuillotineComputerPlayerEasy extends GameComputerPlayer implements GamePlayer {

    /**
     * Constructor for objects of class GuillotineComputerPlayerEasy
     */
    public GuillotineComputerPlayerEasy(){
    }

    public GuillotineState takeTurn(GuillotineState state){
        state = playCard(state);
        if (state.getPlaySecondAction()) {
            playCard(state);
            state.setPlaySecondAction(false);
        }
        state.collectNoble(state.getCurrentPlayer());
        state.drawActionCard(state.getCurrentPlayer());
        state.setCurrentPlayer();
        return state;
    }

    @Override
    public GuillotineState playCard(GuillotineState state) {
        if (state.getCurrentPlayer() == 1){
            ArrayList<ActionCard> hand = state.getComputerPlayer1Hand();
            int handSize = hand.size();

            int rand = (int)(Math.random()*handSize);
            ActionCard playingCard = hand.get(rand);
            state.addToMessage("\nPlayer 2 played " + playingCard.getName() + ".");
            hand.remove(rand);
            playingCard.easyAIAction(state);
            state.setComputerPlayer1Hand(hand);
        }
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
