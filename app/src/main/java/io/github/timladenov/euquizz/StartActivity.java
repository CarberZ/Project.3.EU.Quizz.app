package io.github.timladenov.euquizz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class StartActivity extends AppCompatActivity {

    private String playerNames = "";
    private int sleePTime = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        /*@param namesField is used to get user's name and store it @param playerNames
        * The EditText view is hidden for 3,5 seconds on start. It is later added via
        * @param showNamesField thread.
        * */

        final EditText namesField = (EditText) findViewById(R.id.namesField);
        namesField.setVisibility(View.GONE);


        Thread showNamesField = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {

                        /*Checks if @param sleePTime is more than 0. If it starts for the first time, @param sleePTime will be 3500 (ms), if the
                        * screen is rotated, a value of 0 will be stored, so that the loading screen will initiate only once in app's life cycle,
                        * and name entry view will always be visible.
                        * */

                        if (sleePTime > 0) {
                            sleep(sleePTime);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    namesField.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    namesField.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        showNamesField.start();

        /*OnClickListener will store the name as entered in the EditText view, in a String.
        * Then it will send the string to the next Activity for further use.
        * */

        namesField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerNames = namesField.getText().toString();
//                Log.v("playerNames", playerNames);

                Intent proceedToMain = new Intent(getApplicationContext(), MainActivity.class);
                proceedToMain.putExtra("playerNames", playerNames);
                startActivity(proceedToMain);
                finish();
            }
        });
    }

    /* onSaveInstanceState is used to store data before screen rotation */

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("sleePTime", 0);
        savedInstanceState.putString("playerNames", playerNames);
        super.onSaveInstanceState(savedInstanceState);
    }

    /* onRestoreInstanceState restores the data from onSaveInstanceState to the respectful variables*/

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        sleePTime = savedInstanceState.getInt("sleepTime");
        playerNames = savedInstanceState.getString("playerNames");
    }
}