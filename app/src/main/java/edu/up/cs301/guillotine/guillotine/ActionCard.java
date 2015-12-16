package edu.up.cs301.guillotine.guillotine;

import android.app.Notification;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This defines an action card and how it can work relative to the gamestate
 *
 * @author Nathan Schmedake
 * @author Muhammed Acar
 * @author Melanie Martinell
 * @author Linnea Bair
 * @version December 2015
 */
public class ActionCard implements Serializable {
    private static final long serialVersionUID = 7L;

    private String name; // the name of the action card
    private int image; // the picture of the associated card

    /*
     * Constructor, assigning a name an a picture
     */
    public ActionCard(String givenName, int pic)
    {
        this.name = givenName;
        this.image = pic;
    }

    /*
     * Getter for the image
     */
    public int getImage(){
        return image;
    }

    /*
     * Getter for the name
     */
    public String getName() {return name; }

    /*
     * humanAction
     *      This method describes the action for every action card, testing for its name
     *      and then completing the action in conjunction with the game state.  For humans,
     *      it might be called twice, if the action requires another input.
     *
     * @param curState
     *      The current state of the game
     * @return curState
     *      The modified state of the game
     */
    public GuillotineState humanAction(GuillotineState curState){
        // make if statements for every action card

        double rand = Math.random();

        if(name.equals("After You....")){
            //Put the noble at the front of the line into another player's score pile
            //Creates a popup that allows the human player to choose a player to do this to.
            //Takes that information when the pop up info is received and modifies the game state.

            curState.setNeedPopUp(true);
            curState.setPopUpType(0); //0 = choose a player (number of players = number of buttons on pop up

            if(curState.getIntPopUpRecievedInfo()!=-1){

               // int playerChosen = curState.getIntPopUpRecievedInfo();

                if(curState.getIntPopUpRecievedInfo()==2){
                    curState.computerPlayer1Nobles.add(curState.getDeathRow().get(0));
                    curState.deathRow.remove(0);
                }
                if(curState.getIntPopUpRecievedInfo()==3){
                    curState.computerPlayer2Nobles.add(curState.getDeathRow().get(0));
                    curState.deathRow.remove(0);
                }
                if(curState.getIntPopUpRecievedInfo()==4){
                    curState.computerPlayer3Nobles.add(curState.getDeathRow().get(0));
                    curState.deathRow.remove(0);
                }

                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);

            }


        }
        else if (name.equals("Bribed Guards")){
            //Move the noble at the front of the line to the end of the line

            Noble firstNob = curState.deathRow.get(0);
            curState.addToMessage(firstNob.getNobleName() + " was moved to the end of the line. ");
            curState.removeFromDeathRow(0);
            curState.deathRow.add(firstNob);

        }
        else if (name.equals("Church Support")){
            //Put this card in front of you. It is worth +1 for each Blue noble in your score pile
            //Scoring is implemented in the calculateScore method of game state

            curState.setHasChurchSupport(curState.getCurrentPlayer());
            curState.addToMessage("You will receive +1 for each blue noble you collect. ");

        }
        else if (name.equals("Civic Pride")){
            //Move a Green noble forward up to 2 places in line
            //This method is implemented randomly for the human player.

            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i<deathRow.size();i++){
                if(deathRow.get(i).getNobleColor().equals("green")){
                    Noble card = deathRow.get(i);
                    deathRow.remove(i);
                    if(rand<0.5){
                        deathRow.add(i-1, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward one place. ");
                    }
                    else{
                        deathRow.add(i-2, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward two places. ");
                    }
                    curState.setDeathRow(deathRow);
                    break;
                }
            }

        }
        else if (name.equals("Civic Support")){
            //Play this card in front of you.  It is worth +1 point for each Green noble in your score pile.
            curState.setHasCivicSupport(curState.getCurrentPlayer());
            curState.addToMessage("You will receive +1 for each blue noble you collect. ");


        }
        else if (name.equals("Clothing Swap")){
            //Choose any noble in line and discard it.  Replace it with the top noble from the noble deck.
            //A popup allows the human player to choose a noble to do this to, then the game state is modified.
            //curState.resetMessage();
            curState.addToMessage("Please choose Noble to swap...");

            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble form line

            if(curState.getIntPopUpRecievedInfo()!=-1) {

                curState.nobleDiscard.add(curState.deathRow.get(curState.getIntPopUpRecievedInfo()));
                curState.deathRow.remove(curState.getIntPopUpRecievedInfo());
                curState.deathRow.add(curState.getIntPopUpRecievedInfo(),curState.nobleDeck.get(0));
                curState.nobleDeck.remove(0);

                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
            }

        }
        else if (name.equals("Confusion in Line")){
            //Choose a player.  Randomly rearrange the line just before that player collects his or her next noble.
            //This is implemented as rearranging the row before you play.

            curState.shuffleDeathRow();
            curState.addToMessage("Death Row was shuffled. ");

        }
        else if (name.equals("Double Feature")){
            //Collect an additional noble from the front of the line this turn.

            curState.collectNoble(0);
            curState.addToMessage("You get two nobles! ");
        }
        else if (name.equals("Escape!")){
            //Randomly choose 2 nobles in line and discard them.  Randomly rearrange
            //the remaining nobles in line.

            int randChosenNob1 = (int)(Math.random()*(curState.deathRow.size()));
            curState.nobleDiscard.add(curState.deathRow.get(randChosenNob1));
            curState.deathRow.remove(randChosenNob1);
            int randChosenNob2 = (int)(Math.random()*(curState.deathRow.size()));
            curState.nobleDiscard.add(curState.deathRow.get(randChosenNob2));
            curState.deathRow.remove(randChosenNob2);

            curState.shuffleDeathRow();
        }
        else if (name.equals("Extra Cart")){
            //Add 3 nobles from the noble deck to the end of the line
            //Adds 3 if line is 9 or less, 2 if line is 10, and 1 if line is 11 long
            if(curState.deathRow.size()<=9){
                for(int i =0;i<3;i++) {
                    curState.deathRow.add(curState.nobleDeck.get(0));
                    curState.nobleDeck.remove(0);
                }

            }
            else if(curState.deathRow.size()==10){
                for(int i =0;i<2;i++) {
                    curState.deathRow.add(curState.nobleDeck.get(0));
                    curState.nobleDeck.remove(0);
                }
            }
            else if(curState.deathRow.size()==11){
                curState.deathRow.add(curState.nobleDeck.get(0));
                curState.nobleDeck.remove(0);
            }
        }
        else if (name.equals("Fainting Spell")){
            //Move a noble backward up to 3 places in line
            //This is implemented randomly
            ArrayList<Noble> deathRow = curState.getDeathRow();
            if(deathRow.size()>=4) {
                int randIndex = (int) (Math.random() * (deathRow.size() - 3));
                double randSpaces = Math.random();
                String nobleName;
                if (randSpaces >= .33) {
                    nobleName = deathRow.get(randIndex).getNobleName();
                    deathRow.add((randIndex + 1), deathRow.get(randIndex));
                    deathRow.remove(randIndex);
                    curState.addToMessage(nobleName + " was moved backward one space. ");
                } else if (randSpaces > .33 && randSpaces < .67) {
                    nobleName = deathRow.get(randIndex).getNobleName();
                    deathRow.add((randIndex + 2), deathRow.get(randIndex));
                    deathRow.remove(randIndex);
                    curState.addToMessage(nobleName + " was moved backward two spaces. ");
                } else if (randSpaces >= .67) {
                    nobleName = deathRow.get(randIndex).getNobleName();
                    deathRow.add((randIndex + 3), deathRow.get(randIndex));
                    deathRow.remove(randIndex);
                    curState.addToMessage(nobleName + " was moved backward three spaces. ");
                }
            }
            curState.setDeathRow(deathRow);

        }
        else if (name.equals("Fled to England")){
            //Discard any noble in line
            //Allows you to choose a noble to discard

            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble form line

            if(curState.getIntPopUpRecievedInfo()!=-1) {
                curState.nobleDiscard.add(curState.deathRow.get(curState.getIntPopUpRecievedInfo()));
                curState.deathRow.remove(curState.getIntPopUpRecievedInfo());


                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
            }

        }
        else if (name.equals("Forced Break")){
            //All other players must discard an action card at random
            //Check is 2,3, or 4 players and has all choose a card to discard
            if(curState.getNumPlayers()==2){
                int rand1 = (int)(Math.random()*curState.computerPlayer1Hand.size());

                curState.actionDiscard.add(curState.computerPlayer1Hand.get(rand1));
                curState.computerPlayer1Hand.remove(rand1);
            }
            else if(curState.getNumPlayers()==3){
                int rand1 = (int)(Math.random()*curState.computerPlayer1Hand.size());
                int rand3 = (int)(Math.random()*curState.computerPlayer2Hand.size());

                curState.actionDiscard.add(curState.computerPlayer1Hand.get(rand1));
                curState.actionDiscard.add(curState.computerPlayer2Hand.get(rand3));
                curState.computerPlayer1Hand.remove(rand1);
                curState.computerPlayer2Hand.remove(rand3);
            }
            else if(curState.getNumPlayers()==4) {
                int rand1 = (int) (Math.random() * curState.computerPlayer1Hand.size());
                int rand3 = (int) (Math.random() * curState.computerPlayer2Hand.size());
                int rand4 = (int) (Math.random() * curState.computerPlayer3Hand.size());

                curState.actionDiscard.add(curState.computerPlayer1Hand.get(rand1));
                curState.actionDiscard.add(curState.computerPlayer2Hand.get(rand3));
                curState.actionDiscard.add(curState.computerPlayer3Hand.get(rand4));
                curState.computerPlayer1Hand.remove(rand1);
                curState.computerPlayer2Hand.remove(rand3);
                curState.computerPlayer3Hand.remove(rand4);
            }

        }
        else if (name.equals("Foreign Support")){
            //Put this card in front of you.  Draw an action card whenever you collect a purple noble

            curState.setHasForeignSupport(curState.getCurrentPlayer());
            curState.addToMessage("You will receive one action card for each purple noble you collect. ");
        }
        else if (name.equals("Forward March")){
            //Move a Palace Guard to the front of the line
            //Moves the first Palace Guard found to the front.

            ArrayList<Noble> deathRow = curState.getDeathRow();
            for(int i = 0; i < deathRow.size(); i++) {
                Noble card = deathRow.get(i);
                if (card.getNobleName().equals("Palace Guard")){
                    deathRow.add(0, card);
                    deathRow.remove(i);
                    curState.addToMessage("A Palace Guard was moved to the front of the line. ");
                    break;
                }
            }
            curState.setDeathRow(deathRow);

        }
        else if (name.equals("Fountain of Blood")){
            //Put this card in front of you.  It is worth 2 points.
            //Implements a change in a game state variable.

            curState.setHasFountainOfBlood(curState.getCurrentPlayer());
            curState.addToMessage(("You get two bonus points."));

        }
        else if (name.equals("Friend of the Queen")){
            //Move a noble backward up to 2 places in line.
            //Player chan choose card to move, number of spaces is random.

            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble form line

            if(curState.getIntPopUpRecievedInfo()!=-1) {

                Noble card = curState.deathRow.get(curState.getIntPopUpRecievedInfo());

                double randSpaces = Math.random();
                curState.deathRow.remove(curState.getIntPopUpRecievedInfo());
                if (randSpaces >= .5) {
                    curState.deathRow.add((curState.getIntPopUpRecievedInfo() + 1), card);
                    curState.addToMessage(card.getNobleName() + " was moved backward one space. ");
                } else if (randSpaces < .5) {
                    curState.deathRow.add((curState.getIntPopUpRecievedInfo() + 2), card);
                    curState.addToMessage(card.getNobleName() + " was moved backward two spaces. ");
                }
                curState.setDeathRow(curState.deathRow);

                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
            }

        }
        else if (name.equals("Ignoble Noble")){
            //Move a noble forward exactly 4 places.
            //Allows human to choose a noble to move, does not move if noble cannot move forward 4 spots
            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble form line

            if(curState.getIntPopUpRecievedInfo()!=-1) {
                ArrayList<Noble> deathRow = curState.getDeathRow();
                Noble card = curState.deathRow.get(curState.getIntPopUpRecievedInfo());
                int numCard = curState.getIntPopUpRecievedInfo();


                 if(curState.deathRow.size()>4) {

                    if(curState.getIntPopUpRecievedInfo()<5){
                        numCard= 5;
                        card = curState.deathRow.get(numCard);
                    }

                     deathRow.remove(numCard);
                     deathRow.add((numCard - 4), card);
                    curState.addToMessage(card.getNobleName() + " was moved forward four spaces. ");
               }

                curState.setDeathRow(deathRow);
                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
            }

        }
        else if (name.equals("Indifferent Public")){
            //Put this card in front of you. Any Gray nobles in your score pile are worth 1 instead
            //of their normal values.
            //Changes a gamestate variable to reflect this.  Scoring done in calculateScore method.

            curState.setHasIndifferentPublic(curState.getCurrentPlayer());
            curState.addToMessage("You will receive one point for each gray noble instead of its normal value. ");

        }
        else if (name.equals("Information Exchange")){
            //Trade hands with another player.
            //Popup allows a player to be chosen by human player to switch hands with

            curState.setNeedPopUp(true);
            curState.setPopUpType(0); //0 = choose a player (number of players = number of buttons on pop up

            if(curState.getIntPopUpRecievedInfo()!=-1){

                // int playerChosen = curState.getIntPopUpRecievedInfo();

                if(curState.getIntPopUpRecievedInfo()==2){
                    ArrayList<ActionCard> hand1 = curState.getComputerPlayer1Hand();
                    ArrayList<ActionCard> handHuman = curState.getHumanPlayerHand();

                    curState.computerPlayer1Hand = handHuman;
                    curState.humanPlayerHand = hand1;
                }
                if(curState.getIntPopUpRecievedInfo()==3){
                    ArrayList<ActionCard> hand2 = curState.getComputerPlayer2Hand();
                    ArrayList<ActionCard> handHuman = curState.getHumanPlayerHand();

                    curState.computerPlayer2Hand = handHuman;
                    curState.humanPlayerHand = hand2;
                }
                if(curState.getIntPopUpRecievedInfo()==4){
                    ArrayList<ActionCard> hand3 = curState.getComputerPlayer3Hand();
                    ArrayList<ActionCard> handHuman = curState.getHumanPlayerHand();

                    curState.computerPlayer3Hand = handHuman;
                    curState.humanPlayerHand = hand3;
                }


                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);

            }

        }
        else if (name.equals("Lack of Faith")){
            //If there are any Blue nobles in line, move the one nearest the front of the line to the front of the line.

            for(int i = 0; i<curState.deathRow.size();i++){
                if(curState.deathRow.get(i).getNobleColor().equals("blue")){
                    Noble card = curState.deathRow.get(i);
                    curState.deathRow.remove(i);
                    curState.deathRow.add(0,card);
                    curState.addToMessage(card.getNobleName() + " was moved to the front of the line. ");
                    break;
                }
            }

        }
        else if (name.equals("Late Arrival")){
            //Look at top 3 cards of the noble deck and add any one of them to the end of the line.
            //Melanie, how is this done?

            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble from line

            if(curState.getIntPopUpRecievedInfo()!=-1) {
                int num = curState.getIntPopUpRecievedInfo();

                if(num>2){
                    num =2;
                }

                int deathRowLast = curState.deathRow.size()-1;
                Noble numNob = curState.deathRow.get(num);

                curState.deathRow.remove(num);
                curState.deathRow.add(deathRowLast, numNob);

                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
            }

        }
        else if (name.equals("Let Them Eat Cake")){
            //If Marie Antoinette is in line, move her to the front of the line.
            //Checks for Marie Antoinette and moves her if she is there.

            ArrayList<Noble> deathRow = curState.getDeathRow();
            for(int i = 1; i < deathRow.size(); i++)
            {
                Noble card = deathRow.get(i);
                if (card.getNobleName().equals("Marie Antoinette"))
                {
                    deathRow.remove(i);
                    deathRow.add(0, card);
                    curState.addToMessage("Marie Antoinette was moved to the front of the line. ");
                    break;
                }
            }
            curState.setDeathRow(deathRow);

        }
        else if (name.equals("L'Idiot")){
            //Move a noble forward up to 2 places in line
            //Moves a noble of the user's choice one or two places forward in line randomly

            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble form line

            if(curState.getIntPopUpRecievedInfo()!=-1) {

                ArrayList<Noble> deathRow = curState.getDeathRow();

                Noble card = curState.deathRow.get(curState.getIntPopUpRecievedInfo());
                int numCard = curState.getIntPopUpRecievedInfo();

                if(deathRow.size()>2) {

                    if(curState.getIntPopUpRecievedInfo()<2){
                        numCard= 2;
                        card = deathRow.get(numCard);
                    }

                    double randSpaces = Math.random();
                    deathRow.remove(numCard);
                    if (randSpaces >= .5) {
                        deathRow.add((numCard - 1), card);
                        curState.addToMessage(card.getNobleName() + " was moved forward one space. ");
                    } else if (randSpaces < .5) {
                        deathRow.add((numCard - 2), card);
                        curState.addToMessage(card.getNobleName() + " was moved forward two spaces. ");
                    }
                }
                curState.setDeathRow(deathRow);

                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
            }

        }
        else if (name.equals("Majesty")){
            //Move a Purple noble forward up to 2 places in line.
            //This is implemented randomly.

            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i<deathRow.size();i++){
                if(deathRow.get(i).getNobleColor().equals("purple")){
                    Noble card = deathRow.get(i);
                    deathRow.remove(i);
                    if(rand<0.5){
                        deathRow.add(i-1, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward one place. ");
                    }
                    else{
                        deathRow.add(i-2, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward two places. ");
                    }
                    curState.setDeathRow(deathRow);
                    break;
                }
            }

        }
        else if (name.equals("Mass Confusion")){
            //Put all nobles in line in the noble deck.  Shuffle the noble deck and deal out the
            //same number of nobles in a new line.
            //This is done using a dummy arrayList

            ArrayList<Noble> deathRow = curState.getDeathRow();
            int numNobles = deathRow.size();
            ArrayList<Noble> currentNobleDeck = curState.getNobleDeck();

            int deathRowSize= deathRow.size();
            for (int i = 0; i < deathRowSize; i++) {
                Noble card = deathRow.get(0);
                deathRow.remove(0);
                currentNobleDeck.add(card);
            }
            curState.setNobleDeck(currentNobleDeck);
            curState.shuffleNobleDeck();
            curState.createDeathRow(numNobles);

        }
        else if (name.equals("Military Might")){
            //Move a Red noble forward up to 2 places in line.
            //This is implemented randomly.

            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i<deathRow.size();i++){
                if(deathRow.get(i).getNobleColor().equals("red")){
                    Noble card = deathRow.get(i);
                    deathRow.remove(i);
                    if(rand<0.5){
                        deathRow.add(i-1, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward one place. ");
                    }
                    else{
                        deathRow.add(i-2, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward two places. ");
                    }
                    curState.setDeathRow(deathRow);
                    break;
                }
            }

        }
        else if (name.equals("Military Support")){
            //Put this card in front of you.  It is worth +1 for each Red noble in your score pile.
            //This is implemented in the calculateScore method in gamestate.

            curState.setHasMilitarySupport(curState.getCurrentPlayer());
            curState.addToMessage("You will receive +1 for each red noble you collect. ");

        }
        else if (name.equals("Milling in Line")){
            //Randomly rearrange the first five nobles in line.
            //Creates a dummy line for the first five to shuffle and re-insert into the line.

            ArrayList<Noble> deathRow = curState.getDeathRow();
            ArrayList<Noble> firstFive = new ArrayList<Noble>();

            Noble one = deathRow.get(0);
            firstFive.add(one);

           if(curState.deathRow.size()>=2) {
               Noble two = deathRow.get(1);
               firstFive.add(two);
           }
            if(curState.deathRow.size()>=3) {
                Noble three = deathRow.get(2);
                firstFive.add(three);
            }
            if(curState.deathRow.size()>=4) {
                Noble four = deathRow.get(3);
                firstFive.add(four);
            }
            if(curState.deathRow.size()>=5) {
                Noble five = deathRow.get(4);
                firstFive.add(five);
            }

            Collections.shuffle(firstFive);

            int goThroughArray = 5;
            if(curState.deathRow.size()<5){
                goThroughArray = deathRow.size();
            }
            for(int i =0;i<goThroughArray;i++) {
                curState.removeFromDeathRow(0);
            }
            for(int i = 0;i<goThroughArray;i++){
                deathRow.add(i,firstFive.get(i));
            }

            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Missed!")){
            //Chose a player.  That player must place the last noble he or she collected at the end of the line.
            //This is implemented as the last player to collect a noble has theirs removed.


            curState.setNeedPopUp(true);
            curState.setPopUpType(0); //0 = choose a player (number of players = number of buttons on pop up

            if(curState.getIntPopUpRecievedInfo()!=-1){

            if (curState.getIntPopUpRecievedInfo() == 2 && curState.computerPlayer1Nobles.size()>0) {
                Noble card = curState.computerPlayer1Nobles.get(curState.computerPlayer1Nobles.size() - 1);
                curState.computerPlayer1Nobles.remove(curState.computerPlayer1Nobles.size() - 1);
                curState.deathRow.add(card);
                curState.addToMessage(card.getNobleName() + " was moved from Player 2's hand to death row. ");
            }
            if (curState.getIntPopUpRecievedInfo() == 3 && curState.computerPlayer2Nobles.size()>0) {
                Noble card = curState.computerPlayer2Nobles.get(curState.computerPlayer2Nobles.size()-1);
                curState.computerPlayer2Nobles.remove(curState.computerPlayer2Nobles.size() - 1);
                curState.deathRow.add(card);
                curState.addToMessage(card.getNobleName() + " was moved from Player 3's hand to death row. ");
            }
            if (curState.getIntPopUpRecievedInfo() == 4 && curState.computerPlayer3Nobles.size()>0) {
                Noble card = curState.computerPlayer3Nobles.get(curState.computerPlayer3Nobles.size() - 1);
                curState.computerPlayer3Nobles.remove(curState.computerPlayer3Nobles.size() - 1);
                curState.deathRow.add(card);
                curState.addToMessage(card.getNobleName() + " was moved from Player 4's hand to death row. ");
            }
                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);

            }


        }
        else if (name.equals("Missing Heads")){
            //Choose a player. That player loses a random noble from his or her score pile.
            //Allows you to choose this player and removes a noble from them.

            curState.setNeedPopUp(true);
            curState.setPopUpType(0); //0 = choose a player (number of players = number of buttons on pop up

            if(curState.getIntPopUpRecievedInfo()!=-1){
                // int playerChosen = curState.getIntPopUpRecievedInfo();

                if(curState.getIntPopUpRecievedInfo()==2){

                    int cardNum = (int)(rand*curState.computerPlayer1Nobles.size());
                    Noble card = curState.computerPlayer1Nobles.get(cardNum);
                    curState.addToMessage("Player 2 lost the "+ card.getNobleName() + " card. ");
                    curState.computerPlayer1Nobles.remove(cardNum);

                }
                if(curState.getIntPopUpRecievedInfo()==3){
                    int cardNum = (int)(rand*curState.computerPlayer2Nobles.size());
                    Noble card = curState.computerPlayer2Nobles.get(cardNum);
                    curState.addToMessage("Player 3 lost the "+ card.getNobleName() + " card. ");
                    curState.computerPlayer2Nobles.remove(cardNum);
                }
                if(curState.getIntPopUpRecievedInfo()==4){
                    int cardNum = (int)(rand*curState.computerPlayer3Nobles.size());
                    Noble card = curState.computerPlayer3Nobles.get(cardNum);
                    curState.addToMessage("Player 4 lost the "+ card.getNobleName() + " card. ");
                    curState.computerPlayer3Nobles.remove(cardNum);
                }
                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
            }

        }
        else if (name.equals("Opinionated Guards")){
            //Rearrange the first 4 nobles in line any way you wish.
            //This is random for now, may be changed later.

            ArrayList<Noble> deathRow = curState.getDeathRow();
            ArrayList<Noble> firstFour = new ArrayList<Noble>();

            Noble one = deathRow.get(0);
            firstFour.add(one);

            if(deathRow.size()>=2) {
                Noble two = deathRow.get(1);
                firstFour.add(two);
            }
            if(deathRow.size()>=3) {
                Noble three = deathRow.get(2);
                firstFour.add(three);
            }
            if(deathRow.size()>=4) {
                Noble four = deathRow.get(3);
                firstFour.add(four);
            }
            int numGoThrough = 4;
            if(curState.deathRow.size()<4){
                numGoThrough=curState.deathRow.size();
            }
            Collections.shuffle(firstFour);
            for(int i =0;i<numGoThrough;i++) {
                curState.removeFromDeathRow(0);
            }
            for(int i =0;i<numGoThrough;i++) {
                deathRow.add(i, firstFour.get(i));
            }
            curState.addToMessage("The first four nobles in line were rearranged. ");
            curState.setDeathRow(deathRow);

        }
        else if (name.equals("Political Influence")){
            //Draw 3 additional action cards at the end of your turn.  Do not collect a noble this turn.

            curState.drawActionCard(curState.getCurrentPlayer());
            curState.drawActionCard(curState.getCurrentPlayer());
            curState.drawActionCard(curState.getCurrentPlayer());
            curState.setNobleCollectOk(false);
            curState.addToMessage("You have collected three action cards and no noble. ");

        }
        else if (name.equals("Public Demand")){
            //Move any noble in line to the front of the line.
            //Allows you to choose a noble.

            //curState.resetMessage();
            curState.addToMessage("Please choose Noble to kill next...");

            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble form line

            if(curState.getIntPopUpRecievedInfo()!=-1) {

                Noble chooseNoble = curState.deathRow.get(curState.getIntPopUpRecievedInfo());
                curState.deathRow.remove(curState.getIntPopUpRecievedInfo());
                curState.deathRow.add(0, chooseNoble);

                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
            }

        }
        else if (name.equals("Pushed")){
            //Move a noble forward exactly 2 places in line.
            //Does not allow you to choose the noble in 0 or 1 spot, but any others are fair game.

            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble form line

            if(curState.getIntPopUpRecievedInfo()!=-1) {

                ArrayList<Noble> deathRow = curState.getDeathRow();

                Noble card = curState.deathRow.get(curState.getIntPopUpRecievedInfo());
                int numCard = curState.getIntPopUpRecievedInfo();

                if(deathRow.size()>2) {

                    if(curState.getIntPopUpRecievedInfo()<2){
                        numCard= 3;
                        card = deathRow.get(numCard);
                    }

                        deathRow.remove(numCard);

                        deathRow.add((numCard - 2), card);
                        curState.addToMessage(card.getNobleName() + " was moved forward two spaces. ");

                }
                curState.setDeathRow(deathRow);

                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
            }

        }
        else if (name.equals("Rain Delay")){
            //Shuffle all players' hands into the action deck and deal out 5 new action cards to each player.

            if (curState.getNumPlayers() == 2) {
                curState.getHumanPlayerHand().clear();
                curState.getComputerPlayer1Hand().clear();
                curState.deal();
            } else if (curState.getNumPlayers() == 3) {
                curState.getHumanPlayerHand().clear();
                curState.getComputerPlayer1Hand().clear();
                curState.getComputerPlayer2Hand().clear();
                curState.deal();
            } else if (curState.getNumPlayers() == 4) {
                curState.getHumanPlayerHand().clear();
                curState.getComputerPlayer1Hand().clear();
                curState.getComputerPlayer2Hand().clear();
                curState.getComputerPlayer3Hand().clear();
                curState.deal();
            }
        }
        else if (name.equals("Scarlet Pimpernel")){
            //The day ends after you finish your turn.  Discard any remaining nobles in line.
            //Sets death row to only the first card.  After this is collected the day will automatically end.

            Noble card = curState.getDeathRow().get(0);
            curState.deathRow.clear();
            curState.deathRow.add(card);

        }
        else if (name.equals("Stumble")){
            //Move a noble forward exactly 1 place.
            //Allows human player to choose this.

            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble form line

            if(curState.getIntPopUpRecievedInfo()!=-1) {
                ArrayList<Noble> deathRow = curState.getDeathRow();

                Noble card = curState.deathRow.get(curState.getIntPopUpRecievedInfo());
                int numCard = curState.getIntPopUpRecievedInfo();
                if(deathRow.size()>1) {

                    if(curState.getIntPopUpRecievedInfo()<1){
                        numCard= 2;
                    }

                    deathRow.remove(numCard);
                    deathRow.add((numCard - 1), card);
                    curState.addToMessage(card.getNobleName() + " was moved forward one space. ");

                }
                curState.setDeathRow(deathRow);

                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
            }

        }
        else if (name.equals("The Long Walk")){
            //Reverse the order of the line.
            //Uses a dummy line.

            ArrayList<Noble> curDeathRow = curState.getDeathRow();
            ArrayList<Noble> newDeathRow = new ArrayList<Noble>();
            int curDeathRowSize = curDeathRow.size();
            for (int i = 0; i< curDeathRowSize; i++){
                newDeathRow.add(i, curDeathRow.get((curDeathRow.size()-1)-i));
            }
            curState.setDeathRow(newDeathRow);
            curState.addToMessage("The order of the line was reversed. ");

        }
        else if (name.equals("'Tis a Far Better Thing")){
            //Move a noble forward exactly 3 places in line.
            //This allows the player to choose which noble to do that to, after the third noble in line.

            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble form line

            if(curState.getIntPopUpRecievedInfo()!=-1) {

                ArrayList<Noble> deathRow = curState.getDeathRow();

                Noble card = curState.deathRow.get(curState.getIntPopUpRecievedInfo());
                int numCard = curState.getIntPopUpRecievedInfo();

                if(deathRow.size()>3) {

                    if(curState.getIntPopUpRecievedInfo()<3){
                        numCard= 4;
                    }
                    deathRow.remove(numCard);
                    deathRow.add((numCard - 3), card);
                    curState.addToMessage(card.getNobleName() + " was moved forward three spaces. ");

                }
                curState.setDeathRow(deathRow);

                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
            }

        }
        else if (name.equals("Tough Crowd")){
            //Put this card in front of another player.  It is worth -2 points to that player.
            //Allows player to choose who this happens to.

            curState.setNeedPopUp(true);
            curState.setPopUpType(0); //0 = choose a player (number of players = number of buttons on pop up

            if(curState.getIntPopUpRecievedInfo()!=-1){

                // int playerChosen = curState.getIntPopUpRecievedInfo();

                if(curState.getIntPopUpRecievedInfo()==2){
                    curState.setHasToughCrowd(1);
                }
                if(curState.getIntPopUpRecievedInfo()==3){
                    curState.setHasToughCrowd(2);
                }
                if(curState.getIntPopUpRecievedInfo()==4){
                    curState.setHasToughCrowd(3);
                }

                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
            }

        }
        else if (name.equals("Trip")){
            //Move a noble backward exactly one place in line.  You may play another action card this turn.
            //Allows player to choose, then sets the second action ability to true.

            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble form line

            if(curState.getIntPopUpRecievedInfo()!=-1) {
                ArrayList<Noble> deathRow = curState.getDeathRow();

                Noble card = curState.deathRow.get(curState.getIntPopUpRecievedInfo());
                int numCard = curState.getIntPopUpRecievedInfo();

                if(deathRow.size()>2) {

                    if(curState.getIntPopUpRecievedInfo()>deathRow.size()-1){
                        numCard= deathRow.size()-2;
                        card = deathRow.get(numCard);
                    }

                    deathRow.remove(numCard);
                    deathRow.add((numCard + 1), card);
                    curState.addToMessage(card.getNobleName() + " was moved backwards one space. ");
                }
                curState.setDeathRow(deathRow);
                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
                curState.setPlaySecondAction(true);
            }

        }
        else if (name.equals("Was That My Name?")){
            //Move a noble forward up to 3 places in line.
            //Allows user to choose that noble, the number of spaces forward is random.

            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble form line

            if(curState.getIntPopUpRecievedInfo()!=-1) {

                ArrayList<Noble> deathRow = curState.getDeathRow();
                Noble card = curState.deathRow.get(curState.getIntPopUpRecievedInfo());
                int numCard = curState.getIntPopUpRecievedInfo();

                if(deathRow.size()>3) {

                    if(curState.getIntPopUpRecievedInfo()<3){
                        numCard= 3;
                    }

                    double randSpaces = Math.random();
                    deathRow.remove(numCard);
                    if (randSpaces< .33) {
                        deathRow.add((numCard - 1), card);
                        curState.addToMessage(card.getNobleName() + " was moved forward one space. ");
                    } else if (randSpaces >=.33 && randSpaces<.66) {
                        deathRow.add((numCard - 2), card);
                        curState.addToMessage(card.getNobleName() + " was moved forward two spaces. ");
                    } else if(randSpaces>= .66){

                        deathRow.add((numCard - 3), card);
                        curState.addToMessage(card.getNobleName() + " was moved forward three spaces. ");
                    }
                }
                curState.setDeathRow(deathRow);

                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);
            }
        }

