package edu.up.cs301.guillotine.guillotine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/**
 * This contains the state for the Guillotine game. The state consists of all card decks and hands
 * as well as other necessary variables for the game
 * 
 * @author Nathan Schmedake
 * @author Muhammed Acar
 * @author Melanie Martinell
 * @author Linnea Bair
 * @version November 2015
 */
public class GuillotineState implements Serializable {

	// to satisfy Serializable interface
	private static final long serialVersionUID = 7737393762469851826L;

	// the instance variables needed to be saved
	private int numPlayers = 1;
	private String username;
	private int difficulty;
	private int day;
	private int currentPlayer;
	private boolean gameOver = false;
	private String message = "";
	private boolean nobleCollectOk = true;
	private boolean playSecondAction = false;
	private boolean needPopUp = false;
	private int popUpType = -1;
	private int intPopUpRecievedInfo = -1;

	private ActionCard actionChosen = null;

	private int humanPlayerScore;
	private int computerPlayer1Score;
	private int computerPlayer2Score;
	private int computerPlayer3Score;

	//these cards need to be kept track of, and will be -1 until a player takes posession of them
	private int hasCivicSupport = -1;
	private int hasMilitarySupport = -1;
	private int hasChurchSupport = -1;
	private int hasIndifferentPublic = -1;
	private int hasFountainOfBlood = -1;
	private int hasForeignSupport = -1;
	private int hasToughCrowd = -1;


	//all the decks and nobles, as well as the players' hands and score piles
	ArrayList<Noble> nobleDeck;
	ArrayList<ActionCard> actionDeck;
	ArrayList<Noble> nobleDiscard;
	ArrayList<ActionCard> actionDiscard;
	ArrayList<Noble> deathRow;
	ArrayList<ActionCard> humanPlayerHand;
	ArrayList<ActionCard> computerPlayer1Hand;
	ArrayList<ActionCard> computerPlayer2Hand;
	ArrayList<ActionCard> computerPlayer3Hand;
	ArrayList<Noble> humanPlayerNobles;
	ArrayList<Noble> computerPlayer1Nobles;
	ArrayList<Noble> computerPlayer2Nobles;
	ArrayList<Noble> computerPlayer3Nobles;


	/**
	 * constructor, initializing the counter value from the parameter
	 *
	 * @param numCompPlayers the number of computer players selected
	 * @param name the name the player wants to appear by them
	 * @param easyHard the difficulty that the human player has selected, 0 for easy, 1 for hard
	 */
	public GuillotineState(int numCompPlayers, String name, int easyHard) {
		// initialize the number of players, name, and difficulty
		day = 1;
		numPlayers += numCompPlayers;
		username = name;
		difficulty = easyHard;
		currentPlayer = (int)(Math.random()*numPlayers); //randomize the starting player

		//initialize scores
		humanPlayerScore = 0;
		computerPlayer1Score = 0;
		computerPlayer2Score=0;
		computerPlayer3Score=0;

		//create the decks and shuffle them
		BuildNobleDeck build1 = new BuildNobleDeck();
		nobleDeck = new ArrayList<Noble>(Arrays.asList(build1.getNobleList()));
		shuffleNobleDeck();
		nobleDiscard = new ArrayList<Noble>();
		BuildActionDeck build2 = new BuildActionDeck();
		actionDeck = new ArrayList<ActionCard>(Arrays.asList(build2.getActionList()));
		shuffleActionDeck();
		actionDiscard = new ArrayList<ActionCard>();
		deathRow = new ArrayList<Noble>();
		humanPlayerHand = new ArrayList<ActionCard>();
		humanPlayerNobles = new ArrayList<Noble>();
		if(numCompPlayers >= 1){
			computerPlayer1Hand = new ArrayList<ActionCard>();
			computerPlayer1Nobles = new ArrayList<Noble>();
		}
		if(numCompPlayers >=2){
			computerPlayer2Hand = new ArrayList<ActionCard>();
			computerPlayer2Nobles = new ArrayList<Noble>();
		}
		if(numCompPlayers >=3){
			computerPlayer3Hand = new ArrayList<ActionCard>();
			computerPlayer3Nobles = new ArrayList<Noble>();
		}
		this.createDeathRow(12); //deal the noble line
		this.deal(); //deal each player's hand
	}

	/**
	 * This method deals out the 12 cards in the noble line
	 */
	public void createDeathRow(int numNobles) {
		if (nobleDeck.size() != 0) {
			for (int i = 0; i < numNobles; i++) {
				deathRow.add(nobleDeck.get(0));
				nobleDeck.remove(0);
			}
		}
	}

