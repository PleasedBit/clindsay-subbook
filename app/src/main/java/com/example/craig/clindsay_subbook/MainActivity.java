package com.example.craig.clindsay_subbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "sub_list.sav";

    private ArrayList<Subscription> subList;
    private ArrayAdapter<Subscription> subAdapter;

    private ListView subListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        subListView = (ListView) findViewById(R.id.subListView);

        subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long longint) {
                // delete item (if you click submit or cancel it will be updated/put back
                Subscription sub = (Subscription) parent.getItemAtPosition(position);
                Log.d("ON ITEM CLICK", parent.getItemAtPosition(position).toString());

                subList.remove(sub);
                saveInFile();

                Intent editNewEntryIntent = new Intent(MainActivity.this, CreateNewEntry.class);
                editNewEntryIntent.putExtra("SUB_TO_EDIT", sub);
                startActivityForResult(editNewEntryIntent, 1);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        loadFromFile();

        subAdapter = new ArrayAdapter<Subscription>(this, R.layout.list_item, subList);
        subListView.setAdapter(subAdapter);
    }

    public void gotoAddEntry(View view) {
        Intent createNewEntryIntent = new Intent(this, CreateNewEntry.class);
        startActivityForResult(createNewEntryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Subscription sub = (Subscription) data.getSerializableExtra("SUB_TO_ADD");
            subList.add(sub);

            subAdapter.notifyDataSetChanged();

            saveInFile();
        }
    }

    private void loadFromFile() {

        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));


            Gson gson = new Gson();
            //taken from stack overflow as in class 2018-01-25
            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType();
            subList = gson.fromJson(in, listType);


        } catch (FileNotFoundException e) {
            subList = new ArrayList<Subscription>();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);

            // Added
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(subList, out);
            out.flush();

        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
