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
 * @version November 2015
 */
public class ActionCard implements Serializable {
    private static final long serialVersionUID = 7L;

    private String name;
    private int image;
    public ActionCard(String givenName, int pic)
    {
        this.name = givenName;
        this.image = pic;
    }

    public int getImage(){
        return image;
    }
    public String getName() {return name; }
    public GuillotineState humanAction(GuillotineState curState){
        // make if statements for every action card

        double rand = Math.random();

        if(name.equals("After You....")){

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

            Noble firstNob = curState.deathRow.get(0);
            curState.addToMessage(firstNob.getNobleName() + " was moved to the end of the line. ");
            curState.removeFromDeathRow(0);
            curState.deathRow.add(firstNob);

        }
        else if (name.equals("Church Support")){

            curState.setHasChurchSupport(curState.getCurrentPlayer());
            curState.addToMessage("You will receive +1 for each blue noble you collect. ");

        }
        else if (name.equals("Civic Pride")){

            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i<deathRow.size();i++){
                if(deathRow.get(i).getNobleColor() == "green"){
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

            curState.setHasChurchSupport(curState.getCurrentPlayer());
            curState.addToMessage("You will receive +1 for each blue noble you collect. ");


        }
        else if (name.equals("Clothing Swap")){
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

            curState.shuffleDeathRow();
            curState.addToMessage("Death Row was shuffled. ");

        }
        else if (name.equals("Double Feature")){

            curState.collectNoble(0);

        curState.addToMessage("You get two nobles! ");

        }
        else if (name.equals("Escape!")){
            int randChosenNob1 = (int)(Math.random()*(curState.deathRow.size()));
            curState.nobleDiscard.add(curState.deathRow.get(randChosenNob1));
            curState.deathRow.remove(randChosenNob1);
            int randChosenNob2 = (int)(Math.random()*(curState.deathRow.size()));
            curState.nobleDiscard.add(curState.deathRow.get(randChosenNob2));
            curState.deathRow.remove(randChosenNob2);

            curState.shuffleDeathRow();
        }
        else if (name.equals("Extra Cart")){
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

            curState.setHasForeignSupport(curState.getCurrentPlayer());
            curState.addToMessage("You will receive one action card for each purple noble you collect. ");


        }
        else if (name.equals("Forward March")){

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

            curState.setHasFountainOfBlood(curState.getCurrentPlayer());
            curState.addToMessage(("You get two bonus points."));

        }
        else if (name.equals("Friend of the Queen")){

            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble form line

            if(curState.getIntPopUpRecievedInfo()!=-1) {

                Noble card = curState.deathRow.get(curState.getIntPopUpRecievedInfo());

                double randSpaces = Math.random();
                curState.deathRow.remove(curState.getIntPopUpRecievedInfo());
                if (randSpaces>=.5)
                {
                    curState.deathRow.add((curState.getIntPopUpRecievedInfo()+1), card);
                    curState.addToMessage(card.getNobleName() + " was moved backward one space. ");
                }
                else if (randSpaces<.5)
                {
                    curState.deathRow.add((curState.getIntPopUpRecievedInfo()+2), card);
                    curState.addToMessage(card.getNobleName() + " was moved backward two spaces. ");
                }
                curState.setDeathRow(curState.deathRow);


                curState.setIntPopUpRecievedInfo(-1);
                curState.setNeedPopUp(false);

            }




        }
        else if (name.equals("Ignoble Noble")){

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
            curState.setHasIndifferentPublic(curState.getCurrentPlayer());
            curState.addToMessage("You will receive one point for each gray noble instead of its normal value. ");

        }
        else if (name.equals("Information Exchange")){

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

            for(int i = 0; i<curState.deathRow.size();i++){
                if(curState.deathRow.get(i).getNobleColor() == "blue"){
                    Noble card = curState.deathRow.get(i);
                    curState.deathRow.remove(i);
                    curState.deathRow.add(0,card);
                    curState.addToMessage(card.getNobleName() + " was moved to the front of the line. ");
                    break;
                }
            }

        }
        else if (name.equals("Late Arrival")){

            curState.setNeedPopUp(true);
            curState.setPopUpType(1); //1 = choose a noble form line

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

            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i<deathRow.size();i++){
                if(deathRow.get(i).getNobleColor() == "purple"){
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

            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i<deathRow.size();i++){
                if(deathRow.get(i).getNobleColor() == "red"){
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

            curState.setHasMilitarySupport(curState.getCurrentPlayer());
            curState.addToMessage("You will receive +1 for each red noble you collect. ");

        }
        else if (name.equals("Milling in Line")){
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
            /*deathRow.add(0, firstFive.get(0));
            deathRow.add(1, firstFive.get(1));
            deathRow.add(2, firstFive.get(2));
            deathRow.add(3, firstFive.get(3));
            deathRow.add(4, firstFive.get(4));*/
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Missed!")){



            if (curState.getNumPlayers() == 2) {
                Noble card = curState.computerPlayer1Nobles.get(curState.computerPlayer1Nobles.size()-1);
                curState.computerPlayer1Nobles.remove(curState.computerPlayer1Nobles.size() - 1);
                curState.deathRow.add(card);
                curState.addToMessage(card.getNobleName() + " was moved from Player 2's hand to death row. ");

            }
            if (curState.getNumPlayers() == 3) {

                        Noble card = curState.computerPlayer2Nobles.get(curState.computerPlayer2Nobles.size()-1);
                        curState.computerPlayer2Nobles.remove(curState.computerPlayer2Nobles.size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 3's hand to death row. ");


            }
            if (curState.getNumPlayers() == 4) {


                Noble card = curState.computerPlayer3Nobles.get(curState.computerPlayer3Nobles.size() - 1);
                curState.computerPlayer3Nobles.remove(curState.computerPlayer3Nobles.size() - 1);
                curState.deathRow.add(card);
                curState.addToMessage(card.getNobleName() + " was moved from Player 4's hand to death row. ");


            }


        }
        else if (name.equals("Missing Heads")){

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

            curState.drawActionCard(curState.getCurrentPlayer());
            curState.drawActionCard(curState.getCurrentPlayer());
            curState.drawActionCard(curState.getCurrentPlayer());
            curState.setNobleCollectOk(false);
            curState.addToMessage("You have collected three action cards and no noble. ");

        }
        else if (name.equals("Public Demand")){

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

            Noble card = curState.getDeathRow().get(0);
            curState.deathRow.clear();
            curState.deathRow.add(card);

        }
        else if (name.equals("Stumble")){

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

            Noble firstNob = curState.deathRow.get(0);
            curState.addToMessage(firstNob.getNobleName() + " was moved to the end of the line. ");
            curState.removeFromDeathRow(0);
            curState.deathRow.add(firstNob);

        }
        else if (name.equals("Church Support")){
            curState.setHasChurchSupport(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer()+1) + " will receive +1 for each blue noble they collect. ");

        }
        else if (name.equals("Civic Pride")){
            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i<deathRow.size();i++){
                if(deathRow.get(i).getNobleColor() == "green"){
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
            curState.setHasCivicSupport(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive +1 for each green noble they collect. ");
        }
        else if (name.equals("Clothing Swap")){

            int randNob = (int)(Math.random()*(curState.deathRow.size()));
            Noble leavingNob = curState.deathRow.get(randNob);
            Noble arrivingNob = curState.nobleDeck.get(0);
            curState.deathRow.remove(randNob);
            curState.nobleDeck.remove(0);
            curState.deathRow.add(randNob, arrivingNob);
            curState.addToMessage(leavingNob.getNobleName() + " was replaced with " + arrivingNob.getNobleName() + ". ");
        }
        else if (name.equals("Confusion in Line")){

            curState.shuffleDeathRow();
            curState.addToMessage("Death Row was shuffled. ");

        }
        else if (name.equals("Double Feature")){

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

            int randEscapeNob = (int)(Math.random()*curState.deathRow.size());
            curState.nobleDiscard.add(curState.deathRow.get(randEscapeNob));
            curState.addToMessage(curState.deathRow.get(randEscapeNob).getNobleName() + " was discarded. ");
            curState.deathRow.remove(randEscapeNob);

        }
        else if (name.equals("Forced Break")){
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
            curState.setHasForeignSupport(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive one action card for each purple noble they collect. ");
        }
        else if (name.equals("Forward March")){
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
            curState.setHasFountainOfBlood(curState.getCurrentPlayer());
            curState.addToMessage((curState.getCurrentPlayer()+1) + " gets two bonus points.");
        }
        else if (name.equals("Friend of the Queen")){
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
            curState.setHasIndifferentPublic(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive one point for each gray noble instead of its normal value. ");

        }
        else if (name.equals("Information Exchange")){
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
            for(int i = 0; i<curState.deathRow.size();i++){
                if(curState.deathRow.get(i).getNobleColor() == "blue"){
                    Noble card = curState.deathRow.get(i);
                    curState.deathRow.remove(i);
                    curState.deathRow.add(0,card);
                    curState.addToMessage(card.getNobleName() + " was moved to the front of the line. ");
                    break;
                }
            }
        }
        else if (name.equals("Late Arrival")){
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
            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i<deathRow.size();i++){
                if(deathRow.get(i).getNobleColor() == "purple"){
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
            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i<deathRow.size();i++){
                if(deathRow.get(i).getNobleColor() == "red"){
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
            curState.setHasMilitarySupport(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive +1 for each red noble they collect. ");

        }
        else if (name.equals("Milling in Line")) {
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
        //FIX THIS!!!! if zero noble cards
        else if (name.equals("Missing Heads")){
            if (curState.getNumPlayers() == 2) {
                int cardNum = (int)(rand*curState.humanPlayerNobles.size());
                Noble card = curState.humanPlayerNobles.get(cardNum);
                curState.addToMessage("You lost "+ card.getNobleName() + ". ");
                curState.humanPlayerNobles.remove(cardNum);

            }
            if (curState.getNumPlayers() == 3) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand > .5){
                        int cardNum = (int)(rand*curState.humanPlayerNobles.size());
                        Noble card = curState.humanPlayerNobles.get(cardNum);
                        curState.addToMessage("You lost "+ card.getNobleName() + ". ");
                        curState.humanPlayerNobles.remove(cardNum);
                    }
                    else{
                        int cardNum = (int)(rand*curState.computerPlayer2Nobles.size());
                        Noble card = curState.computerPlayer2Nobles.get(cardNum);
                        curState.addToMessage("Player 3 lost "+ card.getNobleName() + ". ");
                        curState.computerPlayer2Nobles.remove(cardNum);
                    }
                }
                else if (curState.getCurrentPlayer() == 2) {

                    if(rand>.5){
                        int cardNum = (int)(rand*curState.humanPlayerNobles.size());
                        if(cardNum>0) {
                            Noble card = curState.humanPlayerNobles.get(cardNum);
                            curState.addToMessage("You lost " + card.getNobleName() + ". ");
                            curState.humanPlayerNobles.remove(cardNum);
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
                        int cardNum = (int)(rand*curState.humanPlayerNobles.size());
                        Noble card = curState.humanPlayerNobles.get(cardNum);
                        curState.addToMessage("You lost "+ card.getNobleName() + ". ");
                        curState.humanPlayerNobles.remove(cardNum);

                    }
                    else if(rand>.33 && rand<.66){
                        int cardNum = (int)(rand*curState.computerPlayer2Nobles.size());
                        Noble card = curState.computerPlayer2Nobles.get(cardNum);
                        curState.addToMessage("Player 3 lost "+ card.getNobleName() + ". ");
                        curState.computerPlayer2Nobles.remove(cardNum);
                    }
                    else{
                        int cardNum = (int)(rand*curState.computerPlayer3Nobles.size());
                        Noble card = curState.computerPlayer3Nobles.get(cardNum);
                        curState.addToMessage("Player 4 lost "+ card.getNobleName() + ". ");
                        curState.computerPlayer3Nobles.remove(cardNum);
                    }
                } else if (curState.getCurrentPlayer() == 2) {
                    if (rand < .33){
                        int cardNum = (int)(rand*curState.humanPlayerNobles.size());
                        Noble card = curState.humanPlayerNobles.get(cardNum);
                        curState.addToMessage("You lost "+ card.getNobleName() + ". ");
                        curState.humanPlayerNobles.remove(cardNum);

                    }
                    else if(rand>.33 && rand<.66){
                        int cardNum = (int)(rand*curState.computerPlayer1Nobles.size());
                        Noble card = curState.computerPlayer1Nobles.get(cardNum);
                        curState.addToMessage("Player 2 lost "+ card.getNobleName() + ". ");
                        curState.computerPlayer1Nobles.remove(cardNum);
                    }
                    else{
                        int cardNum = (int)(rand*curState.computerPlayer3Nobles.size());
                        Noble card = curState.computerPlayer3Nobles.get(cardNum);
                        curState.addToMessage("Player 4 lost "+ card.getNobleName() + ". ");
                        curState.computerPlayer3Nobles.remove(cardNum);
                    }

                } else if(curState.getCurrentPlayer()==3){
                    if (rand < .33){
                        int cardNum = (int) (rand * curState.humanPlayerNobles.size());
                        Noble card = curState.humanPlayerNobles.get(cardNum);
                        curState.addToMessage("You lost "+ card.getNobleName() + ". ");
                        curState.humanPlayerNobles.remove(cardNum);
                    } else if(rand>.33 && rand<.66){
                        int cardNum = (int)(rand*curState.computerPlayer1Nobles.size());
                        Noble card = curState.computerPlayer1Nobles.get(cardNum);
                        curState.addToMessage("Player 2 lost "+ card.getNobleName() + ". ");
                        curState.computerPlayer1Nobles.remove(cardNum);
                    } else {
                        int cardNum = (int) (rand * curState.computerPlayer2Nobles.size());
                        Noble card = curState.computerPlayer2Nobles.get(cardNum);
                        curState.addToMessage("Player 3 lost "+ card.getNobleName() + ". ");
                        curState.computerPlayer2Nobles.remove(cardNum);
                    }

                }
            }
        }
        else if (name.equals("Opinionated Guards")){
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
            curState.drawActionCard(curState.getCurrentPlayer());
            curState.drawActionCard(curState.getCurrentPlayer());
            curState.drawActionCard(curState.getCurrentPlayer());
            curState.setNobleCollectOk(false);
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " collected three action cards and no noble. ");
        }
        else if (name.equals("Public Demand")){
            ArrayList<Noble> deathRow = curState.getDeathRow();
            int randIndex = (int)(Math.random()*(deathRow.size()));
            Noble card = deathRow.get(randIndex);
            deathRow.remove(randIndex);
            deathRow.add(0, card);
            curState.addToMessage(card.getNobleName() + " was moved to the front of the line. ");
            curState.setDeathRow(deathRow);
        }
        else if (name.equals("Pushed")){
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
            Noble card = curState.getDeathRow().get(0);
            curState.deathRow.clear();
            curState.deathRow.add(card);
        }
        else if (name.equals("Stumble")){
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

            Noble firstNob = curState.deathRow.get(0);
            curState.addToMessage(firstNob.getNobleName() + " was moved to the end of the line. ");
            curState.removeFromDeathRow(0);
            curState.deathRow.add(firstNob);

        } else if (name.equals("Church Support")) {
            curState.setHasChurchSupport(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive +1 for each blue noble they collect. ");

        } else if (name.equals("Civic Pride")) {
            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i < deathRow.size(); i++) {
                if (deathRow.get(i).getNobleColor() == "green") {
                    Noble card = deathRow.get(i);
                    deathRow.remove(i);
                    if (rand < 0.5) {
                        deathRow.add(i - 1, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward one place. ");
                    } else {
                        deathRow.add(i - 2, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward two places. ");
                    }
                    curState.setDeathRow(deathRow);
                    break;
                }
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
            ArrayList<Noble> deathRow = curState.getDeathRow();
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
            curState.setDeathRow(deathRow);
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
            ArrayList<Noble> deathRow = curState.getDeathRow();
            int randIndex = (int) (Math.random() * (deathRow.size() - 2));
            Noble card = deathRow.get(randIndex);
            double randSpaces = Math.random();
            deathRow.remove(randIndex);
            if (randSpaces >= .5) {
                deathRow.add((randIndex + 1), card);
                curState.addToMessage(card.getNobleName() + "was moved backward one space. ");
            } else if (randSpaces < .5) {
                deathRow.add((randIndex + 2), card);
                curState.addToMessage(card.getNobleName() + "was moved backward two spaces. ");
            }
            curState.setDeathRow(deathRow);
        } else if (name.equals("Ignoble Noble")) {

            ArrayList<Noble> deathRow = curState.getDeathRow();
            if (curState.deathRow.size() > 4) {
                int randIndex = 4 + (int) (Math.random() * (deathRow.size() - 4));
                Noble card = deathRow.get(randIndex);
                deathRow.remove(randIndex);
                deathRow.add((randIndex - 4), card);
                curState.addToMessage(card.getNobleName() + "was moved forward four spaces. ");
            }
            curState.setDeathRow(deathRow);
        } else if (name.equals("Indifferent Public")) {
            curState.setHasIndifferentPublic(curState.getCurrentPlayer());
            curState.addToMessage("Player " + (curState.getCurrentPlayer() + 1) + " will receive one point for each gray noble instead of its normal value. ");

        } else if (name.equals("Information Exchange")) {
            if (curState.getNumPlayers() == 2) {
                ArrayList<ActionCard> curPlayerHand = curState.computerPlayer1Hand;
                curState.computerPlayer1Hand = curState.humanPlayerHand;
                curState.humanPlayerHand = curPlayerHand;
                curState.addToMessage("Player 2 switched hands with you. ");
            }
            if (curState.getNumPlayers() == 3) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand > .5) {
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curState.humanPlayerHand;
                        curState.humanPlayerHand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with you. ");
                    } else {
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with Player 3. ");
                    }
                } else if (curState.getCurrentPlayer() == 2) {

                    if (rand > .5) {
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curState.humanPlayerHand;
                        curState.humanPlayerHand = curPlayerHand;
                        curState.addToMessage("Player 3 switched hands with you. ");
                    } else {
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with Player 3. ");
                    }

                }
            }
            if (curState.getNumPlayers() == 4) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand < .33) {
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curState.humanPlayerHand;
                        curState.humanPlayerHand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with you. ");
                    } else if (rand > .33 && rand < .66) {
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with Player 3. ");
                    } else {
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer3Hand;
                        curState.computerPlayer3Hand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with Player 4. ");
                    }
                } else if (curState.getCurrentPlayer() == 2) {
                    if (rand < .33) {
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curState.humanPlayerHand;
                        curState.humanPlayerHand = curPlayerHand;
                        curState.addToMessage("Player 3 switched hands with you. ");

                    } else if (rand > .33 && rand < .66) {
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with Player 3. ");
                    } else {
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer3Hand;
                        curState.computerPlayer3Hand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curPlayerHand;
                        curState.addToMessage("Player 3 switched hands with Player 4. ");
                    }

                } else if (curState.getCurrentPlayer() == 3) {
                    if (rand < .33) {
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer3Hand;
                        curState.computerPlayer3Hand = curState.humanPlayerHand;
                        curState.humanPlayerHand = curPlayerHand;
                        curState.addToMessage("Player 4 switched hands with you. ");

                    } else if (rand > .33 && rand < .66) {
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer3Hand;
                        curState.computerPlayer3Hand = curState.computerPlayer1Hand;
                        curState.computerPlayer1Hand = curPlayerHand;
                        curState.addToMessage("Player 2 switched hands with Player 4. ");
                    } else {
                        ArrayList<ActionCard> curPlayerHand = curState.computerPlayer3Hand;
                        curState.computerPlayer3Hand = curState.computerPlayer2Hand;
                        curState.computerPlayer2Hand = curPlayerHand;
                        curState.addToMessage("Player 3 switched hands with Player 4. ");
                    }

                }
            }
        } else if (name.equals("Lack of Faith")) {
            for (int i = 0; i < curState.deathRow.size(); i++) {
                if (curState.deathRow.get(i).getNobleColor() == "blue") {
                    Noble card = curState.deathRow.get(i);
                    curState.deathRow.remove(i);
                    curState.deathRow.add(0, card);
                    curState.addToMessage(card.getNobleName() + " was moved to the front of the line. ");
                    break;
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
            ArrayList<Noble> deathRow = curState.getDeathRow();
            if (deathRow.size() > 2) {
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
        } else if (name.equals("Majesty")) {
            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i < deathRow.size(); i++) {
                if (deathRow.get(i).getNobleColor() == "purple") {
                    Noble card = deathRow.get(i);
                    deathRow.remove(i);
                    if (rand < 0.5) {
                        deathRow.add(i - 1, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward one place. ");
                    } else {
                        deathRow.add(i - 2, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward two places. ");
                    }
                    curState.setDeathRow(deathRow);
                    break;
                }
            }
        } else if (name.equals("Mass Confusion")) {
            ArrayList<Noble> deathRow = curState.getDeathRow();
            int numNobles = deathRow.size();
            ArrayList<Noble> currentNobleDeck = curState.getNobleDeck();

            for (int i = 0; i < deathRow.size(); i++) {
                Noble card = deathRow.get(0);
                deathRow.remove(0);
                currentNobleDeck.add(card);
            }
            curState.setNobleDeck(currentNobleDeck);
            curState.shuffleNobleDeck();
            curState.createDeathRow(numNobles);
            curState.addToMessage("All nobles in line were shuffled into the deck and Death Row was recreated. ");
        } else if (name.equals("Military Might")) {
            ArrayList<Noble> deathRow = curState.getDeathRow();
            for (int i = 2; i < deathRow.size(); i++) {
                if (deathRow.get(i).getNobleColor() == "red") {
                    Noble card = deathRow.get(i);
                    deathRow.remove(i);
                    if (rand < 0.5) {
                        deathRow.add(i - 1, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward one place. ");
                    } else {
                        deathRow.add(i - 2, card);
                        curState.addToMessage(card.getNobleName() + " was moved forward two places. ");
                    }
                    curState.setDeathRow(deathRow);
                    break;
                }
            }
        } else if (name.equals("Military Support")) {
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
            if (curState.getNumPlayers() == 2) {
                Noble card = curState.getHumanPlayerNobles().get(curState.getHumanPlayerNobles().size() - 1);
                curState.humanPlayerNobles.remove(curState.getHumanPlayerNobles().size() - 1);
                curState.deathRow.add(card);
                curState.addToMessage(card.getNobleName() + " was moved from your hand to death row. ");

            }
            if (curState.getNumPlayers() == 3) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand > .5) {
                        Noble card = curState.getHumanPlayerNobles().get(curState.getHumanPlayerNobles().size() - 1);
                        curState.humanPlayerNobles.remove(curState.getHumanPlayerNobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from your hand to death row. ");
                    } else {
                        Noble card = curState.getComputerPlayer2Nobles().get(curState.getComputerPlayer2Nobles().size() - 1);
                        curState.computerPlayer2Nobles.remove(curState.getComputerPlayer2Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 3's hand to death row. ");
                    }
                } else if (curState.getCurrentPlayer() == 2) {

                    if (rand > .5) {
                        Noble card = curState.getHumanPlayerNobles().get(curState.getHumanPlayerNobles().size() - 1);
                        curState.humanPlayerNobles.remove(curState.getHumanPlayerNobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from your hand to death row. ");
                    } else {
                        Noble card = curState.getComputerPlayer1Nobles().get(curState.getComputerPlayer1Nobles().size() - 1);
                        curState.computerPlayer1Nobles.remove(curState.getComputerPlayer1Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 2's hand to death row. ");
                    }

                }
            }
            if (curState.getNumPlayers() == 4) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand < .33) {
                        Noble card = curState.getHumanPlayerNobles().get(curState.getHumanPlayerNobles().size() - 1);
                        curState.humanPlayerNobles.remove(curState.getHumanPlayerNobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from your hand to death row. ");

                    } else if (rand > .33 && rand < .66) {
                        Noble card = curState.getComputerPlayer2Nobles().get(curState.getComputerPlayer2Nobles().size() - 1);
                        curState.computerPlayer2Nobles.remove(curState.getComputerPlayer2Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 3's hand to death row. ");
                    } else {
                        Noble card = curState.getComputerPlayer3Nobles().get(curState.getComputerPlayer3Nobles().size() - 1);
                        curState.computerPlayer3Nobles.remove(curState.getComputerPlayer3Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 4's hand to death row. ");
                    }
                } else if (curState.getCurrentPlayer() == 2) {
                    if (rand < .33) {
                        Noble card = curState.getHumanPlayerNobles().get(curState.getHumanPlayerNobles().size() - 1);
                        curState.humanPlayerNobles.remove(curState.getHumanPlayerNobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from your hand to death row. ");

                    } else if (rand > .33 && rand < .66) {
                        Noble card = curState.getComputerPlayer1Nobles().get(curState.getComputerPlayer1Nobles().size() - 1);
                        curState.computerPlayer1Nobles.remove(curState.getComputerPlayer1Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 2's hand to death row. ");
                    } else {
                        Noble card = curState.getComputerPlayer3Nobles().get(curState.getComputerPlayer3Nobles().size() - 1);
                        curState.computerPlayer3Nobles.remove(curState.getComputerPlayer3Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 4's hand to death row. ");
                    }

                } else if (curState.getCurrentPlayer() == 3) {
                    if (rand < .33) {
                        Noble card = curState.getHumanPlayerNobles().get(curState.getHumanPlayerNobles().size() - 1);
                        curState.humanPlayerNobles.remove(curState.getHumanPlayerNobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from your hand to death row. ");

                    } else if (rand > .33 && rand < .66) {
                        Noble card = curState.getComputerPlayer1Nobles().get(curState.getComputerPlayer1Nobles().size() - 1);
                        curState.computerPlayer1Nobles.remove(curState.getComputerPlayer1Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 2's hand to death row. ");
                    } else {
                        Noble card = curState.getComputerPlayer2Nobles().get(curState.getComputerPlayer2Nobles().size() - 1);
                        curState.computerPlayer2Nobles.remove(curState.getComputerPlayer2Nobles().size() - 1);
                        curState.deathRow.add(card);
                        curState.addToMessage(card.getNobleName() + " was moved from Player 3's hand to death row. ");
                    }

                }
            }
        }
        //FIX THIS!!!! if zero noble cards
        else if (name.equals("Missing Heads")) {
            if (curState.getNumPlayers() == 2) {
                int cardNum = (int) (rand * curState.humanPlayerNobles.size());
                Noble card = curState.humanPlayerNobles.get(cardNum);
                curState.addToMessage("You lost " + card.getNobleName() + ". ");
                curState.humanPlayerNobles.remove(cardNum);

            }
            if (curState.getNumPlayers() == 3) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand > .5) {
                        int cardNum = (int) (rand * curState.humanPlayerNobles.size());
                        Noble card = curState.humanPlayerNobles.get(cardNum);
                        curState.addToMessage("You lost " + card.getNobleName() + ". ");
                        curState.humanPlayerNobles.remove(cardNum);
                    } else {
                        int cardNum = (int) (rand * curState.computerPlayer2Nobles.size());
                        Noble card = curState.computerPlayer2Nobles.get(cardNum);
                        curState.addToMessage("Player 3 lost " + card.getNobleName() + ". ");
                        curState.computerPlayer2Nobles.remove(cardNum);
                    }
                } else if (curState.getCurrentPlayer() == 2) {

                    if (rand > .5) {
                        int cardNum = (int) (rand * curState.humanPlayerNobles.size());
                        if (cardNum > 0) {
                            Noble card = curState.humanPlayerNobles.get(cardNum);
                            curState.addToMessage("You lost " + card.getNobleName() + ". ");
                            curState.humanPlayerNobles.remove(cardNum);
                        }
                    } else {
                        int cardNum = (int) (rand * curState.computerPlayer1Nobles.size());
                        if (cardNum > 0) {
                            Noble card = curState.computerPlayer1Nobles.get(cardNum);
                            curState.addToMessage("Player 2 lost " + card.getNobleName() + ". ");
                            curState.computerPlayer1Nobles.remove(cardNum);
                        }
                    }

                }
            }
            if (curState.getNumPlayers() == 4) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand < .33) {
                        int cardNum = (int) (rand * curState.humanPlayerNobles.size());
                        Noble card = curState.humanPlayerNobles.get(cardNum);
                        curState.addToMessage("You lost " + card.getNobleName() + ". ");
                        curState.humanPlayerNobles.remove(cardNum);

                    } else if (rand > .33 && rand < .66) {
                        int cardNum = (int) (rand * curState.computerPlayer2Nobles.size());
                        Noble card = curState.computerPlayer2Nobles.get(cardNum);
                        curState.addToMessage("Player 3 lost " + card.getNobleName() + ". ");
                        curState.computerPlayer2Nobles.remove(cardNum);
                    } else {
                        int cardNum = (int) (rand * curState.computerPlayer3Nobles.size());
                        Noble card = curState.computerPlayer3Nobles.get(cardNum);
                        curState.addToMessage("Player 4 lost " + card.getNobleName() + ". ");
                        curState.computerPlayer3Nobles.remove(cardNum);
                    }
                } else if (curState.getCurrentPlayer() == 2) {
                    if (rand < .33) {
                        int cardNum = (int) (rand * curState.humanPlayerNobles.size());
                        Noble card = curState.humanPlayerNobles.get(cardNum);
                        curState.addToMessage("You lost " + card.getNobleName() + ". ");
                        curState.humanPlayerNobles.remove(cardNum);

                    } else if (rand > .33 && rand < .66) {
                        int cardNum = (int) (rand * curState.computerPlayer1Nobles.size());
                        Noble card = curState.computerPlayer1Nobles.get(cardNum);
                        curState.addToMessage("Player 2 lost " + card.getNobleName() + ". ");
                        curState.computerPlayer1Nobles.remove(cardNum);
                    } else {
                        int cardNum = (int) (rand * curState.computerPlayer3Nobles.size());
                        Noble card = curState.computerPlayer3Nobles.get(cardNum);
                        curState.addToMessage("Player 4 lost " + card.getNobleName() + ". ");
                        curState.computerPlayer3Nobles.remove(cardNum);
                    }

                } else if (curState.getCurrentPlayer() == 3) {
                    if (rand < .33) {
                        int cardNum = (int) (rand * curState.humanPlayerNobles.size());
                        Noble card = curState.humanPlayerNobles.get(cardNum);
                        curState.addToMessage("You lost " + card.getNobleName() + ". ");
                        curState.humanPlayerNobles.remove(cardNum);
                    } else if (rand > .33 && rand < .66) {
                        int cardNum = (int) (rand * curState.computerPlayer1Nobles.size());
                        Noble card = curState.computerPlayer1Nobles.get(cardNum);
                        curState.addToMessage("Player 2 lost " + card.getNobleName() + ". ");
                        curState.computerPlayer1Nobles.remove(cardNum);
                    } else {
                        int cardNum = (int) (rand * curState.computerPlayer2Nobles.size());
                        Noble card = curState.computerPlayer2Nobles.get(cardNum);
                        curState.addToMessage("Player 3 lost " + card.getNobleName() + ". ");
                        curState.computerPlayer2Nobles.remove(cardNum);
                    }

                }
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
            ArrayList<Noble> deathRow = curState.getDeathRow();
            int randIndex = (int) (Math.random() * (deathRow.size()));
            Noble card = deathRow.get(randIndex);
            deathRow.remove(randIndex);
            deathRow.add(0, card);
            curState.addToMessage(card.getNobleName() + " was moved to the front of the line. ");
            curState.setDeathRow(deathRow);
        } else if (name.equals("Pushed")) {
            ArrayList<Noble> deathRow = curState.getDeathRow();
            if (deathRow.size() > 2) {
                int randIndex = 2 + ((int) (Math.random() * (deathRow.size() - 2)));
                Noble card = deathRow.get(randIndex);
                deathRow.remove(randIndex);
                deathRow.add((randIndex - 2), card);
                curState.addToMessage(card.getNobleName() + " was moved forward two spaces. ");
            }
            curState.setDeathRow(deathRow);
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
            ArrayList<Noble> deathRow = curState.getDeathRow();
            if (deathRow.size() > 1) {
                int randIndex = 1 + ((int) (Math.random() * (deathRow.size() - 1)));
                Noble card = deathRow.get(randIndex);
                deathRow.remove(randIndex);
                deathRow.add((randIndex - 1), card);
                curState.addToMessage(card.getNobleName() + " moved forward one spaces. ");
            }
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
            ArrayList<Noble> deathRow = curState.getDeathRow();
            if (deathRow.size() > 3) {
                int randIndex = 3 + ((int) (Math.random() * (deathRow.size() - 3)));
                Noble card = deathRow.get(randIndex);
                deathRow.remove(randIndex);
                deathRow.add((randIndex - 3), card);
                curState.addToMessage(card.getNobleName() + " was moved forward three spaces. ");
            }
            curState.setDeathRow(deathRow);
        } else if (name.equals("Tough Crowd")) {
            if (curState.getNumPlayers() == 2) {
                curState.setHasToughCrowd(0);
                curState.addToMessage("You received -2 points. ");
            }
            if (curState.getNumPlayers() == 3) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand > .5) {
                        curState.setHasToughCrowd(0);
                        curState.addToMessage("You received -2 points. ");
                    } else {
                        curState.setHasToughCrowd(2);
                        curState.addToMessage("Player 3 received -2 points. ");
                    }
                } else if (curState.getCurrentPlayer() == 2) {

                    if (rand > .5) {
                        curState.setHasToughCrowd(0);
                        curState.addToMessage("You received -2 points. ");
                    } else {
                        curState.setHasToughCrowd(1);
                        curState.addToMessage("Player 2 received -2 points. ");
                    }

                }
            }
            if (curState.getNumPlayers() == 4) {
                if (curState.getCurrentPlayer() == 1) {
                    if (rand < .33) {
                        curState.setHasToughCrowd(0);
                        curState.addToMessage("You received -2 points. ");
                    } else if (rand > .33 && rand < .66) {
                        curState.setHasToughCrowd(2);
                        curState.addToMessage("Player 3 received -2 points. ");
                    } else {
                        curState.setHasToughCrowd(3);
                        curState.addToMessage("Player 4 received -2 points. ");
                    }
                } else if (curState.getCurrentPlayer() == 2) {
                    if (rand < .33) {
                        curState.setHasToughCrowd(0);
                        curState.addToMessage("You received -2 points. ");
                    } else if (rand > .33 && rand < .66) {
                        curState.setHasToughCrowd(1);
                        curState.addToMessage("Player 2 received -2 points. ");
                    } else {
                        curState.setHasToughCrowd(3);
                        curState.addToMessage("Player 4 received -2 points. ");
                    }

                } else if (curState.getCurrentPlayer() == 3) {
                    if (rand < .33) {
                        curState.setHasToughCrowd(0);
                        curState.addToMessage("You received -2 points. ");
                    } else if (rand > .33 && rand < .66) {
                        curState.setHasToughCrowd(1);
                        curState.addToMessage("Player 2 received -2 points. ");
                    } else {
                        curState.setHasToughCrowd(2);
                        curState.addToMessage("Player 3 received -2 points. ");
                    }

                }
            }
        } else if (name.equals("Trip")) {
            ArrayList<Noble> deathRow = curState.getDeathRow();
            int randIndex = (int) (Math.random() * (deathRow.size() - 1));
            Noble card = deathRow.get(randIndex);
            deathRow.remove(randIndex);
            deathRow.add((randIndex + 1), card);
            curState.setPlaySecondAction(true);
            curState.addToMessage(card.getNobleName() + " was moved backward one space. ");
            curState.setDeathRow(deathRow);
        } else if (name.equals("Was That My Name?")) {
            ArrayList<Noble> deathRow = curState.getDeathRow();
            if (deathRow.size() > 3) {
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
}