	/**
	 * Removes a noble from the line
	 * @param nobleNum
	 */
	public void removeFromDeathRow(int nobleNum) {
		deathRow.remove(nobleNum);
	}

	/**
	 * Shuffles the noble line
	 */
	public void shuffleDeathRow() {
		Collections.shuffle(deathRow);
	}

	/**
	 * Moves a noble card a certain number of places in line
	 * @param initLocation
	 * @param numPlaces
	 */
	public void moveExactly(int initLocation, int numPlaces) {
		Noble card = deathRow.get(initLocation);
		deathRow.remove(initLocation);
		deathRow.add(initLocation - numPlaces, card);
	}

	/**
	 * Shuffles action card deck
	 */
	public void shuffleActionDeck(){
		Collections.shuffle(actionDeck);
	}

	/**
	 * Shuffles noble card deck
	 */
	public void shuffleNobleDeck(){
		Collections.shuffle(nobleDeck);
	}

	/**
	 * Allows access of the human player's hand
	 * @return
	 */
	public ArrayList<ActionCard> getHumanPlayerHand() {
		return humanPlayerHand;
	}

	/**
	 * Allows access of a computer player's hand
	 * @return
	 */
	public ArrayList<ActionCard> getComputerPlayer1Hand() {
		return computerPlayer1Hand;
	}

	/**
	 * Allows access of a computer player's hand
	 * @return
	 */
	public ArrayList<ActionCard> getComputerPlayer2Hand() {
		return computerPlayer2Hand;
	}

	/**
	 * Allows access of a computer player's hand
	 * @return
	 */
	public ArrayList<ActionCard> getComputerPlayer3Hand() {
		return computerPlayer3Hand;
	}

	/**
	 * Allows access of the human player's score pile
	 * @return
	 */
	public ArrayList<Noble> getHumanPlayerNobles() {
		return humanPlayerNobles;
	}

	/**
	 * Allows access of a computer player's score pile
	 * @return
	 */
	public ArrayList<Noble> getComputerPlayer1Nobles() {
		return computerPlayer1Nobles;
	}

	/**
	 * Allows access of a computer player's score pile
	 * @return
	 */
	public ArrayList<Noble> getComputerPlayer2Nobles() {
		return computerPlayer2Nobles;
	}

	/**
	 * Allows access of a computer player's score pile
	 * @return
	 */
	public ArrayList<Noble> getComputerPlayer3Nobles() {
		return computerPlayer3Nobles;
	}

	/**
	 * Allows access of noble deck
	 * @return
	 */
	public ArrayList<Noble> getNobleDeck() {
		return nobleDeck;
	}

	/**
	 * Allows access of noble discard pile
	 * @return
	 */
	public ArrayList<Noble> getNobleDiscard() {
		return nobleDiscard;
	}

	/**
	 * Allows access of action deck
	 * @return
	 */
	public ArrayList<ActionCard> getActionDeck() {
		return actionDeck;
	}

	/**
	 * Allows access of action discard pile
	 * @return
	 */
	public ArrayList<ActionCard> getActionDiscard() {
		return actionDiscard;
	}

	/**
	 * Allows other classes to set the human player's hand
	 * @param hand
	 */
	public void setHumanPlayerHand(ArrayList<ActionCard> hand) {
		humanPlayerHand = hand;
	}

	/**
	 * Allows other classes to set a computer player's hand
	 * @param hand
	 */
	public void setComputerPlayer1Hand(ArrayList<ActionCard> hand) {
		computerPlayer1Hand = hand;
	}

	/**
	 * Allows other classes to set a computer player's hand
	 * @param hand
	 */
	public void setComputerPlayer2Hand(ArrayList<ActionCard> hand) {
		computerPlayer2Hand = hand;
	}

	/**
	 * Allows other classes to set a computer player's hand
	 * @param hand
	 */
	public void setComputerPlayer3Hand(ArrayList<ActionCard> hand) {
		computerPlayer3Hand = hand;
	}

	/**
	 * Allows other classes to set the human player's score pile
	 * @param nobles
	 */
	public void setHumanPlayerNobles(ArrayList<Noble> nobles) {
		humanPlayerNobles = nobles;
	}

	/**
	 * Allows other classes to set a computer player's score pile
	 * @param nobles
	 */
	public void setComputerPlayer1Nobles(ArrayList<Noble> nobles) {
		computerPlayer1Nobles = nobles;
	}

	/**
	 * Allows other classes to set a computer player's score pile
	 * @param nobles
	 */
	public void setComputerPlayer2Nobles(ArrayList<Noble> nobles) {
		computerPlayer2Nobles = nobles;
	}

