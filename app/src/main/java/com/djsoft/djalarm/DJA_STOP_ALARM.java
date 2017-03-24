package com.djsoft.djalarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Math;
import java.util.ArrayList;

public class DJA_STOP_ALARM extends AppCompatActivity {
    private int firstVal;
    private int secondVal;
    private TextView problemText;
    private TextView errMsg;
    private EditText entry;
    private SharedPreferences settings;
    public static boolean problemSolved;
    private int operation = 0; //0-addition, 1-subtraction, 2-multiplication, 3-division

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);
        if (!DJA_STOP_ALARM.problemSolved)
        {
            finish();
            return;
        }
        problemSolved = false;

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //define major UI attributes
        problemText = (TextView) findViewById(R.id.mathProblem);
        errMsg = (TextView) findViewById(R.id.errorMsg);
        entry = (EditText) findViewById(R.id.answerEntry);

        //setup the UI
        errMsg.setText("Enter the answer to stop the alarm");
        errMsg.setTextSize(30);
        setProblem();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            problemSolved = true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void makeSecondLessThanFirst()
    {
        while (firstVal < secondVal)
        {
            firstVal = (int)(Math.random() * 101);
        }
    }


    private void setProblem()
    {
        int mode = 0;
        try {
            mode = Integer.parseInt(settings.getString("list_problem_difficulty",null));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        switch (mode)
        {
            case 1:
            {
                firstVal = (int)(Math.random() * 101);
                secondVal = (int)(Math.random() * 101);
                operation = (int)(Math.random() * 2);
                if (operation == 1)
                { //if it's subtraction, make sure the second number is less than the first one
                    if (firstVal < secondVal)
                    {
                        if (secondVal == 100 && firstVal == 0)
                        {
                            while (secondVal == 100)
                            {
                                secondVal = (int)(Math.random() * 101);
                            }
                            makeSecondLessThanFirst();
                        } else if (firstVal == 0)
                        {
                            makeSecondLessThanFirst();
                        } else if (secondVal == 100)
                        {
                            while (secondVal == 100)
                            {
                                secondVal = (int)(Math.random() * 101);
                            }
                            makeSecondLessThanFirst();
                        } else {
                            makeSecondLessThanFirst();
                        }
                    }
                }
                break;
            }
            case 2:
            {
                operation = (int)(Math.random() * 2) + 2;

                System.out.println("Operation: " + operation);

                if (operation == 2)
                { //multiplication
                    firstVal = (int)(Math.random() * 13);
                    secondVal = (int)(Math.random() * 13);
                } else { //must be 3 - if it's anything else, we done goofed
                    firstVal = (int)(Math.random() * 41) + 10;

                    System.out.println("Initial firstVal: " + firstVal);

                    boolean prime = true;
                    int count = 2;
                    while (prime && count < 10)
                    { //check if the number of the firstVal is prime
                        if (firstVal % count == 0)
                            prime = false;
                        count++;
                    }

                    System.out.println("Prime: " + prime);

                    while (prime)
                    { //if it is prime, lets like not have it do that
                        firstVal = (int)(Math.random() * 41) + 10;
                        for (int i = 2; i < 10; i++)
                        {
                            if (firstVal % i == 0)
                                prime = false;
                        }
                    }

                    System.out.println("final firstVal: " + firstVal);

                    //now that we know for sure the first number is not a prime number, let's try to find a secondVal that can go into the firstVal nice and easy
                    //first we need to fill an arraylist of possible values
                    ArrayList<Integer> posVals = new ArrayList<>();
                    for (int i = 2; i < 10; i++)
                    {
                        if (firstVal % i == 0)
                            posVals.add(i);
                    }

                    //now we need to generate a random value from the arraylist and have that be our value
                    int valFromArr = (int)(Math.random() * posVals.size());
                    secondVal = posVals.get(valFromArr);

                }
                System.out.println("After all of this: " + firstVal + " , " + secondVal);
                break;
            }
            default:
            {
                firstVal = (int)(Math.random() * 11);
                secondVal = (int)(Math.random() * 11);
                operation = (int)(Math.random() * 2);
                if (operation == 1)
                { //if it's subtraction, make sure the second number is less than the first one
                    if (firstVal < secondVal)
                    {
                        if (secondVal == 10 && firstVal == 0)
                        {
                            while (secondVal == 10)
                            {
                                secondVal = (int)(Math.random() * 11);
                            }
                            while (firstVal < secondVal)
                            {
                                firstVal = (int)(Math.random() * 11);
                            }
                        } else if (firstVal == 0)
                        {
                            while (firstVal < secondVal)
                            {
                                firstVal = (int)(Math.random() * 11);
                            }
                        } else if (secondVal == 10)
                        {
                            while (secondVal == 10)
                            {
                                secondVal = (int)(Math.random() * 11);
                            }
                            while (firstVal < secondVal)
                            {
                                firstVal = (int)(Math.random() * 11);
                            }
                        } else {
                            while (firstVal < secondVal)
                            {
                                firstVal = (int)(Math.random() * 11);
                            }
                        }
                    }
                }
            }
        }
        String operStr = " + ";
        switch (operation)
        {
            case 1:
            {
                operStr = " - ";
                break;
            }
            case 2:
            {
                operStr = " * ";
                break;
            }
            case 3:
            {
                operStr = " / ";
                break;
            }
        }
        problemText.setText(firstVal + operStr + secondVal);
    }

    public void okButtonClick(View view)
    {
        boolean correct = false;
        try
        {
            switch (operation)
            {
                case 1:
                { //subtraction
                    if (Integer.parseInt(String.valueOf(entry.getText())) == (firstVal - secondVal))
                        correct = true;
                    break;
                }
                case 2:
                { //multiplication
                    if (Integer.parseInt(String.valueOf(entry.getText())) == (firstVal * secondVal))
                        correct = true;
                    break;
                }
                case 3:
                { //division
                    if (Integer.parseInt(String.valueOf(entry.getText())) == (firstVal / secondVal))
                        correct = true;
                    break;
                }
                default:
                { //addition
                    if (Integer.parseInt(String.valueOf(entry.getText())) == (firstVal + secondVal))
                        correct = true;
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        if (correct)
        {
            System.out.println("Correct!");

            //bring up the main activity
            startActivity(new Intent(this, DJA_MAIN.class));
            DJA_MAIN.alOnOffButton.setChecked(false);
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final SharedPreferences.Editor settingsEditor = settings.edit();
            settingsEditor.remove("ALARM_HOUR");
            settingsEditor.remove("ALARM_MINUTE");
            settingsEditor.remove("ALARM_DAY");
            settingsEditor.remove("ALARM_MONTH");
            settingsEditor.remove("ALARM_YEAR");
            settingsEditor.apply();
            problemSolved = true;
            finish();
        } else
            Toast.makeText(getApplicationContext(),"Incorrect Answer!",Toast.LENGTH_SHORT).show();
    }
}