package com.gmail.zawadkaadam.sudokuwithscoreboard;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Game extends AppCompatActivity {

    private static final String TAG = "Sudoku";

    static final String DIFFICULT_KEY = "com.gmail.zawadkaadam.sudokuwithscoreboard.main.level";
    private static final int DIFFICULTY_EASY = 0;
    private static final int DIFFICULTY_MEDIUM = 1;
    private static final int DIFFICULTY_HARD = 2;
    private int selX = 0;
    private int selY = 0;
    private final View keys[] = new View[9];
    private View keypad;
    private int[] usedForTile;
    private LinearLayout[] rows = new LinearLayout[9];
    private ArrayList<TextView> tiles = new ArrayList<>(81);


    private int[] puzzle;
    private final String easyPuzzle =
            "360000000004230800000004200" +
                    "070460003820000014500013020" +
                    "001900000007048300000000045";
    private final String mediumPuzzle =
            "650000070000506000014000005" +
                    "007009000002314700000700800" +
                    "500000630000201000030000097";
    private final String hardPuzzle =
            "009000000080605020501078000" +
                    "000000700706040102004000000" +
                    "000720903090301080000000600";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        int diff = getIntent().getIntExtra(DIFFICULT_KEY,
                DIFFICULTY_EASY);
        puzzle = getPuzzle(diff);
        calculateUsedTiles();
        //puzzleView = new PuzzleView(this);
        setContentView(R.layout.game);
        //usedForTile = getUsedTiles(1, 1);
        findViews();

        setLisiningObjects();
        putValueToTiles();
    }

    private void putValueToTiles() {
        for (int i = 0; i < tiles.size(); i++) {
            if (puzzle[i] != 0)
                tiles.get(i).setText("" + puzzle[i]);
            if (isDarkBackground(i)) {
                tiles.get(i).setBackgroundResource(R.drawable.border_dark);
            }
            ;
        }
    }

    private boolean isDarkBackground(int i) {
        if (i / 9 < 3 || i / 9 > 5) {
            if (i % 9 < 6 && i % 9 > 2) {
                return true;
            }
        } else if (i / 9 < 6 && i / 9 > 2) {
            if (i % 9 < 3 || i % 9 > 5) {
                return true;
            }
        }
        return false;
    }

    private void setVisibleKeys() {
        for (View key : keys) {
            key.setVisibility(View.VISIBLE);
        }

        for (int item : usedForTile) {
            if (item != 0)
                keys[item - 1].setVisibility(View.INVISIBLE);
        }
    }

    private void setLisiningObjects() {
        for (int i = 0; i < keys.length; i++) {
            final int t = i + 1;
            keys[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectedTile(t);
                }
            });
        }
        for (int i = 0; i < tiles.size(); i++) {
            final int finalI = i;
            tiles.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select(tiles.get(finalI));
                }
            });
        }
    }

    private void findViews() {
        //keypad = findViewById(R.id.keypad);
        keys[0] = findViewById(R.id.keypad_1);
        keys[1] = findViewById(R.id.keypad_2);
        keys[2] = findViewById(R.id.keypad_3);
        keys[3] = findViewById(R.id.keypad_4);
        keys[4] = findViewById(R.id.keypad_5);
        keys[5] = findViewById(R.id.keypad_6);
        keys[6] = findViewById(R.id.keypad_7);
        keys[7] = findViewById(R.id.keypad_8);
        keys[8] = findViewById(R.id.keypad_9);
        rows[0] = (LinearLayout) findViewById(R.id.row0);
        rows[1] = (LinearLayout) findViewById(R.id.row1);
        rows[2] = (LinearLayout) findViewById(R.id.row2);
        rows[3] = (LinearLayout) findViewById(R.id.row3);
        rows[4] = (LinearLayout) findViewById(R.id.row4);
        rows[5] = (LinearLayout) findViewById(R.id.row5);
        rows[6] = (LinearLayout) findViewById(R.id.row6);
        rows[7] = (LinearLayout) findViewById(R.id.row7);
        rows[8] = (LinearLayout) findViewById(R.id.row8);

        for (int j = 0; j < 9; j++) {

            for (int i = 0; i < rows[j].getChildCount(); i++) {
                tiles.add((TextView) rows[j].getChildAt(i));
            }
        }


    }


    private int[] getPuzzle(int diff) {
        String puz;

        // TODO: Continue last game
        switch (diff) {
            case DIFFICULTY_HARD:
                puz = hardPuzzle;
                break;
            case DIFFICULTY_MEDIUM:
                puz = mediumPuzzle;
                break;
            case DIFFICULTY_EASY:
            default:
                puz = easyPuzzle;
                break;
        }

        return fromPuzzleString(puz);
    }


    static private String toPuzzleString(int[] puz) {
        StringBuilder buf = new StringBuilder();
        for (int element : puz) {
            buf.append(element);
        }
        return buf.toString();
    }


    static protected int[] fromPuzzleString(String string) {
        int[] puz = new int[string.length()];
        for (int i = 0; i < puz.length; i++) {
            puz[i] = string.charAt(i) - '0';
        }
        return puz;
    }


    private int getTile(int x, int y) {
        return puzzle[y * 9 + x];
    }


    private void setTile(int x, int y, int value) {
        puzzle[y * 9 + x] = value;
        Log.d(TAG, "SET VALUE " + puzzle[y * 9 + x]);
    }


    protected String getTileString(int x, int y) {
        int v = getTile(x, y);
        if (v == 0)
            return "";
        else
            return String.valueOf(v);
    }


    protected boolean setTileIfValid(int x, int y, int value) {
        int[] tiles = getUsedTiles(x, y);
        if (value != 0) {
            for (int tile : tiles) {
                if (tile == value)
                    return false;
            }
        }
        setTile(x, y, value);
        calculateUsedTiles();
        putValueToTiles();
        return true;
    }


    protected void showNoMoreMoves(int x, int y) {
        int[] tiles = getUsedTiles(x, y);
        if (tiles.length == 9) {
            Toast toast = Toast.makeText(this,
                    R.string.label_no_more_moves, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    private final int[][][] used = new int[9][9][];


    protected int[] getUsedTiles(int x, int y) {
        return used[x][y];
    }


    private void calculateUsedTiles() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                used[x][y] = calculateUsedTiles(x, y);
                Log.d(TAG, "used[" + x + "][" + y + "] = "
                        + toPuzzleString(used[x][y]));
            }
        }
    }


    private int[] calculateUsedTiles(int x, int y) {
        int[] c = new int[9];

        for (int i = 0; i < 9; i++) {
            if (i == x)
                continue;
            int t = getTile(i, y);
            if (t != 0)
                c[t - 1] = t;
        }

        for (int i = 0; i < 9; i++) {
            if (i == y)
                continue;
            int t = getTile(x, i);
            if (t != 0)
                c[t - 1] = t;
        }

        int startx = (x / 3) * 3;
        int starty = (y / 3) * 3;
        for (int i = startx; i < startx + 3; i++) {
            for (int j = starty; j < starty + 3; j++) {
                if (i == x && j == y)
                    continue;
                int t = getTile(i, j);
                if (t != 0)
                    c[t - 1] = t;
            }
        }

        int nused = 0;
        for (int t : c) {
            if (t != 0)
                nused++;
        }
        int[] c1 = new int[nused];
        nused = 0;
        for (int t : c) {
            if (t != 0)
                c1[nused++] = t;
        }
        return c1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Music.play(this, R.raw.main);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Music.stop(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown: keycode=" + keyCode + ", event="
                + event);
        switch (keyCode) {
//            case KeyEvent.KEYCODE_DPAD_UP:
//                select(selX, selY - 1);
//                break;
//            case KeyEvent.KEYCODE_DPAD_DOWN:
//                select(selX, selY + 1);
//                break;
//            case KeyEvent.KEYCODE_DPAD_LEFT:
//                select(selX - 1, selY);
//                break;
//            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                select(selX + 1, selY);
//                break;


            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE:
                setSelectedTile(0);
                break;
            case KeyEvent.KEYCODE_1:
                setSelectedTile(1);
                break;
            case KeyEvent.KEYCODE_2:
                setSelectedTile(2);
                break;
            case KeyEvent.KEYCODE_3:
                setSelectedTile(3);
                break;
            case KeyEvent.KEYCODE_4:
                setSelectedTile(4);
                break;
            case KeyEvent.KEYCODE_5:
                setSelectedTile(5);
                break;
            case KeyEvent.KEYCODE_6:
                setSelectedTile(6);
                break;
            case KeyEvent.KEYCODE_7:
                setSelectedTile(7);
                break;
            case KeyEvent.KEYCODE_8:
                setSelectedTile(8);
                break;
            case KeyEvent.KEYCODE_9:
                setSelectedTile(9);
                break;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                this.showNoMoreMoves(selX, selY);
                break;


            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public void setSelectedTile(int tile) {
        if (setTileIfValid(selX, selY, tile)) {

            Log.d(TAG, "setSelectedTile: " + tile);
            //this.getCurrentFocus().startAnimation(AnimationUtils.loadAnimation(this,
            //         R.anim.shake));

        } else {
            Log.d(TAG, "setSelectedTile: invalid: " + tile);

        }
    }


    private void select(TextView textView) {


        int selectedTile = selY * 9 + selX;
        if (isDarkBackground(selectedTile)) {
            tiles.get(selectedTile).setBackgroundResource(R.drawable.border_dark);
        } else {
            tiles.get(selectedTile).setBackgroundResource(R.drawable.border_light);
        }
        for (int i = 0; i < tiles.size(); i++) {
            if (textView.equals(tiles.get(i))) {
                selY = i / 9;
                selX = i % 9;
                tiles.get(i).setBackgroundResource(R.drawable.border_selected);
            }
        }
        calculateUsedTiles();
        usedForTile = getUsedTiles(selX, selY);
        Log.d(TAG, " usedForTile: " + Arrays.toString(usedForTile));
        setVisibleKeys();
    }


}