	/**
	 * Allows other classes to set a computer player's score pile
	 * @param nobles
	 */
	public void setComputerPlayer3Nobles(ArrayList<Noble> nobles) {
		computerPlayer3Nobles = nobles;
	}

	/**
	 * Allows other classes to set the noble deck
	 * @param nobles
	 */
	public void setNobleDeck(ArrayList<Noble> nobles) {
		nobleDeck = nobles;
	}

	/**
	 * Allows other classes to set the noble discard pile
	 * @param nobles
	 */
	public void setNobleDiscard(ArrayList<Noble> nobles) {
		nobleDiscard = nobles;
	}

	/**
	 * Allows other classes to set the action card deck
	 * @param actions
	 */
	public void setActionDeck(ArrayList<ActionCard> actions) {
		actionDeck = actions;
	}

	/**
	 * Allows other classes to set the action card discard pile
	 * @param actions
	 */
	public void setActionDiscard(ArrayList<ActionCard> actions) {
		actionDiscard = actions;
	}

	/**
	 * Allows other classes to get the day
	 * @return
	 */
	public int getDay() {
		return day;
	}

	/**
	 * Allows other classes to set the day
	 * @param whichDay
	 */
	public void setDay(int whichDay){
		day = whichDay;
	}

	/**
	 * Get the current player
	 * @return
	 */
	public int getCurrentPlayer() {return currentPlayer;}

	/**
	 * increment the current player, as it will go in order
	 */
	public void setCurrentPlayer(){
		//roll over if the current player is the highest number player
		if(currentPlayer == numPlayers-1){
			currentPlayer = 0;
		}
		else{
			currentPlayer++;
		}
	}

	/**
	 * Deals five action cards to each player that is playing
	 */
	public void deal(){
		//
		for (int i = 0; i < 5; i++) {
			humanPlayerHand.add(actionDeck.get(0));
			actionDeck.remove(0);
		}

		//check for number of players and deal their hands
		if(computerPlayer1Hand != null){
			for (int i = 0; i < 5; i++) {

				if(actionDeck.size()==0){
					actionDeck.addAll(actionDiscard);
				}
				computerPlayer1Hand.add(actionDeck.get(0));
				actionDeck.remove(0);
			}
		}
		if(computerPlayer2Hand != null){
			for (int i = 0; i < 5; i++) {

				if(actionDeck.size()==0){
					actionDeck.addAll(actionDiscard);
				}
				computerPlayer2Hand.add(actionDeck.get(0));
				actionDeck.remove(0);
			}
		}
		if(computerPlayer3Hand != null){
			for (int i = 0; i < 5; i++) {

				if(actionDeck.size()==0){
					actionDeck.addAll(actionDiscard);
				}
				computerPlayer3Hand.add(actionDeck.get(0));
				actionDeck.remove(0);
			}
		}
	}

	//draws an action card for the given player
	public void drawActionCard(int playerIndex){
		if (actionDeck.size() != 0) {
			ActionCard card = actionDeck.get(0);
			actionDeck.remove(0);
			if (playerIndex == 0) {
				humanPlayerHand.add(card);
			}
			else if (playerIndex == 1) {
				computerPlayer1Hand.add(card);
			}
			else if (playerIndex == 2) {
				computerPlayer2Hand.add(card);
			}
			else if (playerIndex == 3) {
				computerPlayer3Hand.add(card);
			}
		}
	}

	//collects the noble for the given player
	public void collectNoble(int playerIndex){
		if(nobleCollectOk) {
			if (deathRow.size() != 0) {
				Noble card = deathRow.get(0);
				playerIndex = card.nobleAction(this);
				deathRow.remove(0);
				if (playerIndex == 0) {
					humanPlayerNobles.add(card);
				} else if (playerIndex == 1) {
					computerPlayer1Nobles.add(card);
				} else if (playerIndex == 2) {
					computerPlayer2Nobles.add(card);
				} else if (playerIndex == 3) {
					computerPlayer3Nobles.add(card);
				}

				if(playerIndex==0){
					addToMessage("You received " + card.getNobleName() + ". ");
				}
				else {
					addToMessage("Player " + (playerIndex + 1) + " received " + card.getNobleName() + ". ");
				}
			}
			if (deathRow.size() == 0) {
				if (day == 3) {
					gameOver = true;

				} else {
					day++;
					createDeathRow(12);
				}

			}
		}
		nobleCollectOk = true;
	}

	//check to see if the game is over
	public boolean isGameOver(){
		return gameOver;
	}

