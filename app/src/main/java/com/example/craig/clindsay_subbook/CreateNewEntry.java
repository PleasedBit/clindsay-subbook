package com.example.craig.clindsay_subbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.util.Iterator;

import static java.lang.Boolean.FALSE;

public class CreateNewEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_entry);

        if (getIntent().getSerializableExtra("SUB_TO_EDIT") != null) {
            Log.d("DATA_GOT_PASSED", getIntent().getSerializableExtra("SUB_TO_EDIT").toString());

            findViewById(R.id.button_delete).setVisibility(View.VISIBLE);
            TextView title = (TextView) findViewById(R.id.textView_title);
            title.setText("Edit Subscription");

            Subscription sub = (Subscription) getIntent().getSerializableExtra("SUB_TO_EDIT");
            EditText Name = findViewById(R.id.editTextName);
            EditText Date = findViewById(R.id.editDate);
            EditText Cost = findViewById(R.id.editTextCost);
            EditText Desc = findViewById(R.id.editTextDesc);

            Name.setText(sub.getName(), TextView.BufferType.EDITABLE);
            Date.setText(sub.getDate(), TextView.BufferType.EDITABLE);
            Cost.setText(sub.getCost(), TextView.BufferType.EDITABLE);
            Desc.setText(sub.getDesc(), TextView.BufferType.EDITABLE);
        }
    }

    public void onClickSubmit(View view) {
        // On clicking submit we need to first verify all the values
        EditText Name = findViewById(R.id.editTextName);
        EditText Date = findViewById(R.id.editDate);
        EditText Cost = findViewById(R.id.editTextCost);
        EditText Desc = findViewById(R.id.editTextDesc);

        //No validation for name

        // START DATE VALIDATION
        //Date must be in the format "yyyy-mm-dd"
        String date_text = Date.getText().toString();
        //if (date_text.length() != 10) {
        //    /*FAILURE!*/
        //   Log.e("DATE_VERIFICATION", "Wrong character count");
        //    return;
        //}

        String[] date_parts = date_text.split("-");

        if (date_parts.length != 3) {
            /*FAILURE!*/
            Log.e("DATE_VERIFICATION", "Wrong date part count");
            return;
        }

        int[] date_ints = new int[3];

        for (int i = 0; i < date_parts.length; i++) {
            date_ints[i] = Integer.parseInt(date_parts[i]);
            Log.d("DATE_VERIFICATION", date_parts[i]);
        }

        // date_ints[0] should be a year (4 digits). I am assuming your subscription
        // did not start in the year 999 or will start in the year 10000 And so will reject those.
        if (date_ints[0] >= 9999 || date_ints[0] < 1000) {
           /*FAILURE!*/
            Log.e("DATE_VERIFICATION", "Invalid year");
            return;
        }

        // date_ints[1] is the month, is should be between 1 and 12 (inclusive, obviously)
        if (date_ints[1] > 12 || date_ints[1] < 1) {
            /*FAILURE!*/
            Log.e("DATE_VERIFICATION", "Invalid month");
            return;
        }

        //date_ints[2] is the day, must be valid for the month and if it is a leap year
        switch(date_ints[1]) {
            case 1: // 31 days
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if (date_ints[2] > 31 || date_ints[2] < 1) {
                    /*FAILURE!*/
                    Log.e("DATE_VERIFICATION", "Invalid day, 31 day case");
                    return;
                }
                break;
            case 4: // 30 days
            case 6:
            case 9:
            case 11:
                if (date_ints[2] > 30 || date_ints[2] < 1) {
                    /*FAILURE!*/
                    Log.e("DATE_VERIFICATION", "Invalid day, 30 day case");
                    return;
                }
                break;
            case 2: //february...
                //if leap year (29 days)
                if ((date_ints[0]%4 == 0 && date_ints[0]%100 != 0) ||  date_ints[0]%400 == 0) {
                    Log.d("DATE_VERIFICATION", "Its a leap year");
                    if (date_ints[2] > 29 || date_ints[2] < 1) {
                        /*FAILURE!*/
                        Log.e("DATE_VERIFICATION", "Invalid day, 29 day case");
                        return;
                    }
                } else { // 28 days
                    if (date_ints[2] > 28 || date_ints[2] < 1) {
                        /*FAILURE!*/
                        Log.e("DATE_VERIFICATION", "Invalid day, 28 day case");
                        return;
                    }
                }
                break;
        }

        //if we got here our date is valid.
        // END DATE VALIDATION

        // START COST VALIDATION
        // the cost is already restricted to a number by the text box settings that we have, I just
        // need to check that the decimal is 2 digits if it is there

        // away...
        if (Cost.getText().toString().matches("^\\d+(\\.\\d\\d)?$") == FALSE) {
            /*FAILURE!*/
            Log.e("COST_VERIFICATION", "Invalid format");
            return;
        }

        // END COST VALIDATION

        // no description validation.
        // if we get here we have passed all the tests

        // save

        Subscription sub = new Subscription(Name.getText().toString(),
                                            date_text,
                                            Cost.getText().toString(),
                                            Desc.getText().toString());

        Intent mainActivityData = new Intent();
        mainActivityData.putExtra("SUB_TO_ADD", sub);
        setResult(RESULT_OK, mainActivityData);
        finish();
    }

    public void onClickCancel(View view){
        if (getIntent().getSerializableExtra("SUB_TO_EDIT") != null) {
            Intent mainActivityData = new Intent();
            mainActivityData.putExtra("SUB_TO_ADD", getIntent().getSerializableExtra("SUB_TO_EDIT"));
            setResult(RESULT_OK, mainActivityData);
            finish();
            return;
        }

        Intent mainActivityData = new Intent();
        setResult(RESULT_CANCELED, mainActivityData);
        finish();
    }

    public void onClickDelete(View view){
        // the item has alreaady been deleted, and we don't want to restore it
        Intent mainActivityData = new Intent();
        setResult(RESULT_CANCELED, mainActivityData);
        finish();
    }

}
