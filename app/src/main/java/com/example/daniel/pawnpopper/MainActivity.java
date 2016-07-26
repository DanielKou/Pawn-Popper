package com.example.daniel.pawnpopper;

import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    final int ROWS = 8;
    final int COLUMNS = 8;
    final String SAVED_STATE = "SAVED_STATE";
    static Board board;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = new Board(ROWS, COLUMNS, this);
        // if there's saved state
        if(savedInstanceState != null){
            board.createBoard(savedInstanceState.getString(SAVED_STATE));
        }
        else{
            board.createBoard();
        }
        board.draw(findViewById(R.id.layout));
    }

    //save board state on pause
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        String state = board.saveState();
        savedInstanceState.putString(SAVED_STATE, state);
        super.onSaveInstanceState(savedInstanceState);
    }
}
