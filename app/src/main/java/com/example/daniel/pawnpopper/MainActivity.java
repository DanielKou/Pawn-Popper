package com.example.daniel.pawnpopper;

import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    final int ROWS = 8;
    final int COLUMNS = 8;

    static int[] a = {1,2,3};
    static Board board;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LinearLayout finalCell = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = new Board(ROWS, COLUMNS, this);
        board.createBoard();
        board.draw(findViewById(R.id.layout));
    }




}
