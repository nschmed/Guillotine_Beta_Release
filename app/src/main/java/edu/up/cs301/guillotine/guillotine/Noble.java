package edu.up.cs301.guillotine.guillotine;

import java.io.Serializable;

/**
 * This prototype defines a noble and their actions if they have actions.
 *
 * @author Nathan Schmedake
 * @author Muhammed Acar
 * @author Melanie Martinell
 * @author Linnea Bair
 * @version November 2015
 */
public class Noble implements Serializable{

    private static final long serialVersionUID = 7777L;
    protected String name;
    protected String color;
    protected int points;
    int image;

    /*
     * Constructor
     * @param name
     * @param color
     * @param points
     * @param pic
     */
    public Noble(String name, String color, int points, int pic) {
        this.name = name;
        this.color = color;
        this.points = points;
        this.image = pic;
    }

    //name getter
    public String getNobleName() {
        return name;
    }

    //color getter
    public String getNobleColor() {
        return color;
    }

    //points getter
    public int getNoblePoints() {
        return points;
    }

    //points setter
    public void setNoblePoints(Integer points) {
        this.points = points;
    }

    //image getter
    public int getImage(){ return image;}

    /*
     * nobleAction takes the action of the noble, then displays what happened in the prompt if that is not
     * done in any other location
     *
     * @param curState
     *      The current state of the game
     */
    public int nobleAction(GuillotineState curState){
        int index = curState.getCurrentPlayer(); //get the current player to return/modify
        if(this.name.equals("The Clown")){
            //When you collect this noble, place it in another player's score pile
            //This is implemented randomly
            double rand = Math.random();
            if (curState.getNumPlayers() == 2) {
                if (curState.getCurrentPlayer() == 1) {
                    index = 0;
                }
                else if (curState.getCurrentPlayer() == 0){
                    index = 1;
                }

            }
            if (curState.getNumPlayers() == 3) {
                if (curState.getCurrentPlayer() == 0) {
                    if (rand > .5){
                        index = 1;
                    }
                    else{
                        index = 2;
                    }
                }
                else if (curState.getCurrentPlayer() == 1) {
                    if (rand > .5){
                        index = 0;
                    }
                    else{
                        index = 2;
                    }
                }
                else if (curState.getCurrentPlayer() == 2) {

                    if(rand>.5){
                        index = 0;
                    }
                    else{
                        index = 1;
                    }

                }
            }
            if (curState.getNumPlayers() == 4) {
                if (curState.getCurrentPlayer() == 0) {
                    if (rand < .33){
                        index = 1;

                    }
                    else if(rand>.33 && rand<.66){
                        index = 2;
                    }
                    else{
                        index = 3;
                    }
                }
                else if (curState.getCurrentPlayer() == 1) {
                    if (rand < .33){
                        index = 0;

                    }
                    else if(rand>.33 && rand<.66){
                        index = 2;
                    }
                    else{
                        index = 3;
                    }
                } else if (curState.getCurrentPlayer() == 2) {
                    if (rand < .33){
                        index = 0;
                    }
                    else if(rand>.33 && rand<.66){
                        index = 1;
                    }
                    else{
                        index = 3;
                    }

                } else if(curState.getCurrentPlayer()==3){
                    if (rand < .33){
                        index = 0;

                    }
                    else if(rand>.33 && rand<.66){
                        index = 1;
                    }
                    else{
                        index = 2;
                    }

                }
            }
        }
        else if(this.name.equals("Rival Executioner")){
            //Collect the top noble of the noble deck after you collect this noble.
            //This implementation taked the top noble off the deck and places it in the respective
            //player's score pile
            Noble card = curState.nobleDeck.get(0);
            curState.nobleDeck.remove(0);
            if (curState.getCurrentPlayer() == 0) {
                curState.humanPlayerNobles.add(card);
                curState.addToMessage("You received " + card.getNobleName() + ". ");
            }
            else if (curState.getCurrentPlayer() == 1) {
                curState.computerPlayer1Nobles.add(card);
                curState.addToMessage("Player 2 received " + card.getNobleName() + ". ");

            } else if (curState.getCurrentPlayer() == 2) {
                curState.computerPlayer2Nobles.add(card);
                curState.addToMessage("Player 3 received " + card.getNobleName() + ". ");

            } else if(curState.getCurrentPlayer() == 3){
                curState.computerPlayer3Nobles.add(card);
                curState.addToMessage("Player 4 received " + card.getNobleName() + ". ");
            }
        }
        else if(this.name.equals("Lady")){
            //Draw an additional action card at the end of your turn after you collect this noble.
            curState.drawActionCard(index);
        }
        else if(this.name.equals("Robespierre")){
            //The day ends after you collect this noble.  Discard any nobles remaining in line.
            //This sets death row to just be the first noble in it, so when it is collected, the day
            //must be over.
            Noble card = curState.getDeathRow().get(0);
            curState.deathRow.clear();
            curState.deathRow.add(card);
        }
        else if(this.name.equals("Lady in Waiting")){
            //Draw an additional action card at the end of your turn after you collect this noble.
            curState.drawActionCard(index);
        } else if(this.name.equals("Fast Noble")){
            //Collect an additional noble from the front of the line after you collect this noble.
            //The order of the noble collection will be reversed, but both will be collected by
            //the correct player.
            curState.collectNoble(index);
        }
        else if(this.name.equals("Lord")){
            //Draw an additional action card at the end of your turn after you collect this noble.
            curState.drawActionCard(index);
        }
        else if(this.name.equals("General")){
            //Add another noble from the noble deck to the end of the line after you collect this noble.
            Noble card = curState.nobleDeck.get(0);
            curState.deathRow.add(card);
            curState.nobleDeck.remove(0);
            curState.addToMessage(card.getNobleName() + " was added to the end of the line. ");
        }
        else if(this.name.equals("Captain of the Guard")){
            //Add another noble from the noble deck to the end of the line after you collect this noble.
            Noble card = curState.nobleDeck.get(0);
            curState.deathRow.add(card);
            curState.nobleDeck.remove(0);
            curState.addToMessage(card.getNobleName() + " was added to the end of the line. ");
        }
        return index;
    }
}