        return curState;
    }
    public GuillotineState easyAIAction(GuillotineState curState){
        // make if statements for every action card
        double rand = Math.random();

        if(name.equals("After You....")){
            //Put the noble at the front of the line into another player's score pile
            if (curState.getNumPlayers() == 2) {
                curState.collectNoble(0);

            }
            if (curState.getNumPlayers() == 3) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand > .5){
                        curState.collectNoble(0);
                    }
                    else{
                        curState.collectNoble(2);
                    }
                }
                else if (curState.getCurrentPlayer() == 2) {

                    if(rand>.5){
                        curState.collectNoble(0);
                    }
                    else{
                        curState.collectNoble(1);
                    }

                }
            }
            if (curState.getNumPlayers() == 4) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand < .33){
                        curState.collectNoble(0);

                    }
                    else if(rand>.33 && rand<.66){
                        curState.collectNoble(2);
                    }
                    else{
                        curState.collectNoble(3);
                    }
                } else if (curState.getCurrentPlayer() == 2) {
                    if (rand < .33){
                        curState.collectNoble(0);

                    }
                    else if(rand>.33 && rand<.66){
                        curState.collectNoble(1);
                    }
                    else{
                        curState.collectNoble(3);
                    }

                } else if(curState.getCurrentPlayer()==3){
                    if (rand < .33){
                        curState.collectNoble(0);

                    }
                    else if(rand>.33 && rand<.66){
                        curState.collectNoble(1);
                    }
                    else{
                        curState.collectNoble(2);
                    }

                }
            }
        }
        else if (name.equals("Bribed Guards")){
            //Move the noble at the front of the line to the end of the line
            Noble firstNob = curState.deathRow.get(0);
            curState.addToMessage(firstNob.getNobleName() + " was moved to the end of the line. ");
            curState.removeFromDeathRow(0);
            curState.deathRow.add(firstNob);

        }
        else if (name.equals("Church Support")){
            //Put this card in front of you. It is worth +1 for each Blue noble in your score pile
            //Scoring is implemented in the calculateScore method of game state
            curState.setHasChurchSupport(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer()+1) + " will receive +1 for each blue noble they collect. ");

        }
        else if (name.equals("Civic Pride")){
            //Move a Green noble forward up to 2 places in line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i<deathRow.size();i++){
                if(deathRow.get(i).getNobleColor().equals("green")){
                    Noble card = deathRow.get(i);
                    deathRow.remove(i);
                    if(rand<0.5){
                        deathRow.add(i-1, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward one place. ");
                    }
                    else{
                        deathRow.add(i-2, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward two places. ");
                    }
                    curState.setDeathRow(deathRow);
                    break;
                }
            }

        }
        else if (name.equals("Civic Support")){
            //Play this card in front of you.  It is worth +1 for each Green noble in your score pile
            curState.setHasCivicSupport(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive +1 for each green noble they collect. ");
        }
        else if (name.equals("Clothing Swap")){
            //Choose any noble in line and discard it.  Replace it with the top noble from the noble deck.
            int randNob = (int)(Math.random()*(curState.deathRow.size()));
            Noble leavingNob = curState.deathRow.get(randNob);
            Noble arrivingNob = curState.nobleDeck.get(0);
            curState.deathRow.remove(randNob);
            curState.nobleDeck.remove(0);
            curState.deathRow.add(randNob, arrivingNob);
            curState.addToMessage(leavingNob.getNobleName() + " was replaced with " + arrivingNob.getNobleName() + ". ");
        }
        else if (name.equals("Confusion in Line")){
            //Choose a player. Randomly rearrange the line just before that player collects his or her next noble.
            curState.shuffleDeathRow();
            curState.addToMessage("Death Row was shuffled. ");

        }
        else if (name.equals("Double Feature")){
            //Collect an additional noble from the front of the line this turn.
            if(curState.getCurrentPlayer()==1){
               curState.collectNoble(1);

            }
            else if(curState.getCurrentPlayer()==2){
                curState.collectNoble(2);
            }
            else if(curState.getCurrentPlayer()==3){
                curState.collectNoble(3);
            }
            curState.addToMessage("Player " + (curState.getCurrentPlayer()+1) + " gets two nobles! ");
        }
        else if (name.equals("Escape!")){
            //Randomly choose two nobles in line and discard them. Randomly rearrange the remaining nobles in line.
            if(curState.deathRow.size()>2) {
                int randChosenNob1 = (int) (Math.random() * (curState.deathRow.size()));
                curState.nobleDiscard.add(curState.deathRow.get(randChosenNob1));
                curState.deathRow.remove(randChosenNob1);
                int randChosenNob2 = (int) (Math.random() * (curState.deathRow.size()));
                curState.nobleDiscard.add(curState.deathRow.get(randChosenNob2));
                curState.deathRow.remove(randChosenNob2);

                curState.shuffleDeathRow();

                curState.addToMessage(randChosenNob1 + " and " + randChosenNob2 + " were discarded. Death row was shuffled. ");

            }
        }
        else if (name.equals("Extra Cart")){
            //Add 3 nobles from the noble deck to the end of the line.
            if(curState.deathRow.size()<=9){
                for(int i =0;i<3;i++) {
                    curState.deathRow.add(curState.nobleDeck.get(0));
                    curState.addToMessage(curState.nobleDeck.get(0).getNobleName() + " was added to Death Row. ");
                    curState.nobleDeck.remove(0);
                }

            }
            else if(curState.deathRow.size()==10){
                for(int i =0;i<2;i++) {
                    curState.deathRow.add(curState.nobleDeck.get(0));
                    curState.addToMessage(curState.nobleDeck.get(0).getNobleName() + " was added to Death Row. ");
                    curState.nobleDeck.remove(0);
                }
            }
            else if(curState.deathRow.size()==11){
                curState.deathRow.add(curState.nobleDeck.get(0));
                curState.addToMessage(curState.nobleDeck.get(0).getNobleName() + " was added to Death Row. ");
                curState.nobleDeck.remove(0);
            }

        }
        else if (name.equals("Fainting Spell")){
            //Move a noble backward up to 3 places in line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            int randIndex = (int)(Math.random()*(deathRow.size()-3));
            double randSpaces = Math.random();
            String nobleName;
            if (randSpaces>=.33)
            {
                nobleName = deathRow.get(randIndex).getNobleName();
                deathRow.add((randIndex+1), deathRow.get(randIndex));
                deathRow.remove(randIndex);
                curState.addToMessage(nobleName + " was moved backward one space. ");
            }
            else if (randSpaces>.33 && randSpaces<.67)
            {
                nobleName = deathRow.get(randIndex).getNobleName();
                deathRow.add((randIndex+2), deathRow.get(randIndex));
                deathRow.remove(randIndex);
                curState.addToMessage(nobleName + " was moved backward two spaces. ");
            }
            else if (randSpaces>=.67)
            {
                nobleName = deathRow.get(randIndex).getNobleName();
                deathRow.add((randIndex+3), deathRow.get(randIndex));
                deathRow.remove(randIndex);
                curState.addToMessage(nobleName + " was moved backward three spaces. ");
            }
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Fled to England")){
            //Discard any noble in line.

            int randEscapeNob = (int)(Math.random()*curState.deathRow.size());
            curState.nobleDiscard.add(curState.deathRow.get(randEscapeNob));
            curState.addToMessage(curState.deathRow.get(randEscapeNob).getNobleName() + " was discarded. ");
            curState.deathRow.remove(randEscapeNob);

        }
        else if (name.equals("Forced Break")){
            //All other players must discard an action card at random.
            if(curState.getNumPlayers()==2){
                int rand1 = (int)(Math.random()*curState.humanPlayerHand.size());

                curState.actionDiscard.add(curState.humanPlayerHand.get(rand1));
                curState.humanPlayerHand.remove(rand1);
            }
            else if(curState.getNumPlayers()==3){
                if(curState.getCurrentPlayer() == 1){
                    int rand1 = (int)(Math.random()*curState.humanPlayerHand.size());
                    int rand3 = (int)(Math.random()*curState.computerPlayer2Hand.size());

                    curState.actionDiscard.add(curState.humanPlayerHand.get(rand1));
                    curState.actionDiscard.add(curState.computerPlayer2Hand.get(rand3));
                    curState.humanPlayerHand.remove(rand1);
                    curState.computerPlayer2Hand.remove(rand3);
                }
                else {
                    int rand1 = (int) (Math.random() * curState.humanPlayerHand.size());
                    int rand2 = (int) (Math.random() * curState.computerPlayer1Hand.size());

                    curState.actionDiscard.add(curState.humanPlayerHand.get(rand1));
                    curState.actionDiscard.add(curState.computerPlayer1Hand.get(rand2));
                    curState.humanPlayerHand.remove(rand1);
                    curState.computerPlayer1Hand.remove(rand2);
                }

            }
            else if(curState.getNumPlayers()==4) {
                if(curState.getCurrentPlayer() == 1) {
                    int rand1 = (int) (Math.random() * curState.humanPlayerHand.size());
                    int rand3 = (int) (Math.random() * curState.computerPlayer2Hand.size());
                    int rand4 = (int) (Math.random() * curState.computerPlayer3Hand.size());

                    curState.actionDiscard.add(curState.humanPlayerHand.get(rand1));
                    curState.actionDiscard.add(curState.computerPlayer2Hand.get(rand3));
                    curState.actionDiscard.add(curState.computerPlayer3Hand.get(rand4));
                    curState.humanPlayerHand.remove(rand1);
                    curState.computerPlayer2Hand.remove(rand3);
                    curState.computerPlayer3Hand.remove(rand4);
                }
                else if(curState.getCurrentPlayer() == 2) {
                    int rand1 = (int) (Math.random() * curState.humanPlayerHand.size());
                    int rand2 = (int) (Math.random() * curState.computerPlayer1Hand.size());
                    int rand4 = (int) (Math.random() * curState.computerPlayer3Hand.size());

                    curState.actionDiscard.add(curState.humanPlayerHand.get(rand1));
                    curState.actionDiscard.add(curState.computerPlayer1Hand.get(rand2));
                    curState.actionDiscard.add(curState.computerPlayer3Hand.get(rand4));
                    curState.humanPlayerHand.remove(rand1);
                    curState.computerPlayer1Hand.remove(rand2);
                    curState.computerPlayer3Hand.remove(rand4);
                }
                else{
                    int rand1 = (int) (Math.random() * curState.humanPlayerHand.size());
                    int rand2 = (int) (Math.random() * curState.computerPlayer1Hand.size());
                    int rand3 = (int) (Math.random() * curState.computerPlayer2Hand.size());

                    curState.actionDiscard.add(curState.humanPlayerHand.get(rand1));
                    curState.actionDiscard.add(curState.computerPlayer1Hand.get(rand2));
                    curState.actionDiscard.add(curState.computerPlayer2Hand.get(rand3));
                    curState.humanPlayerHand.remove(rand1);
                    curState.computerPlayer1Hand.remove(rand2);
                    curState.computerPlayer2Hand.remove(rand3);
                }
            }
            curState.addToMessage("All other players discarded one action card. ");

        }
        else if (name.equals("Foreign Support")){
            //Put this card in front of you.  Draw an action card whenever you collect a purple noble.
            curState.setHasForeignSupport(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive one action card for each purple noble they collect. ");
        }
        else if (name.equals("Forward March")){
            // Move a palace guard to the front of the line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            for(int i = 0; i < deathRow.size(); i++) {
                Noble card = deathRow.get(i);
                if (card.getNobleName().equals("Palace Guard")){
                    deathRow.add(0, card);
                    deathRow.remove(i);
                    curState.addToMessage("A Palace Guard was moved to the front of the line. ");
                    break;
                }

            }
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Fountain of Blood")){
            //Put this card in front of you. It is worth 2 points.
            curState.setHasFountainOfBlood(curState.getCurrentPlayer());
            curState.addToMessage((curState.getCurrentPlayer()+1) + " gets two bonus points.");
        }
        else if (name.equals("Friend of the Queen")){
            //Move a noble backward up to 2 places in line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            int randIndex = (int)(Math.random()*(deathRow.size()-2));
            Noble card = deathRow.get(randIndex);
            double randSpaces = Math.random();
            deathRow.remove(randIndex);
            if (randSpaces>=.5)
            {
                deathRow.add((randIndex+1), card);
                curState.addToMessage(card.getNobleName() + "was moved backward one space. ");
            }
            else if (randSpaces<.5)
            {
                deathRow.add((randIndex+2), card);
                curState.addToMessage(card.getNobleName() + "was moved backward two spaces. ");
            }
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Ignoble Noble")){
            //Move a noble forward exactly 4 places in line.

                ArrayList<Noble> deathRow = curState.getDeathRow();
            if(curState.deathRow.size()>4) {
                int randIndex = 4 + (int) (Math.random() * (deathRow.size() - 4));
                Noble card = deathRow.get(randIndex);
                deathRow.remove(randIndex);
                deathRow.add((randIndex - 4), card);
                curState.addToMessage(card.getNobleName() + "was moved forward four spaces. ");
            }
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Indifferent Public")){
            //Put this card in front of you.  Any Gray nobles in your score pile are worth 1 point instead of their
            //normal values.
            curState.setHasIndifferentPublic(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive one point for each gray noble instead of its normal value. ");

        }
        else if (name.equals("Information Exchange")){
            //Trade hands with another player.
            if (curState.getNumPlayers() == 2) {
                ArrayList<ActionCard> curPlayerHand = curState.computerPlayer1Hand;
                curState.computerPlayer1Hand = curState.humanPlayerHand;
                curState.humanPlayerHand = curPlayerHand;
                curState.addToMessage("Player 2 switched hands with you. ");
            }
            if (curState.getNumPlayers() == 3) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand > .5){
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curState.humanPlayerHand;
                        curState.humanPlayerHand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with you. ");
                    }
                    else{
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with Player 3. ");
                    }
                }
                else if (curState.getCurrentPlayer() == 2) {

                    if(rand>.5){
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curState.humanPlayerHand;
                        curState.humanPlayerHand = curPlayerHand;
                        curState.addToMessage("Player 3 switched hands with you. ");
                    }
                    else{
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with Player 3. ");
                    }

                }
            }
            if (curState.getNumPlayers() == 4) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand < .33){
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curState.humanPlayerHand;
                        curState.humanPlayerHand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with you. ");
                    }
                    else if(rand>.33 && rand<.66){
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with Player 3. ");
                    }
                    else{
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer3Hand;
                        curState.computerPlayer3Hand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with Player 4. ");
                    }
                } else if (curState.getCurrentPlayer() == 2) {
                    if (rand < .33){
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curState.humanPlayerHand;
                        curState.humanPlayerHand = curPlayerHand;
                        curState.addToMessage("Player 3 switched hands with you. ");

                    }
                    else if(rand>.33 && rand<.66){
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with Player 3. ");
                    }
                    else{
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer3Hand;
                        curState.computerPlayer3Hand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curPlayerHand;
                        curState.addToMessage("Player 3 switched hands with Player 4. ");
                    }

                } else if(curState.getCurrentPlayer()==3){
                    if (rand < .33){
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer3Hand;
                        curState.computerPlayer3Hand = curState.humanPlayerHand;
                        curState.humanPlayerHand = curPlayerHand;
                        curState.addToMessage("Player 4 switched hands with you. ");

                    }
                    else if(rand>.33 && rand<.66){
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer3Hand;
                        curState.computerPlayer3Hand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with Player 4. ");
                    }
                    else{
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer3Hand;
                        curState.computerPlayer3Hand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curPlayerHand;
                        curState.addToMessage("Player 3 switched hands with Player 4. ");
                    }

                }
            }
        }
        else if (name.equals("Lack of Faith")){
            //If there are any blue nobles in line, move the one nearest the front of the line to the front
            //of the line.
            for(int i = 0; i<curState.deathRow.size();i++){
                if(curState.deathRow.get(i).getNobleColor().equals("blue")){
                    Noble card = curState.deathRow.get(i);
                    curState.deathRow.remove(i);
                    curState.deathRow.add(0,card);
                    curState.addToMessage(card.getNobleName() + " was moved to the front of the line. ");
                    break;
                }
            }
        }
        else if (name.equals("Late Arrival")){
            //Look at the top 3 cards of the noble deck and add any one of them to the end of the line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            double random = Math.random();
            if(deathRow.size()>=3) {
                Noble card;
                if (random <= .33) {
                    card = curState.getNobleDeck().get(0);
                    curState.nobleDeck.remove(0);
                    deathRow.add(card);
                    curState.addToMessage(card.getNobleName() + " was added to the end of Death Row. ");
                } else if (random > .33 && random <= .67) {
                    card = curState.getNobleDeck().get(1);
                    curState.nobleDeck.remove(1);
                    deathRow.add(card);
                    curState.addToMessage(card.getNobleName() + " was added to the end of Death Row. ");
                } else if (random > .67) {
                    card = curState.getNobleDeck().get(2);
                    curState.nobleDeck.remove(2);
                    deathRow.add(card);
                    curState.addToMessage(card.getNobleName() + " was added to the end of Death Row. ");
                }

            }
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Let Them Eat Cake")){
            //If Marie Antoinette is in line, move her to the front of the line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            for(int i = 1; i < deathRow.size(); i++)
            {
                Noble card = deathRow.get(i);
                if (card.getNobleName().equals("Marie Antoinette"))
                {
                    deathRow.remove(i);
                    deathRow.add(0, card);
                    curState.addToMessage("Marie Antoinette was moved to the front of the line. ");
                    break;
                }
            }
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("L'Idiot")){
            //Move a noble forward up to 2 places in line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            if(deathRow.size()>2) {
                int randIndex = 2 + ((int) (Math.random() * (deathRow.size() - 2)));
                Noble card = deathRow.get(randIndex);
                double randSpaces = Math.random();
                deathRow.remove(randIndex);
                if (randSpaces >= .5) {
                    deathRow.add((randIndex - 1), card);
                    curState.addToMessage(card.getNobleName() + " was moved forward one space. ");
                } else if (randSpaces < .5) {
                    deathRow.add((randIndex - 2), card);
                    curState.addToMessage(card.getNobleName() + " was moved forward two spaces. ");
                }
            }
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Majesty")){
            //Move a purple noble forward up to 2 places in line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i<deathRow.size();i++){
                if(deathRow.get(i).getNobleColor().equals("purple")){
                    Noble card = deathRow.get(i);
                    deathRow.remove(i);
                    if(rand<0.5){
                        deathRow.add(i-1, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward one place. ");
                    }
                    else{
                        deathRow.add(i-2, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward two places. ");
                    }
                    curState.setDeathRow(deathRow);
                    break;
                }
            }
        }
        else if (name.equals("Mass Confusion")){
            //Put all nobles in line in the noble deck.  Shuffle the noble
            //deck and deal out the same number of nobles in a new line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            int numNobles = deathRow.size();
            ArrayList<Noble> currentNobleDeck = curState.getNobleDeck();

            for(int i = 0; i< deathRow.size();i++){
                Noble card = deathRow.get(0);
                deathRow.remove(0);
                currentNobleDeck.add(card);
            }
            curState.setNobleDeck(currentNobleDeck);
            curState.shuffleNobleDeck();
            curState.createDeathRow(numNobles);
            curState.addToMessage("All nobles in line were shuffled into the deck and Death Row was recreated. ");
        }
        else if (name.equals("Military Might")){
            //Move a Red noble forward up to 2 places in line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i<deathRow.size();i++){
                if(deathRow.get(i).getNobleColor().equals("red")){
                    Noble card = deathRow.get(i);
                    deathRow.remove(i);
                    if(rand<0.5){
                        deathRow.add(i-1, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward one place. ");
                    }
                    else{
                        deathRow.add(i-2, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward two places. ");
                    }
                    curState.setDeathRow(deathRow);
                    break;
                }
            }
        }
        else if (name.equals("Military Support")) {
            //Put this card in front of you.  It is worth +1 point for each red noble in your score pile.
            curState.setHasMilitarySupport(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive +1 for each red noble they collect. ");

        }
        else if (name.equals("Milling in Line")) {
            //Randomly rearrange the first 5 nobles in line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            ArrayList<Noble> firstFive = new ArrayList<Noble>();

            Noble one = deathRow.get(0);
            firstFive.add(one);

            if(deathRow.size()>=2) {
                Noble two = deathRow.get(1);
                firstFive.add(two);
            }
            if(deathRow.size()>=3) {
                Noble three = deathRow.get(2);
                firstFive.add(three);
            }
            if(deathRow.size()>=4) {
                Noble four = deathRow.get(3);
                firstFive.add(four);
            }
            if(deathRow.size() >= 5) {
                Noble five = deathRow.get(4);
                firstFive.add(five);
            }


            int numGoThrough = 5;
            if(curState.deathRow.size()<5){
                numGoThrough=curState.deathRow.size();
            }

            Collections.shuffle(firstFive);
            for(int i =0;i<numGoThrough; i++) {
                curState.removeFromDeathRow(0);
            }
            for(int i =0;i<numGoThrough;i++){
                deathRow.add(i,firstFive.get(i));
            }

            curState.addToMessage("The first five nobles in line were rearranged. ");
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Missed!")){
            //Choose a player.  That player must place the last noble he or she collected at the end of the line.
            if (curState.getNumPlayers() == 2) {
                Noble card = curState.getHumanPlayerNobles().get(curState.getHumanPlayerNobles().size()-1);
                curState.humanPlayerNobles.remove(curState.getHumanPlayerNobles().size() - 1);
                curState.deathRow.add(card);
                curState.addToMessage(card.getNobleName() + " was moved from your hand to death row. ");

            }
            if (curState.getNumPlayers() == 3) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand > .5){
                        Noble card = curState.getHumanPlayerNobles().get(curState.getHumanPlayerNobles().size() - 1);
                        curState.humanPlayerNobles.remove(curState.getHumanPlayerNobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from your hand to death row. ");
                    } else{
                        Noble card = curState.getComputerPlayer2Nobles().get(curState.getComputerPlayer2Nobles().size()-1);
                        curState.computerPlayer2Nobles.remove(curState.getComputerPlayer2Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 3's hand to death row. ");
                    }
                } else if (curState.getCurrentPlayer() == 2) {

                    if(rand>.5){
                        Noble card = curState.getHumanPlayerNobles().get(curState.getHumanPlayerNobles().size()-1);
                        curState.humanPlayerNobles.remove(curState.getHumanPlayerNobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from your hand to death row. ");
                    }
                    else{
                        Noble card = curState.getComputerPlayer1Nobles().get(curState.getComputerPlayer1Nobles().size()-1);
                        curState.computerPlayer1Nobles.remove(curState.getComputerPlayer1Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 2's hand to death row. ");
                    }

                }
            }
            if (curState.getNumPlayers() == 4) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand < .33){
                        Noble card = curState.getHumanPlayerNobles().get(curState.getHumanPlayerNobles().size()-1);
                        curState.humanPlayerNobles.remove(curState.getHumanPlayerNobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from your hand to death row. ");

                    }
                    else if(rand>.33 && rand<.66){
                        Noble card = curState.getComputerPlayer2Nobles().get(curState.getComputerPlayer2Nobles().size()-1);
                        curState.computerPlayer2Nobles.remove(curState.getComputerPlayer2Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 3's hand to death row. ");
                    }
                    else{
                        Noble card = curState.getComputerPlayer3Nobles().get(curState.getComputerPlayer3Nobles().size()-1);
                        curState.computerPlayer3Nobles.remove(curState.getComputerPlayer3Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 4's hand to death row. ");
                    }
                } else if (curState.getCurrentPlayer() == 2) {
                    if (rand < .33){
                        Noble card = curState.getHumanPlayerNobles().get(curState.getHumanPlayerNobles().size()-1);
                        curState.humanPlayerNobles.remove(curState.getHumanPlayerNobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from your hand to death row. ");

                    }
                    else if(rand>.33 && rand<.66) {
                        Noble card = curState.getComputerPlayer1Nobles().get(curState.getComputerPlayer1Nobles().size()-1);
                        curState.computerPlayer1Nobles.remove(curState.getComputerPlayer1Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 2's hand to death row. ");
                    }
                    else{
                        Noble card = curState.getComputerPlayer3Nobles().get(curState.getComputerPlayer3Nobles().size()-1);
                        curState.computerPlayer3Nobles.remove(curState.getComputerPlayer3Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 4's hand to death row. ");
                    }

                } else if(curState.getCurrentPlayer()==3){
                    if (rand < .33){
                        Noble card = curState.getHumanPlayerNobles().get(curState.getHumanPlayerNobles().size()-1);
                        curState.humanPlayerNobles.remove(curState.getHumanPlayerNobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from your hand to death row. ");

                    }
                    else if(rand>.33 && rand<.66){
                        Noble card = curState.getComputerPlayer1Nobles().get(curState.getComputerPlayer1Nobles().size()-1);
                        curState.computerPlayer1Nobles.remove(curState.getComputerPlayer1Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 2's hand to death row. ");
                    }
                    else{
                        Noble card = curState.getComputerPlayer2Nobles().get(curState.getComputerPlayer2Nobles().size()-1);
                        curState.computerPlayer2Nobles.remove(curState.getComputerPlayer2Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 3's hand to death row. ");
                    }

                }
            }
        }
        else if (name.equals("Missing Heads")){
            //Choose a player. That player loses a random noble from his or her score pile.
            if (curState.getNumPlayers() == 2) {
                if(curState.humanPlayerNobles.size() != 0) {
                    int cardNum = (int) (rand * curState.humanPlayerNobles.size());
                    Noble card = curState.humanPlayerNobles.get(cardNum);
                    curState.addToMessage("You lost " + card.getNobleName() + ". ");
                    curState.humanPlayerNobles.remove(cardNum);
                }

            }
            if (curState.getNumPlayers() == 3) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand > .5){
                        if (curState.humanPlayerNobles.size() != 0) {
                            int cardNum = (int) (rand * curState.humanPlayerNobles.size());
                            Noble card = curState.humanPlayerNobles.get(cardNum);
                            curState.addToMessage("You lost " + card.getNobleName() + ". ");
                            curState.humanPlayerNobles.remove(cardNum);
                        }
                    }
                    else{
                        if (curState.computerPlayer2Nobles.size()!= 0) {
                            int cardNum = (int) (rand * curState.computerPlayer2Nobles.size());
                            Noble card = curState.computerPlayer2Nobles.get(cardNum);
                            curState.addToMessage("Player 3 lost " + card.getNobleName() + ". ");
                            curState.computerPlayer2Nobles.remove(cardNum);
                        }
                    }
                }
                else if (curState.getCurrentPlayer() == 2) {

                    if(rand>.5){

                        int cardNum = (int)(rand*curState.humanPlayerNobles.size());
                        if(cardNum>0) {
                            if (curState.humanPlayerNobles.size()!= 0) {
                                Noble card = curState.humanPlayerNobles.get(cardNum);
                                curState.addToMessage("You lost " + card.getNobleName() + ". ");
                                curState.humanPlayerNobles.remove(cardNum);
                            }
                        }
                    }
                    else{
                        int cardNum = (int)(rand*curState.computerPlayer1Nobles.size());
                        if(cardNum>0) {
                            Noble card = curState.computerPlayer1Nobles.get(cardNum);
                            curState.addToMessage("Player 2 lost " + card.getNobleName() + ". ");
                            curState.computerPlayer1Nobles.remove(cardNum);
                        }
                    }

                }
            }
            if (curState.getNumPlayers() == 4) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand < .33){
                        if (curState.humanPlayerNobles.size()!= 0) {
                            int cardNum = (int) (rand * curState.humanPlayerNobles.size());
                            Noble card = curState.humanPlayerNobles.get(cardNum);
                            curState.addToMessage("You lost " + card.getNobleName() + ". ");
                            curState.humanPlayerNobles.remove(cardNum);
                        }

                    }
                    else if(rand>.33 && rand<.66){
                        if (curState.computerPlayer2Nobles.size()!= 0) {
                            int cardNum = (int) (rand * curState.computerPlayer2Nobles.size());
                            Noble card = curState.computerPlayer2Nobles.get(cardNum);
                            curState.addToMessage("Player 3 lost " + card.getNobleName() + ". ");
                            curState.computerPlayer2Nobles.remove(cardNum);
                        }
                    }
                    else{
                        if (curState.computerPlayer3Nobles.size()!= 0) {
                            int cardNum = (int) (rand * curState.computerPlayer3Nobles.size());
                            Noble card = curState.computerPlayer3Nobles.get(cardNum);
                            curState.addToMessage("Player 4 lost " + card.getNobleName() + ". ");
                            curState.computerPlayer3Nobles.remove(cardNum);
                        }
                    }
                } else if (curState.getCurrentPlayer() == 2) {
                    if (rand < .33){
                        if (curState.humanPlayerNobles.size()!= 0) {
                            int cardNum = (int) (rand * curState.humanPlayerNobles.size());
                            Noble card = curState.humanPlayerNobles.get(cardNum);
                            curState.addToMessage("You lost " + card.getNobleName() + ". ");
                            curState.humanPlayerNobles.remove(cardNum);
                        }
                    }
                    else if(rand>.33 && rand<.66){
                        if (curState.computerPlayer1Nobles.size()!= 0) {
                            int cardNum = (int) (rand * curState.computerPlayer1Nobles.size());
                            Noble card = curState.computerPlayer1Nobles.get(cardNum);
                            curState.addToMessage("Player 2 lost " + card.getNobleName() + ". ");
                            curState.computerPlayer1Nobles.remove(cardNum);
                        }
                    }
                    else{
                        if (curState.computerPlayer3Nobles.size()!= 0) {
                            int cardNum = (int) (rand * curState.computerPlayer3Nobles.size());
                            Noble card = curState.computerPlayer3Nobles.get(cardNum);
                            curState.addToMessage("Player 4 lost " + card.getNobleName() + ". ");
                            curState.computerPlayer3Nobles.remove(cardNum);
                        }
                    }

                } else if(curState.getCurrentPlayer()==3){
                    if (rand < .33){
                        if (curState.humanPlayerNobles.size()!= 0) {
                            int cardNum = (int) (rand * curState.humanPlayerNobles.size());
                            Noble card = curState.humanPlayerNobles.get(cardNum);
                            curState.addToMessage("You lost " + card.getNobleName() + ". ");
                            curState.humanPlayerNobles.remove(cardNum);
                        }
                    } else if(rand>.33 && rand<.66){
                        if (curState.computerPlayer1Nobles.size()!= 0) {
                            int cardNum = (int) (rand * curState.computerPlayer1Nobles.size());
                            Noble card = curState.computerPlayer1Nobles.get(cardNum);
                            curState.addToMessage("Player 2 lost " + card.getNobleName() + ". ");
                            curState.computerPlayer1Nobles.remove(cardNum);
                        }
                    } else {
                        if (curState.computerPlayer2Nobles.size()!= 0) {
                            int cardNum = (int) (rand * curState.computerPlayer2Nobles.size());
                            Noble card = curState.computerPlayer2Nobles.get(cardNum);
                            curState.addToMessage("Player 3 lost " + card.getNobleName() + ". ");
                            curState.computerPlayer2Nobles.remove(cardNum);
                        }
                    }

                }
            }
        }
        else if (name.equals("Opinionated Guards")){
            //Rearrange the first 4 nobles in line any way you wish
            ArrayList<Noble> deathRow = curState.getDeathRow();
            ArrayList<Noble> firstFour = new ArrayList<Noble>();

            Noble one = deathRow.get(0);
            firstFour.add(one);

            if(deathRow.size()>=2) {
                Noble two = deathRow.get(1);
                firstFour.add(two);
            }
            if(deathRow.size()>=3) {
                Noble three = deathRow.get(2);
                firstFour.add(three);
            }
            if(deathRow.size()>=4) {
                Noble four = deathRow.get(3);
                firstFour.add(four);
            }

            int numGoThrough = 4;
            if(curState.deathRow.size()<4){
                numGoThrough=curState.deathRow.size();
            }

            Collections.shuffle(firstFour);

            for(int i =0;i<numGoThrough;i++) {
                curState.removeFromDeathRow(0);
            }
            for(int i =0;i<numGoThrough;i++) {
                deathRow.add(i, firstFour.get(i));
            }

            curState.addToMessage("The first four nobles in line were rearranged. ");
            curState.setDeathRow(deathRow);

        }
        else if (name.equals("Political Influence")){
            //Draw 3 additional action cards at the end of your turn.  Do not collect a noble this turn.
            curState.drawActionCard(curState.getCurrentPlayer());
            curState.drawActionCard(curState.getCurrentPlayer());
            curState.drawActionCard(curState.getCurrentPlayer());
            curState.setNobleCollectOk(false);
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " collected three action cards and no noble. ");
        }
        else if (name.equals("Public Demand")){
            //Move any noble in line to the front of the line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            int randIndex = (int)(Math.random()*(deathRow.size()));
            Noble card = deathRow.get(randIndex);
            deathRow.remove(randIndex);
            deathRow.add(0, card);
            curState.addToMessage(card.getNobleName() + " was moved to the front of the line. ");
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Pushed")){
            //Move a noble forward exactly 2 places in line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            if(deathRow.size()>2) {
                int randIndex = 2 + ((int) (Math.random() * (deathRow.size() - 2)));
                Noble card = deathRow.get(randIndex);
                deathRow.remove(randIndex);
                deathRow.add((randIndex - 2), card);
                curState.addToMessage(card.getNobleName() + " was moved forward two spaces. ");
            }
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Rain Delay")){
            //Shuffle all players' hands into the action deck and deal out 5 new action cards to each player.
            if(curState.getNumPlayers()==2)
            {
                curState.getHumanPlayerHand().clear();
                curState.getComputerPlayer1Hand().clear();
                curState.deal();
            }
            else if(curState.getNumPlayers()==3)
            {
                curState.getHumanPlayerHand().clear();
                curState.getComputerPlayer1Hand().clear();
                curState.getComputerPlayer2Hand().clear();
                curState.deal();
            }
            else if (curState.getNumPlayers()==4)
            {
                curState.getHumanPlayerHand().clear();
                curState.getComputerPlayer1Hand().clear();
                curState.getComputerPlayer2Hand().clear();
                curState.getComputerPlayer3Hand().clear();
                curState.deal();
            }
            curState.addToMessage("All players' hands were shuffled into the deck and each player was dealt five new action cards. ");
        }
        else if (name.equals("Scarlet Pimpernel")){
            //The day ends after you finish your turn.  Discard any nobles remaining in line.
            Noble card = curState.getDeathRow().get(0);
            curState.deathRow.clear();
            curState.deathRow.add(card);
        }
        else if (name.equals("Stumble")){
            //Move a noble forward exactly 1 places in line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            if(deathRow.size()>1) {
                int randIndex = 1 + ((int) (Math.random() * (deathRow.size() - 1)));
                Noble card = deathRow.get(randIndex);
                deathRow.remove(randIndex);
                deathRow.add((randIndex - 1), card);
                curState.addToMessage(card.getNobleName() + " moved forward one spaces. ");
            }
        }
        else if (name.equals("The Long Walk")){
            //Reverse the order of the line.
            ArrayList<Noble> curDeathRow = curState.getDeathRow();
            ArrayList<Noble> newDeathRow = new ArrayList<Noble>();
            int curDeathRowSize = curDeathRow.size();
            for (int i = 0; i< curDeathRowSize; i++){
                newDeathRow.add(i, curDeathRow.get((curDeathRow.size()-1)-i));
            }
            curState.setDeathRow(newDeathRow);
            curState.addToMessage("The order of the line was reversed. ");
        }
        else if (name.equals("'Tis a Far Better Thing")){
            //Move a noble forward exactly 3 places in line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            if(deathRow.size()>3) {
                int randIndex = 3 + ((int) (Math.random() * (deathRow.size() - 3)));
                Noble card = deathRow.get(randIndex);
                deathRow.remove(randIndex);
                deathRow.add((randIndex - 3), card);
                curState.addToMessage(card.getNobleName() + " was moved forward three spaces. ");
            }
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Tough Crowd")){
            //Put this card in front of another player.  It is worth -2 points to that player.
            if (curState.getNumPlayers() == 2) {
                curState.setHasToughCrowd(0);
                curState.addToMessage("You received -2 points. ");
            }
            if (curState.getNumPlayers() == 3) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand > .5){
                        curState.setHasToughCrowd(0);
                        curState.addToMessage("You received -2 points. ");
                    }
                    else{
                        curState.setHasToughCrowd(2);
                        curState.addToMessage("Player 3 received -2 points. ");
                    }
                }
                else if (curState.getCurrentPlayer() == 2) {

                    if(rand>.5){
                        curState.setHasToughCrowd(0);
                        curState.addToMessage("You received -2 points. ");
                    }
                    else{
                        curState.setHasToughCrowd(1);
                        curState.addToMessage("Player 2 received -2 points. ");
                    }

                }
            }
            if (curState.getNumPlayers() == 4) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand < .33){
                        curState.setHasToughCrowd(0);
                        curState.addToMessage("You received -2 points. ");
                    }
                    else if(rand>.33 && rand<.66){
                        curState.setHasToughCrowd(2);
                        curState.addToMessage("Player 3 received -2 points. ");
                    }
                    else{
                        curState.setHasToughCrowd(3);
                        curState.addToMessage("Player 4 received -2 points. ");
                    }
                } else if (curState.getCurrentPlayer() == 2) {
                    if (rand < .33){
                        curState.setHasToughCrowd(0);
                        curState.addToMessage("You received -2 points. ");
                    }
                    else if(rand>.33 && rand<.66){
                        curState.setHasToughCrowd(1);
                        curState.addToMessage("Player 2 received -2 points. ");
                    }
                    else{
                        curState.setHasToughCrowd(3);
                        curState.addToMessage("Player 4 received -2 points. ");
                    }

                } else if(curState.getCurrentPlayer()==3){
                    if (rand < .33){
                        curState.setHasToughCrowd(0);
                        curState.addToMessage("You received -2 points. ");
                    }
                    else if(rand>.33 && rand<.66){
                        curState.setHasToughCrowd(1);
                        curState.addToMessage("Player 2 received -2 points. ");
                    }
                    else{
                        curState.setHasToughCrowd(2);
                        curState.addToMessage("Player 3 received -2 points. ");
                    }

                }
            }
        }
        else if (name.equals("Trip")){
            //Move a noble backward exactly 1 place in line. You may play another action card this turn.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            int randIndex = (int)(Math.random()*(deathRow.size()-1));
            Noble card = deathRow.get(randIndex);
            deathRow.remove(randIndex);
            deathRow.add((randIndex + 1), card);
            curState.setPlaySecondAction(true);
            curState.addToMessage(card.getNobleName() + " was moved backward one space. ");
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Was That My Name?")){
            //Move a noble forward up to 3 places in line.
            ArrayList<Noble> deathRow = curState.getDeathRow();
            if(deathRow.size()>3) {
                int randIndex = 3 + (int) (Math.random() * (deathRow.size() - 3));
                double randSpaces = Math.random();
                Noble card;
                if (randSpaces >= .33) {
                    card = deathRow.get(randIndex);
                    deathRow.remove(randIndex);
                    deathRow.add((randIndex - 1), card);
                    curState.addToMessage(deathRow.get(randIndex).getNobleName() + " was moved forward one space. ");
                } else if (randSpaces > .33 && randSpaces < .67) {
                    card = deathRow.get(randIndex);
                    deathRow.remove(randIndex);
                    deathRow.add((randIndex - 2), card);
                    curState.addToMessage(deathRow.get(randIndex).getNobleName() + " was moved forward two spaces. ");
                } else if (randSpaces >= .67) {
                    card = deathRow.get(randIndex);
                    deathRow.remove(randIndex);
                    deathRow.add((randIndex - 3), card);
                    curState.addToMessage(deathRow.get(randIndex).getNobleName() + " was moved forward three spaces. ");
                }
            }
            curState.setDeathRow(deathRow);
        }

        return curState;
    }
    public GuillotineState hardAIAction(GuillotineState curState) {
        //Put the noble at the front of the line into another player's score pile.
        // make if statements for every action card
        double rand = Math.random();

        if (name.equals("After You....")) {
            if (curState.getNumPlayers() == 2) {
                curState.collectNoble(0);

            }
            if (curState.getNumPlayers() == 3) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand > .5) {
                        curState.collectNoble(0);
                    } else {
                        curState.collectNoble(2);
                    }
                } else if (curState.getCurrentPlayer() == 2) {

                    if (rand > .5) {
                        curState.collectNoble(0);
                    } else {
                        curState.collectNoble(1);
                    }

                }
            }
            if (curState.getNumPlayers() == 4) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand < .33) {
                        curState.collectNoble(0);

                    } else if (rand > .33 && rand < .66) {
                        curState.collectNoble(2);
                    } else {
                        curState.collectNoble(3);
                    }
                } else if (curState.getCurrentPlayer() == 2) {
                    if (rand < .33) {
                        curState.collectNoble(0);

                    } else if (rand > .33 && rand < .66) {
                        curState.collectNoble(1);
                    } else {
                        curState.collectNoble(3);
                    }

                } else if (curState.getCurrentPlayer() == 3) {
                    if (rand < .33) {
                        curState.collectNoble(0);

                    } else if (rand > .33 && rand < .66) {
                        curState.collectNoble(1);
                    } else {
                        curState.collectNoble(2);
                    }

                }
            }
        } else if (name.equals("Bribed Guards")) {
            //Move the noble at the front of the line to the end of the line.
            Noble firstNob = curState.deathRow.get(0);
            curState.addToMessage(firstNob.getNobleName() + " was moved to the end of the line. ");
            curState.removeFromDeathRow(0);
            curState.deathRow.add(firstNob);

        } else if (name.equals("Church Support")) {
            //Put this card in front of you. It is worth +1 for each Blue noble in your score pile
            //Scoring is implemented in the calculateScore method of game state
            curState.setHasChurchSupport(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive +1 for each blue noble they collect. ");

        } else if (name.equals("Civic Pride")) {
            Integer currentN = 10;
            Integer currentI = null;
            ArrayList<Noble> deathRow = curState.getDeathRow();
            Noble currentNoble = null;
            boolean firstNoble = true;
            loop:
            {
                for (int i = 1; i < curState.getDeathRow().size(); i++) {
                    if (curState.getDeathRow().get(i).getNobleColor().equals("purple")) {
                        if (firstNoble) {
                            currentN = curState.getDeathRow().get(i).getNoblePoints();
                            currentNoble = curState.getDeathRow().get(i);
                            firstNoble = false;
                        }
                    } else if (currentN < curState.getDeathRow().get(i).getNoblePoints()) {
                        currentN = curState.getDeathRow().get(i).getNoblePoints();
                        currentNoble = curState.getDeathRow().get(i);
                        currentI = i;
                    }
                }
            }
            if (currentI != null && currentNoble != null) {
                deathRow.remove(currentI);
                deathRow.add(0, currentNoble);
                curState.addToMessage(currentNoble.getNobleName() + " was moved to the front of the line. ");
                curState.setDeathRow(deathRow);

            } else {
                curState.setCompleted(false);
            }

        } else if (name.equals("Civic Support")) {
            curState.setHasCivicSupport(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive +1 for each green noble they collect. ");
        } else if (name.equals("Clothing Swap")) {

            int randNob = (int) (Math.random() * (curState.deathRow.size()));
            Noble leavingNob = curState.deathRow.get(randNob);
            Noble arrivingNob = curState.nobleDeck.get(0);
            curState.deathRow.remove(randNob);
            curState.nobleDeck.remove(0);
            curState.deathRow.add(randNob, arrivingNob);
            curState.addToMessage(leavingNob.getNobleName() + " was replaced with " + arrivingNob.getNobleName() + ". ");
        } else if (name.equals("Confusion in Line")) {

            curState.shuffleDeathRow();
            curState.addToMessage("Death Row was shuffled. ");

        } else if (name.equals("Double Feature")) {

            if (curState.getCurrentPlayer() == 1) {
                curState.collectNoble(1);

            } else if (curState.getCurrentPlayer() == 2) {
                curState.collectNoble(2);
            } else if (curState.getCurrentPlayer() == 3) {
                curState.collectNoble(3);
            }
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " gets two nobles! ");
        } else if (name.equals("Escape!")) {
            if (curState.deathRow.size() > 2) {
                int randChosenNob1 = (int) (Math.random() * (curState.deathRow.size()));
                curState.nobleDiscard.add(curState.deathRow.get(randChosenNob1));
                curState.deathRow.remove(randChosenNob1);
                int randChosenNob2 = (int) (Math.random() * (curState.deathRow.size()));
                curState.nobleDiscard.add(curState.deathRow.get(randChosenNob2));
                curState.deathRow.remove(randChosenNob2);

                curState.shuffleDeathRow();

                curState.addToMessage(randChosenNob1 + " and " + randChosenNob2 + " were discarded. Death row was shuffled. ");

            }
        } else if (name.equals("Extra Cart")) {

            if (curState.deathRow.size() <= 9) {
                for (int i = 0; i < 3; i++) {
                    curState.deathRow.add(curState.nobleDeck.get(0));
                    curState.addToMessage(curState.nobleDeck.get(0).getNobleName() + " was added to Death Row. ");
                    curState.nobleDeck.remove(0);
                }

            } else if (curState.deathRow.size() == 10) {
                for (int i = 0; i < 2; i++) {
                    curState.deathRow.add(curState.nobleDeck.get(0));
                    curState.addToMessage(curState.nobleDeck.get(0).getNobleName() + " was added to Death Row. ");
                    curState.nobleDeck.remove(0);
                }
            } else if (curState.deathRow.size() == 11) {
                curState.deathRow.add(curState.nobleDeck.get(0));
                curState.addToMessage(curState.nobleDeck.get(0).getNobleName() + " was added to Death Row. ");
                curState.nobleDeck.remove(0);
            }

        } else if (name.equals("Fainting Spell")) {
            hardUpToMove(curState, -3);
        } else if (name.equals("Fled to England")) {

            int randEscapeNob = (int) (Math.random() * curState.deathRow.size());
            curState.nobleDiscard.add(curState.deathRow.get(randEscapeNob));
            curState.addToMessage(curState.deathRow.get(randEscapeNob).getNobleName() + " was discarded. ");
            curState.deathRow.remove(randEscapeNob);

        } else if (name.equals("Forced Break")) {
            if (curState.getNumPlayers() == 2) {
                int rand1 = (int) (Math.random() * curState.humanPlayerHand.size());

                curState.actionDiscard.add(curState.humanPlayerHand.get(rand1));
                curState.humanPlayerHand.remove(rand1);
            } else if (curState.getNumPlayers() == 3) {
                if (curState.getCurrentPlayer() == 1) {
                    int rand1 = (int) (Math.random() * curState.humanPlayerHand.size());
                    int rand3 = (int) (Math.random() * curState.computerPlayer2Hand.size());

                    curState.actionDiscard.add(curState.humanPlayerHand.get(rand1));
                    curState.actionDiscard.add(curState.computerPlayer2Hand.get(rand3));
                    curState.humanPlayerHand.remove(rand1);
                    curState.computerPlayer2Hand.remove(rand3);
                } else {
                    int rand1 = (int) (Math.random() * curState.humanPlayerHand.size());
                    int rand2 = (int) (Math.random() * curState.computerPlayer1Hand.size());

                    curState.actionDiscard.add(curState.humanPlayerHand.get(rand1));
                    curState.actionDiscard.add(curState.computerPlayer1Hand.get(rand2));
                    curState.humanPlayerHand.remove(rand1);
                    curState.computerPlayer1Hand.remove(rand2);
                }

            } else if (curState.getNumPlayers() == 4) {
                if (curState.getCurrentPlayer() == 1) {
                    int rand1 = (int) (Math.random() * curState.humanPlayerHand.size());
                    int rand3 = (int) (Math.random() * curState.computerPlayer2Hand.size());
                    int rand4 = (int) (Math.random() * curState.computerPlayer3Hand.size());

                    curState.actionDiscard.add(curState.humanPlayerHand.get(rand1));
                    curState.actionDiscard.add(curState.computerPlayer2Hand.get(rand3));
                    curState.actionDiscard.add(curState.computerPlayer3Hand.get(rand4));
                    curState.humanPlayerHand.remove(rand1);
                    curState.computerPlayer2Hand.remove(rand3);
                    curState.computerPlayer3Hand.remove(rand4);
                } else if (curState.getCurrentPlayer() == 2) {
                    int rand1 = (int) (Math.random() * curState.humanPlayerHand.size());
                    int rand2 = (int) (Math.random() * curState.computerPlayer1Hand.size());
                    int rand4 = (int) (Math.random() * curState.computerPlayer3Hand.size());

                    curState.actionDiscard.add(curState.humanPlayerHand.get(rand1));
                    curState.actionDiscard.add(curState.computerPlayer1Hand.get(rand2));
                    curState.actionDiscard.add(curState.computerPlayer3Hand.get(rand4));
                    curState.humanPlayerHand.remove(rand1);
                    curState.computerPlayer1Hand.remove(rand2);
                    curState.computerPlayer3Hand.remove(rand4);
                } else {
                    int rand1 = (int) (Math.random() * curState.humanPlayerHand.size());
                    int rand2 = (int) (Math.random() * curState.computerPlayer1Hand.size());
                    int rand3 = (int) (Math.random() * curState.computerPlayer2Hand.size());

                    curState.actionDiscard.add(curState.humanPlayerHand.get(rand1));
                    curState.actionDiscard.add(curState.computerPlayer1Hand.get(rand2));
                    curState.actionDiscard.add(curState.computerPlayer2Hand.get(rand3));
                    curState.humanPlayerHand.remove(rand1);
                    curState.computerPlayer1Hand.remove(rand2);
                    curState.computerPlayer2Hand.remove(rand3);
                }
            }
            curState.addToMessage("All other players discarded one action card. ");

        } else if (name.equals("Foreign Support")) {
            curState.setHasForeignSupport(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive one action card for each purple noble they collect. ");
        } else if (name.equals("Forward March")) {
            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 0; i < deathRow.size(); i++) {
                Noble card = deathRow.get(i);
                if (card.getNobleName().equals("Palace Guard")) {
                    deathRow.add(0, card);
                    deathRow.remove(i);
                    curState.addToMessage("A Palace Guard was moved to the front of the line. ");
                    break;
                }

            }
            curState.setDeathRow(deathRow);
        } else if (name.equals("Fountain of Blood")) {
            curState.setHasFountainOfBlood(curState.getCurrentPlayer());
            curState.addToMessage((curState.getCurrentPlayer() + 1) + " gets two bonus points.");
        } else if (name.equals("Friend of the Queen")) {
            hardUpToMove(curState, -2);
        } else if (name.equals("Ignoble Noble")) {
            hardExactMove(curState, 4);
        } else if (name.equals("Indifferent Public")) {
            curState.setHasIndifferentPublic(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive one point for each gray noble instead of its normal value. ");

        } else if (name.equals("Information Exchange")) {
            ArrayList<ActionCard> curPlayerHand;
            if (curState.getCurrentPlayer() == 2) {
                curPlayerHand = curState.computerPlayer1Hand;
                curState.computerPlayer1Hand = curState.humanPlayerHand;
                curState.humanPlayerHand = curPlayerHand;
            } else if (curState.getCurrentPlayer() == 3) {
                curPlayerHand = curState.computerPlayer1Hand;
                curState.computerPlayer2Hand = curState.humanPlayerHand;
                curState.humanPlayerHand = curPlayerHand;
            } else if (curState.getCurrentPlayer() == 4) {
                curPlayerHand = curState.computerPlayer1Hand;
                curState.computerPlayer3Hand = curState.humanPlayerHand;
                curState.humanPlayerHand = curPlayerHand;
            }
        } else if (name.equals("Lack of Faith")) {
            for (int i = 0; i < curState.deathRow.size(); i++) {
                if (curState.deathRow.get(i).getNobleColor().equals("blue")) {
                    Noble card = curState.deathRow.get(i);
                    curState.deathRow.remove(i);
                    curState.deathRow.add(0, card);
                    curState.addToMessage(card.getNobleName() + " was moved to the front of the line. ");
                    break;
                } else {
                    curState.setCompleted(false);
                }
            }
        } else if (name.equals("Late Arrival")) {
            ArrayList<Noble> deathRow = curState.getDeathRow();
            double random = Math.random();
            if (deathRow.size() >= 3) {
                Noble card;
                if (random <= .33) {
                    card = curState.getNobleDeck().get(0);
                    curState.nobleDeck.remove(0);
                    deathRow.add(card);
                    curState.addToMessage(card.getNobleName() + " was added to the end of Death Row. ");
                } else if (random > .33 && random <= .67) {
                    card = curState.getNobleDeck().get(1);
                    curState.nobleDeck.remove(1);
                    deathRow.add(card);
                    curState.addToMessage(card.getNobleName() + " was added to the end of Death Row. ");
                } else if (random > .67) {
                    card = curState.getNobleDeck().get(2);
                    curState.nobleDeck.remove(2);
                    deathRow.add(card);
                    curState.addToMessage(card.getNobleName() + " was added to the end of Death Row. ");
                }

            }
            curState.setDeathRow(deathRow);
        } else if (name.equals("Let Them Eat Cake")) {
            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 1; i < deathRow.size(); i++) {
                Noble card = deathRow.get(i);
                if (card.getNobleName().equals("Marie Antoinette")) {
                    deathRow.remove(i);
                    deathRow.add(0, card);
                    curState.addToMessage("Marie Antoinette was moved to the front of the line. ");
                    break;
                }
            }
            curState.setDeathRow(deathRow);
        } else if (name.equals("L'Idiot")) {
            hardUpToMove(curState, 2);
        } else if (name.equals("Majesty")) {
            boolean done = false;
            Integer currentN = 10;
            Integer currentI = null;
            ArrayList<Noble> deathRow = curState.getDeathRow();
            Noble currentNoble = null;
            boolean firstNoble = true;
            loop:
            {
                for (int i = 1; i < curState.getDeathRow().size(); i++) {
                    if (curState.getDeathRow().get(i).getNobleColor().equals("purple")) {
                        if (firstNoble) {
                            currentN = curState.getDeathRow().get(i).getNoblePoints();
                            currentNoble = curState.getDeathRow().get(i);
                            firstNoble = false;
                        } else if (curState.getDeathRow().get(i).getNobleName().equals("Count") || curState.getDeathRow().get(i).getNobleName().equals("Countess") && hasCountOrCountess(curState) == true) {
                            currentNoble = curState.getDeathRow().get(i);
                            deathRow.remove(i);
                            deathRow.add(0, currentNoble);
                            curState.addToMessage(currentNoble.getNobleName() + " was moved to the front of the line. ");
                            curState.setDeathRow(deathRow);
                            done = true;
                            break loop;
                        } else if (currentN < curState.getDeathRow().get(i).getNoblePoints()) {
                            currentN = curState.getDeathRow().get(i).getNoblePoints();
                            currentNoble = curState.getDeathRow().get(i);
                            currentI = i;
                        }
                    }
                }
            }
            if (done == false && currentI != null && currentNoble != null) {
                deathRow.remove(currentI);
                deathRow.add(0, currentNoble);
                curState.addToMessage(currentNoble.getNobleName() + " was moved to the front of the line. ");
                curState.setDeathRow(deathRow);

            } else {
                curState.setCompleted(false);
            }

        }
        else if (name.equals("Mass Confusion"))

        {
            ArrayList<Noble> deathRow = curState.getDeathRow();
            int numNobles = deathRow.size();
            ArrayList<Noble> currentNobleDeck = curState.getNobleDeck();

            for (int i = 0; i < deathRow.size(); i++) {
                Noble card = deathRow.get(i);
                deathRow.remove(i);
                currentNobleDeck.add(card);
            }
            curState.setNobleDeck(currentNobleDeck);
            curState.shuffleNobleDeck();
            curState.createDeathRow(numNobles);
        }
        else if (name.equals("Military Might"))

        {
            Integer currentN = 10;
            Integer currentI = null;
            boolean done = false;
            ArrayList<Noble> deathRow = curState.getDeathRow();
            Noble currentNoble = null;
            boolean firstNoble = true;
            loop:
            {
                for (int i = 1; i < curState.getDeathRow().size(); i++) {
                    if (curState.getDeathRow().get(i).getNobleColor().equals("purple")) {
                        if (firstNoble) {
                            currentN = curState.getDeathRow().get(i).getNoblePoints();
                            currentNoble = curState.getDeathRow().get(i);
                            firstNoble = false;
                        } else if (curState.getDeathRow().get(i).getNobleName().equals("Palace Guard")) {
                            currentNoble = curState.getDeathRow().get(i);
                            deathRow.remove(i);
                            deathRow.add(0, currentNoble);
                            curState.addToMessage(currentNoble.getNobleName() + " was moved to the front of the line. ");
                            curState.setDeathRow(deathRow);
                            done = true;
                            break loop;
                        } else if (currentN < curState.getDeathRow().get(i).getNoblePoints()) {
                            currentN = curState.getDeathRow().get(i).getNoblePoints();
                            currentNoble = curState.getDeathRow().get(i);
                            currentI = i;

                        }
                    }

                }
            }
            if (done == false && currentI != null && currentNoble != null) {
                deathRow.remove(currentI);
                deathRow.add(0, currentNoble);
                curState.addToMessage(currentNoble.getNobleName() + " was moved to the front of the line. ");
                curState.setDeathRow(deathRow);

            } else {
                curState.setCompleted(false);
            }


        }
        else if (name.equals("Military Support")) {
            curState.setHasMilitarySupport(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive +1 for each red noble they collect. ");

        } else if (name.equals("Milling in Line")) {
            ArrayList<Noble> deathRow = curState.getDeathRow();
            ArrayList<Noble> firstFive = new ArrayList<Noble>();

            Noble one = deathRow.get(0);
            firstFive.add(one);

            if (deathRow.size() >= 2) {
                Noble two = deathRow.get(1);
                firstFive.add(two);
            }
            if (deathRow.size() >= 3) {
                Noble three = deathRow.get(2);
                firstFive.add(three);
            }
            if (deathRow.size() >= 4) {
                Noble four = deathRow.get(3);
                firstFive.add(four);
            }
            if (deathRow.size() >= 5) {
                Noble five = deathRow.get(4);
                firstFive.add(five);
            }


            int numGoThrough = 5;
            if (curState.deathRow.size() < 5) {
                numGoThrough = curState.deathRow.size();
            }

            Collections.shuffle(firstFive);
            for (int i = 0; i < numGoThrough; i++) {
                curState.removeFromDeathRow(0);
            }
            for (int i = 0; i < numGoThrough; i++) {
                deathRow.add(i, firstFive.get(i));
            }

            curState.addToMessage("The first five nobles in line were rearranged. ");
            curState.setDeathRow(deathRow);
        } else if (name.equals("Missed!")) {
            if (curState.getHumanPlayerNobles().size() != 0) {
                Noble card = curState.getHumanPlayerNobles().get(curState.getHumanPlayerNobles().size() - 1);
                curState.humanPlayerNobles.remove(curState.getHumanPlayerNobles().size() - 1);
                curState.deathRow.add(card);
                //curState.addToMessage(card.getNobleName() + " was moved from your hand to death row. ");
            } else {
                curState.setCompleted(false);
            }

        }
        //FIX THIS!!!! if zero noble cards
        else if (name.equals("Missing Heads")) {
            if (curState.getHumanPlayerNobles().size() != 0) {
                int cardNum = (int) (rand * curState.humanPlayerNobles.size());
                Noble card = curState.humanPlayerNobles.get(cardNum);
                //curState.addToMessage("You lost " + card.getNobleName() + ". ");
                curState.humanPlayerNobles.remove(cardNum);
            }
            else
            {
                curState.setCompleted(false);
            }
        } else if (name.equals("Opinionated Guards")) {
            ArrayList<Noble> deathRow = curState.getDeathRow();
            ArrayList<Noble> firstFour = new ArrayList<Noble>();

            Noble one = deathRow.get(0);
            firstFour.add(one);

            if (deathRow.size() >= 2) {
                Noble two = deathRow.get(1);
                firstFour.add(two);
            }
            if (deathRow.size() >= 3) {
                Noble three = deathRow.get(2);
                firstFour.add(three);
            }
            if (deathRow.size() >= 4) {
                Noble four = deathRow.get(3);
                firstFour.add(four);
            }

            int numGoThrough = 4;
            if (curState.deathRow.size() < 4) {
                numGoThrough = curState.deathRow.size();
            }

            Collections.shuffle(firstFour);

            for (int i = 0; i < numGoThrough; i++) {
                curState.removeFromDeathRow(0);
            }
            for (int i = 0; i < numGoThrough; i++) {
                deathRow.add(i, firstFour.get(i));
            }

            curState.addToMessage("The first four nobles in line were rearranged. ");
            curState.setDeathRow(deathRow);

        } else if (name.equals("Political Influence")) {
            curState.drawActionCard(curState.getCurrentPlayer());
            curState.drawActionCard(curState.getCurrentPlayer());
            curState.drawActionCard(curState.getCurrentPlayer());
            curState.setNobleCollectOk(false);
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " collected three action cards and no noble. ");
        } else if (name.equals("Public Demand")) {
            {
                Integer currentI = null;
                boolean done = false;
                Integer currentN = 10;
                ArrayList<Noble> deathRow = curState.getDeathRow();
                Noble currentNoble = null;
                loop:
                {
                    for (int i = 1; i < curState.getDeathRow().size(); i++) {
                        if (i == 1) {
                            currentN = curState.getDeathRow().get(i).getNoblePoints();
                            currentNoble = curState.getDeathRow().get(i);
                        } else if (curState.getDeathRow().get(i).getNobleName().equals("Palace Guard")) {
                            currentNoble = curState.getDeathRow().get(i);
                            deathRow.remove(i);
                            deathRow.add(0, currentNoble);
                            curState.addToMessage(currentNoble.getNobleName() + " was moved to the front of the line. ");
                            curState.setDeathRow(deathRow);
                            done = true;
                            break loop;
                        } else if (curState.getDeathRow().get(i).getNobleName().equals("Count") || curState.getDeathRow().get(i).getNobleName().equals("Countess") && hasCountOrCountess(curState) == true) {
                            currentNoble = curState.getDeathRow().get(i);
                            deathRow.remove(i);
                            deathRow.add(0, currentNoble);
                            curState.addToMessage(currentNoble.getNobleName() + " was moved to the front of the line. ");
                            curState.setDeathRow(deathRow);
                            done = true;
                            break loop;
                        } else if (currentN < curState.getDeathRow().get(i).getNoblePoints()) {
                            currentN = curState.getDeathRow().get(i).getNoblePoints();
                            currentNoble = curState.getDeathRow().get(i);
                            currentI = i;
                        }
                    }
                    if (done == false && currentI != null && currentNoble != null) {
                        deathRow.remove(currentI);
                        deathRow.add(0, currentNoble);
                        curState.addToMessage(currentNoble.getNobleName() + " was moved to the front of the line. ");
                        curState.setDeathRow(deathRow);
                    } else {
                        curState.setCompleted(false);
                    }
                }
            }
        } else if (name.equals("Pushed")) {
                hardExactMove(curState, 2);
        } else if (name.equals("Rain Delay")) {
            if (curState.getNumPlayers() == 2) {
                curState.getHumanPlayerHand().clear();
                curState.getComputerPlayer1Hand().clear();
                curState.deal();
            } else if (curState.getNumPlayers() == 3) {
                curState.getHumanPlayerHand().clear();
                curState.getComputerPlayer1Hand().clear();
                curState.getComputerPlayer2Hand().clear();
                curState.deal();
            } else if (curState.getNumPlayers() == 4) {
                curState.getHumanPlayerHand().clear();
                curState.getComputerPlayer1Hand().clear();
                curState.getComputerPlayer2Hand().clear();
                curState.getComputerPlayer3Hand().clear();
                curState.deal();
            }
            curState.addToMessage("All players' hands were shuffled into the deck and each player was dealt five new action cards. ");
        } else if (name.equals("Scarlet Pimpernel")) {
            Noble card = curState.getDeathRow().get(0);
            curState.deathRow.clear();
            curState.deathRow.add(card);
        } else if (name.equals("Stumble")) {
                hardExactMove(curState, 1);
        } else if (name.equals("The Long Walk")) {
            ArrayList<Noble> curDeathRow = curState.getDeathRow();
            ArrayList<Noble> newDeathRow = new ArrayList<Noble>();
            int curDeathRowSize = curDeathRow.size();
            for (int i = 0; i < curDeathRowSize; i++) {
                newDeathRow.add(i, curDeathRow.get((curDeathRow.size() - 1) - i));
            }
            curState.setDeathRow(newDeathRow);
            curState.addToMessage("The order of the line was reversed. ");
        } else if (name.equals("'Tis a Far Better Thing")) {
                hardExactMove(curState, 3);
        } else if (name.equals("Tough Crowd")) {
                curState.setHasToughCrowd(0);

        } else if (name.equals("Trip")) {
                hardUpToMove(curState, -1);
                curState.setPlaySecondAction(true);
        } else if (name.equals("Was That My Name?")) {
                hardUpToMove(curState, 3);
        }

        return curState;
    }
            public void hardExactMove(GuillotineState state, int i) {
                ArrayList<Noble> deathRow = state.getDeathRow();
                if (state.getDeathRow().size() >= i + 1) {
                    Noble card;
                    if (deathRow.get(i).getNoblePoints() > deathRow.get(0).getNoblePoints()) {
                        card = deathRow.get(i);
                        deathRow.remove(i);
                        deathRow.add(0, card);
                        //state.addToMessage(card.getNobleName() + "was moved forward " + i + " space/s. ");
                        state.setDeathRow(deathRow);
                    }

                }
            }

            public void hardUpToMove(GuillotineState state, int i) {
                ArrayList<Noble> deathRow = state.getDeathRow();
                int noble0Points = state.getDeathRow().get(0).getNoblePoints();
                int noble1Points = state.getDeathRow().get(1).getNoblePoints();
                int noble2Points = state.getDeathRow().get(2).getNoblePoints();
                int noble3Points = state.getDeathRow().get(3).getNoblePoints();
                Noble card;
                if (i > 0) {
                    if (noble3Points > noble0Points && noble3Points > noble2Points && noble3Points > noble1Points && i == 3 && state.getDeathRow().size() >= 4) {
                        card = state.getDeathRow().get(3);
                        deathRow.remove(3);
                        deathRow.add(0, card);
                        state.addToMessage(card.getNobleName() + "was moved forward three spaces");
                        state.setDeathRow(deathRow);
                    } else if (noble2Points > noble0Points && noble2Points > noble3Points && noble2Points > noble1Points && state.getDeathRow().size() >= 3) {
                        card = state.getDeathRow().get(2);
                        deathRow.remove(2);
                        deathRow.add(0, card);
                        state.addToMessage(card.getNobleName() + "was moved forward two spaces");
                        state.setDeathRow(deathRow);
                    } else if (noble1Points > noble0Points && noble1Points > noble2Points && noble1Points > noble3Points && state.getDeathRow().size() >= 2) {
                        card = state.getDeathRow().get(1);
                        deathRow.remove(1);
                        deathRow.add(0, card);
                        state.addToMessage(card.getNobleName() + "was moved forward one space");
                        state.setDeathRow(deathRow);
                    } else {
                        if (deathRow.size() > 3) {
                            int randIndex = 3 + (int) (Math.random() * (deathRow.size() - 3));
                            double randSpaces = Math.random();
                            if (randSpaces >= .33) {
                                card = deathRow.get(randIndex);
                                deathRow.remove(randIndex);
                                deathRow.add((randIndex - 1), card);
                                state.addToMessage(deathRow.get(randIndex).getNobleName() + " was moved forward one space. ");
                                state.setDeathRow(deathRow);
                            } else if (randSpaces > .33 && randSpaces < .67) {
                                card = deathRow.get(randIndex);
                                deathRow.remove(randIndex);
                                deathRow.add((randIndex - 2), card);
                                state.addToMessage(deathRow.get(randIndex).getNobleName() + " was moved forward two spaces. ");
                                state.setDeathRow(deathRow);
                            } else if (randSpaces >= .67) {
                                card = deathRow.get(randIndex);
                                deathRow.remove(randIndex);
                                deathRow.add((randIndex - 3), card);
                                state.addToMessage(deathRow.get(randIndex).getNobleName() + " was moved forward three spaces. ");
                                state.setDeathRow(deathRow);
                            }
                        }
                        state.setDeathRow(deathRow);
                    }
                } else if (i < 0) {
                    if (i == -1) {
                        if (noble0Points < noble1Points) {
                            card = deathRow.get(1);
                            deathRow.remove(1);
                            deathRow.add(0, card);
                            state.addToMessage(card.getNobleName() + "was moved backward one space");
                            state.setDeathRow(deathRow);
                        } else if (state.getCurrentPlayer() == 2 && (noble3Points > state.getDeathRow().get(4).getNoblePoints() || state.getDeathRow().get(3).getNobleName().equals("Palace Guard") || state.getDeathRow().get(3).getNobleName().equals("Count") || state.getDeathRow().get(3).getNobleName().equals("Countess"))) {
                            card = deathRow.get(3);
                            deathRow.remove(3);
                            deathRow.trimToSize();
                            deathRow.add(4, card);
                            state.addToMessage(card.getNobleName() + "was moved backward one space");
                            state.setDeathRow(deathRow);
                        } else if (state.getCurrentPlayer() == 2 && (noble2Points < noble3Points || state.getDeathRow().get(3).getNobleName().equals("Palace Guard") || state.getDeathRow().get(3).getNobleName().equals("Count") || state.getDeathRow().get(3).getNobleName().equals("Countess"))) {
                            card = deathRow.get(2);
                            deathRow.remove(2);
                            deathRow.trimToSize();
                            deathRow.add(3, card);
                            state.addToMessage(card.getNobleName() + "was moved backward one space");
                            state.setDeathRow(deathRow);
                        } else if (state.getCurrentPlayer() == 3 && (noble2Points > noble3Points || state.getDeathRow().get(2).getNobleName().equals("Palace Guard") || state.getDeathRow().get(2).getNobleName().equals("Count") || state.getDeathRow().get(2).getNobleName().equals("Countess"))) {
                            card = deathRow.get(2);
                            deathRow.remove(2);
                            deathRow.trimToSize();
                            deathRow.add(3, card);
                            state.addToMessage(card.getNobleName() + "was moved backward one space");
                            state.setDeathRow(deathRow);
                        } else if (state.getCurrentPlayer() == 3 && (noble1Points < noble2Points || state.getDeathRow().get(2).getNobleName().equals("Palace Guard") || state.getDeathRow().get(2).getNobleName().equals("Count") || state.getDeathRow().get(2).getNobleName().equals("Countess"))) {
                            card = deathRow.get(1);
                            deathRow.remove(1);
                            deathRow.trimToSize();
                            deathRow.add(2, card);
                            state.addToMessage(card.getNobleName() + "was moved backward one space");
                            state.setDeathRow(deathRow);
                        } else if (state.getCurrentPlayer() == 4 && (noble1Points > noble2Points || state.getDeathRow().get(1).getNobleName().equals("Palace Guard") || state.getDeathRow().get(1).getNobleName().equals("Count") || state.getDeathRow().get(1).getNobleName().equals("Countess"))) {
                            card = deathRow.get(1);
                            deathRow.remove(1);
                            deathRow.trimToSize();
                            deathRow.add(2, card);
                            state.addToMessage(card.getNobleName() + "was moved backward one space");
                            state.setDeathRow(deathRow);
                        } else if (state.getCurrentPlayer() == 4 && (noble0Points < noble1Points || state.getDeathRow().get(1).getNobleName().equals("Palace Guard") || state.getDeathRow().get(1).getNobleName().equals("Count") || state.getDeathRow().get(1).getNobleName().equals("Countess"))) {
                            card = deathRow.get(0);
                            deathRow.remove(0);
                            deathRow.trimToSize();
                            deathRow.add(1, card);
                            state.addToMessage(card.getNobleName() + "was moved backward one space");
                            state.setDeathRow(deathRow);
                        }
                    } else if (i == -2) {
                        if (noble0Points < noble1Points) {
                            card = deathRow.get(1);
                            deathRow.remove(1);
                            deathRow.add(0, card);
                            state.addToMessage(deathRow.get(0).getNobleName() + "was moved backwards one space");
                            state.setDeathRow(deathRow);
                        } else {
                            int randIndex = (int) (Math.random() * (deathRow.size() - 2));
                            card = deathRow.get(randIndex);
                            double randSpaces = Math.random();
                            deathRow.remove(randIndex);
                            if (randSpaces >= .5) {
                                deathRow.add((randIndex + 1), card);
                                state.addToMessage(card.getNobleName() + "was moved backward one space. ");
                            } else if (randSpaces < .5) {
                                deathRow.add((randIndex + 2), card);
                                state.addToMessage(card.getNobleName() + "was moved backward two spaces. ");
                            }
                            state.setDeathRow(deathRow);
                        }
                    } else if (i == -3) {
                        if (noble0Points < noble1Points) {
                            card = deathRow.get(1);
                            deathRow.remove(1);
                            deathRow.add(0, card);
                            state.addToMessage(deathRow.get(0).getNobleName() + "was moved backwards one space");
                            state.setDeathRow(deathRow);
                        } else {
                            if (deathRow.size() >= 4) {
                                int randIndex = (int) (Math.random() * (deathRow.size() - 3));
                                double randSpaces = Math.random();
                                String nobleName;
                                if (randSpaces >= .33) {
                                    nobleName = deathRow.get(randIndex).getNobleName();
                                    deathRow.add((randIndex + 1), deathRow.get(randIndex));
                                    deathRow.remove(randIndex);
                                    state.addToMessage(nobleName + " was moved backward one space. ");
                                } else if (randSpaces > .33 && randSpaces < .67) {
                                    nobleName = deathRow.get(randIndex).getNobleName();
                                    deathRow.add((randIndex + 2), deathRow.get(randIndex));
                                    deathRow.remove(randIndex);
                                    state.addToMessage(nobleName + " was moved backward two spaces. ");
                                } else if (randSpaces >= .67) {
                                    nobleName = deathRow.get(randIndex).getNobleName();
                                    deathRow.add((randIndex + 3), deathRow.get(randIndex));
                                    deathRow.remove(randIndex);
                                    state.addToMessage(nobleName + " was moved backward three spaces. ");
                                }
                            }
                            state.setDeathRow(deathRow);
                        }
                    }
                }
            }

            public boolean hasCountOrCountess(GuillotineState state) {
                if (state.getCurrentPlayer() == 2) {
                    for (int i = 0; i < state.computerPlayer1Hand.size(); i++) {
                        if (state.getComputerPlayer1Hand().get(i).getName().equals("Count") || state.getComputerPlayer1Hand().get(i).getName().equals("Countess")) {
                            return true;
                        }
                    }
                    return false;
                } else if (state.getCurrentPlayer() == 3) {
                    for (int i = 0; i < state.computerPlayer2Hand.size(); i++) {
                        if (state.getComputerPlayer2Hand().get(i).getName().equals("Count") || state.getComputerPlayer2Hand().get(i).getName().equals("Countess")) {
                            return true;
                        }
                    }
                    return false;
                } else if (state.getCurrentPlayer() == 4) {
                    for (int i = 0; i < state.computerPlayer3Hand.size(); i++) {
                        if (state.getComputerPlayer3Hand().get(i).getName().equals("Count") || state.getComputerPlayer3Hand().get(i).getName().equals("Countess")) {
                            return true;
                        }
                    }
                }
                return false;
            }

        }