	//more getters and setters
	public int getHumanPlayerScore(){
		return humanPlayerScore;
	}
	public int getComputerPlayer1Score(){
		return computerPlayer1Score;
	}
	public int getComputerPlayer2Score(){
		return computerPlayer2Score;
	}
	public int getComputerPlayer3Score(){
		return computerPlayer3Score;
	}
	public void addNobleScoreHuman(int newScore){
		humanPlayerScore += newScore;
	}
	public void setDeathRow(ArrayList<Noble> dr){ deathRow = dr;}
	public String getUsername(){
		return username;
	}

	public int getNumPlayers(){
		return numPlayers;
	}
	public ArrayList<Noble> getDeathRow(){return deathRow;}
	public void setHasForeignSupport(int player){
		hasForeignSupport = player;
	}
	public void setHasChurchSupport(int player){
		hasChurchSupport = player;
	}
	public void setHasCivicSupport(int player){
		hasCivicSupport = player;
	}
	public void setHasMilitarySupport(int player){
		hasMilitarySupport = player;
	}
	public void setHasFountainOfBlood(int player){
		hasFountainOfBlood = player;
	}
	public void setHasIndifferentPublic(int player){
		hasIndifferentPublic = player;
	}
	public void setHasToughCrowd(int player) { hasToughCrowd = player; }

