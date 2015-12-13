package edu.up.cs301.guillotine.guillotine;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import edu.up.cs301.guillotine.R;

/**
 * This is the start screen for the game.
 *
 * @author Nathan Schmedake
 * @author Muhammed Acar
 * @author Melanie Martinell
 * @author Linnea Bair
 * @version November 2015
 */
public class InitScreen extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener
{
    //edit text
    EditText name;

    //username
    String userName;

    //buttons
    Button start;
    Button rules;

    //radio buttons
    RadioButton oneOpp;
    RadioButton twoOpp;
    RadioButton threeOpp;
    RadioButton beginner;
    RadioButton expert;

    //radio groups
    RadioGroup opponents;
    RadioGroup levels;

    //instance variables
    int numCompPlayers;
    int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initscreen);

        //edit text
        name = (EditText) findViewById(R.id.editName);

        //user name
        userName = name.getText().toString();

        //buttons
        start = (Button) findViewById(R.id.start);
        rules = (Button) findViewById(R.id.rulesButton1);

        //initialize # opponents and level
        difficulty = 0;
        numCompPlayers = 1;

        //radio buttons
        oneOpp = (RadioButton) findViewById(R.id.oneOpp);
        twoOpp = (RadioButton) findViewById(R.id.twoOpp);
        threeOpp = (RadioButton) findViewById(R.id.threeOpp);
        beginner = (RadioButton) findViewById(R.id.beginner);
        expert = (RadioButton) findViewById(R.id.expert);

        //radio groups
        opponents = (RadioGroup) findViewById(R.id.opponents);
        opponents.setOnCheckedChangeListener(this);
        levels = (RadioGroup) findViewById(R.id.levels);
        levels.setOnCheckedChangeListener(this);

        //lock landscape orientation
        lockOrientationLandscape(InitScreen.this);
    }
    public static void lockOrientationLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_initscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //create a local game
    public void onStart(View view){

        userName = name.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        int values[] = {numCompPlayers, difficulty};
        String names[]={userName};
        intent.putExtra("values", values);
        intent.putExtra("name", names);
        startActivity(intent);

    }

    //go to rules screen
    public void onRules(View view)
    {
        startActivity(new Intent(InitScreen.this, RulesScreen.class));
    }

    //onCheckChanged: Listens to which radio button has been selected and sets # opponents and level
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        //# opponents
        if(group.equals(opponents))
        {
            if(checkedId == R.id.oneOpp)//1 opponent
            {
                numCompPlayers = 1;
            }
            else if(checkedId == R.id.twoOpp)//2 opponents
            {
                numCompPlayers = 2;
            }
            else if(checkedId == R.id.threeOpp)//3 opponents
            {
                numCompPlayers = 3;
            }
        }

        //levels
        if(group.equals(levels))
        {
            if(checkedId == R.id.beginner)//beginner level
            {
                difficulty = 0;
            }
            else if (checkedId == R.id.expert)//expert level
            {
                difficulty = 1;
            }
        }
    }
}
