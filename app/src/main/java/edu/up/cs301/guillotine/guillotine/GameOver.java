package edu.up.cs301.guillotine.guillotine;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import edu.up.cs301.guillotine.R;

/**
 * This is the basic view of the game over screen, and the activity that supports its display.
 *
 * @author Nathan Schmedake
 * @author Muhammed Acar
 * @author Melanie Martinell
 * @author Linnea Bair
 * @version December 2015
 */
public class GameOver extends AppCompatActivity {

    //These are the players' scores at the end of the game.
    private TextView userScore;
    private TextView scorePlayer1;
    private TextView scorePlayer2;
    private TextView scorePlayer3;
    //This is the intent used to pass in the players' scores.
    Intent intent3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        //Initialize text views
        userScore = (TextView)findViewById(R.id.userScoreGameOver);
        scorePlayer1 = (TextView)findViewById(R.id.comp1ScoreGameOver);
        scorePlayer2 = (TextView)findViewById(R.id.comp2ScoreGameOver);
        scorePlayer3 = (TextView)findViewById(R.id.comp3ScoreGameOver);

        //Receive intent and the values of the scores as an array.
        intent3 = getIntent();
        int littleArray[] = intent3.getIntArrayExtra("values");

        //See who wins
        int winner =0;
        for(int i=0;i<littleArray.length-1;i++){
            if(littleArray[i]<littleArray[i+1]){
                winner = i+1;
            }
        }

        //Display who wins with a toast.
        String winnerName = "";
        if(winner ==0){
            winnerName = "You have won!!";
        }
        if(winner ==1){
            winnerName = "Computer player1 has won.";
        }
        if(winner ==2){
            winnerName = "Computer player2 has won.";
        }
        if(winner ==3){
            winnerName = "Computer player3 has won.";
        }

        Toast.makeText(getApplicationContext(),winnerName,Toast.LENGTH_LONG).show();

        //Set the scores and display them.
        String human = littleArray[0] + "";
        String comp1 = littleArray[1]+"";
        String comp2 = littleArray[2]+"";
        String comp3 = littleArray[3]+"";

        userScore.setText(human + "");
        scorePlayer1.setText(comp1+"");
        scorePlayer2.setText(comp2+"");
        scorePlayer3.setText(comp3+"");

        lockOrientationLandscape(GameOver.this);
    }

    /*
     * This allows for the orientation to be locked to landscape
     * @param activity
     *      The current activity
     */
    public static void lockOrientationLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /*
     * This creates the menu. It is basic.
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_over, menu);
        return true;
    }

    /*
     * This allows for the menu to be active
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