	/*
	 * This method calculates all the scores for each player, based on all criteria for scoring.
	 */
	public void calculateScores() {
		int currentScore = 0;
		int numGuards = 0;
		int countCountess = 0;
		if (numPlayers >= 2) {
			//calculate human player's score, looking through entire score pile
			for (int i = 0; i < humanPlayerNobles.size(); i++) {
				if (humanPlayerNobles.get(i).getNobleColor().equals("blue") && hasChurchSupport == 0){
					currentScore++;
				}
				if (humanPlayerNobles.get(i).getNobleColor().equals("green") && hasCivicSupport == 0){
					currentScore++;
				}
				if (humanPlayerNobles.get(i).getNobleColor().equals("red") && hasMilitarySupport == 0){
					currentScore++;
				}
				if (hasFountainOfBlood == 0){
					currentScore+=2;
				}
				if (hasToughCrowd == 0){
					currentScore-=2;
				}
				if (humanPlayerNobles.get(i).getNobleName().equals("Palace Guard")) {
					numGuards++;
					currentScore += numGuards;
				}
				else if (humanPlayerNobles.get(i).getNobleName().equals("Count") || humanPlayerNobles.get(i).getNobleName().equals("Countess")) {
					countCountess++;
					if (countCountess == 2) {
						currentScore += 4;
					} else {
						currentScore += 2;
					}
				}
				else if(humanPlayerNobles.get(i).getNobleColor().equals("gray") && hasIndifferentPublic == 0){
					currentScore++;
				}
				//else {
					currentScore += humanPlayerNobles.get(i).getNoblePoints();
				//}

			}
			//set human player score to the calculated score
			humanPlayerScore = currentScore;

			//reset variables to 0
			countCountess = 0;
			numGuards = 0;
			currentScore = 0;

			//calculate computer player 1's score, looking through their entire score pile
			for (int i = 0; i < computerPlayer1Nobles.size(); i++) {
				if (computerPlayer1Nobles.get(i).getNobleColor().equals("blue") && hasChurchSupport == 1){
					currentScore++;
				}
				if (computerPlayer1Nobles.get(i).getNobleColor().equals("green") && hasCivicSupport == 1){
					currentScore++;
				}
				if (computerPlayer1Nobles.get(i).getNobleColor().equals("red") && hasMilitarySupport == 1){
					currentScore++;
				}
				if (hasFountainOfBlood == 1){
					currentScore+=2;
				}
				if (hasToughCrowd == 1){
					currentScore-=2;
				}
				if (computerPlayer1Nobles.get(i).getNobleName().equals("Palace Guard")) {
					numGuards++;
					currentScore += numGuards;
				}
				else if (computerPlayer1Nobles.get(i).getNobleName().equals("Count") || computerPlayer1Nobles.get(i).getNobleName().equals("Countess")) {
					countCountess++;
					if (countCountess == 2) {
						currentScore += 4;
					} else {
						currentScore += 2;
					}
				}
				else if(computerPlayer1Nobles.get(i).getNobleColor().equals("gray") && hasIndifferentPublic == 1){
					currentScore++;
				}
				else {
					currentScore += computerPlayer1Nobles.get(i).getNoblePoints();
				}

			}
			//set computer player 1 score to the calculated score
			computerPlayer1Score = currentScore;

			//reset variables to 0
			countCountess = 0;
			numGuards = 0;
			currentScore = 0;
		}
		if (numPlayers >= 3) {
			//calculate second computer player's score, looking through their entire score pile, if it exists
			for (int i = 0; i < computerPlayer2Nobles.size(); i++) {
				if (computerPlayer2Nobles.get(i).getNobleColor().equals("blue") && hasChurchSupport == 2){
					currentScore++;
				}
				if (computerPlayer2Nobles.get(i).getNobleColor().equals("green") && hasCivicSupport == 2){
					currentScore++;
				}
				if (computerPlayer2Nobles.get(i).getNobleColor().equals("red") && hasMilitarySupport == 2){
					currentScore++;
				}
				if (hasFountainOfBlood == 2){
					currentScore+=2;
				}
				if (hasToughCrowd == 2){
					currentScore-=2;
				}
				if (computerPlayer2Nobles.get(i).getNobleName().equals("Palace Guard")) {
					numGuards++;
					currentScore += numGuards;
				}
				else if (computerPlayer2Nobles.get(i).getNobleName().equals("Count") || computerPlayer2Nobles.get(i).getNobleName().equals("Countess")) {
					countCountess++;
					if (countCountess == 2) {
						currentScore += 4;
					} else {
						currentScore += 2;
					}
				}
				else if(computerPlayer2Nobles.get(i).getNobleColor().equals("gray") && hasIndifferentPublic == 2){
					currentScore++;
				}
				else {
					currentScore += computerPlayer2Nobles.get(i).getNoblePoints();
				}

			}
			//set computer player 1 score to the calculated score
			computerPlayer2Score = currentScore;

			//reset variables to 0
			countCountess = 0;
			numGuards = 0;
			currentScore = 0;
		}

		//calculate third computer player's score if it exists
		if (numPlayers >= 4) {
			//calculate computer player 1's score, looking through their entire score pile
			for (int i = 0; i < computerPlayer3Nobles.size(); i++) {
				if (computerPlayer3Nobles.get(i).getNobleColor().equals("blue") && hasChurchSupport == 3){
					currentScore++;
				}
				if (computerPlayer3Nobles.get(i).getNobleColor().equals("green") && hasCivicSupport == 3){
					currentScore++;
				}
				if (computerPlayer3Nobles.get(i).getNobleColor().equals("red") && hasMilitarySupport == 3){
					currentScore++;
				}
				if (hasFountainOfBlood == 3){
					currentScore+=2;
				}
				if (hasToughCrowd == 3){
					currentScore-=2;
				}
				if (computerPlayer3Nobles.get(i).getNobleName().equals("Palace Guard")) {
					numGuards++;
					currentScore += numGuards;
				}
				else if (computerPlayer3Nobles.get(i).getNobleName().equals("Count") || computerPlayer3Nobles.get(i).getNobleName().equals("Countess")) {
					countCountess++;
					if (countCountess == 2) {
						currentScore += 4;
					} else {
						currentScore += 2;
					}
				}
				else if(computerPlayer3Nobles.get(i).getNobleColor().equals("gray") && hasIndifferentPublic == 3){
					currentScore++;
				}
				else {
					currentScore += computerPlayer3Nobles.get(i).getNoblePoints();
				}

			}
			//set computer player 1 score to the calculated score
			computerPlayer3Score = currentScore;

			//reset variables to 0
			countCountess = 0;
			numGuards = 0;
			currentScore = 0;
		}

	}
	//A variety of getters and setters for variables in here.
	public void addToMessage(String addition){
		message = message + " " + addition;
	}
	public String getMessage(){
		return message;
	}
	public void resetMessage(){
		message = "";
	}
	public void setNobleCollectOk(boolean ok){
		nobleCollectOk = ok;
	}
	public boolean getPlaySecondAction(){
		return playSecondAction;
	}
	public void setPlaySecondAction(boolean ok) {
		playSecondAction = ok;
	}
	public void setNeedPopUp(boolean yes){
		needPopUp = yes;
	}
	public boolean getNeedPopUp(){
		return needPopUp;
	}
	public void setPopUpType(int type){
		popUpType = type;
	}
	public int getPopUpType(){
		return popUpType;
	}
	public void setIntPopUpRecievedInfo(int recievedInfo){
		intPopUpRecievedInfo = recievedInfo;
	}
	public int getIntPopUpRecievedInfo(){
		return intPopUpRecievedInfo;
	}
	public void setActionCardChosen(ActionCard card){
		actionChosen = card;
	}
	public ActionCard getActionChosen(){
		return actionChosen;
	}
}