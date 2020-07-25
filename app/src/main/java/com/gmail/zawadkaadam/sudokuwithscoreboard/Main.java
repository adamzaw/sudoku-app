package com.gmail.zawadkaadam.sudokuwithscoreboard;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



public class Main extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "Sudoku";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        View buttonInfo = findViewById(R.id.information_button);
        buttonInfo.setOnClickListener(this);
        View buttonNewGame = findViewById(R.id.new_game_button);
        buttonNewGame.setOnClickListener(this);
        View buttonExit = findViewById(R.id.exit_button);
        buttonExit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_game_button:
                openDialogNewGame();
                break;
            case R.id.information_button:
                Intent i = new Intent(this, Info.class);
                startActivity(i);
                break;
            case R.id.exit_button:
                finish();
                break;

        }
    }


    private void openDialogNewGame() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.new_game_title)
                .setItems(R.array.level, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        runGame(which);
                    }
                })
                .show();
    }

    private void runGame(int which) {
        Log.d(TAG, "click " + which);
        Intent intent = new Intent(Main.this, Game.class);
        intent.putExtra(Game.DIFFICULT_KEY, which);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, Preferences.class));
                return true;
        }
        return false;
    }
    @Override
    protected void onResume(){
        super.onResume();
        Music.play(this, R.raw.main);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Music.stop(this);
    }
}