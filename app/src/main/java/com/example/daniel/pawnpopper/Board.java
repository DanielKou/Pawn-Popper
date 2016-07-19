package com.example.daniel.pawnpopper;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Daniel on 2016-06-27.
 */
public class Board {
    private int ROWS;
    private int COLUMNS;
    private LinearLayout[][] grid;
    private Piece[][] pieces;
    private Context context;

    public Board(int row, int column, Context c){
        ROWS = row;
        COLUMNS = column;
        context = c;
        grid = new LinearLayout[ROWS][COLUMNS];
        pieces = new Piece[ROWS][COLUMNS];
    }

    public void draw(View parent) {
        for (int i = 0; i < ROWS; i++) {
            LinearLayout row = new LinearLayout(context);
            row.setWeightSum((float) COLUMNS);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            rowParam.weight = 1f;

            row.setLayoutParams(rowParam);

            for (int j = 0; j < ROWS; j++) {
                LinearLayout cell = new LinearLayout(context);
                cell.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                cell.setGravity(Gravity.CENTER_VERTICAL);
                cell.setOnDragListener(new myDragListener());
                cell.setBackgroundColor(context.getResources().getColor(((i + j) % 2 == 0) ? R.color.color1 : R.color.color2));
                cell.setTag(j + "" + i);
                cell.setPadding(6,0,0,0);
                row.addView(cell);
                cell.addView(pieces[i][j].getIcon());
                grid[i][j] = cell;
            }
            ((LinearLayout) parent).addView(row);

        }
    }

    public void createBoard(){
        // create board
        for (int i = 0; i < ROWS; i++){
            for (int j = 0; j < COLUMNS; j++){
                pieces[i][j] = new Piece(context);
            }
        }

        resolveClusters(false);

    }

    public void resolveClusters(boolean pop){
        boolean clusterFound = false;
        // check horizontal clusters
        System.out.println("HORIZONTAL");
        for (int i = 0; i < ROWS; i++){
            int clusterLen = 1;

            // find horizontal cluster
            for (int j = 0; j < COLUMNS; j++){
                boolean checkCluster = false;

                if (j == COLUMNS - 1){
                    checkCluster = true;
                }
                else{
                    if (pieces[i][j].getType() == pieces[i][j+1].getType()){
                        clusterLen++;
                    }
                    else{
                        checkCluster = true;
                    }
                }

                if (checkCluster){
                    if (clusterLen >= 3){
                        clusterFound = true;

                        if (pop){
                            popAndShift(j, i, clusterLen, 'h');
                        }
                        else {
                            int swapPiece;
                            int swapDir;

                            do {
                                swapPiece = j - (int) (Math.random() * 100) % clusterLen;
                                swapDir = (((int) (Math.random() * 100) % 2) == 0) ? -1 : 1;
                                int a = 0;
                            }
                            while ((!inBounds(swapPiece)) || (!inBounds(i + swapDir)) || madeCluster(swapPiece, i, swapPiece, i + swapDir, true));

                            System.out.println("MOVE " + swapPiece + "|" + i + " TO " + swapPiece + "|" + (i + swapDir));
                        }
                    }
                    clusterLen = 1;
                }
            }
        }

        // check vertical clusters
        System.out.println("VERTICAL");
        for (int i = 0; i < ROWS; i++){
            int clusterLen = 1;

            // find horizontal cluster
            for (int j = 0; j < COLUMNS; j++){
                boolean checkCluster = false;

                if (j == COLUMNS - 1){
                    checkCluster = true;
                }
                else{
                    if (pieces[j][i].getType() == pieces[j+1][i].getType()){
                        clusterLen++;
                    }
                    else{
                        checkCluster = true;
                    }
                }

                if (checkCluster){
                    if (clusterLen >= 3){
                        clusterFound = true;

                        if (pop){
                            popAndShift(i, j, clusterLen, 'v');
                        }
                        else {
                            int swapPiece;
                            int swapDir;

                            do {
                                swapPiece = j - (int) (Math.random() * 100) % clusterLen;
                                swapDir = (((int) (Math.random() * 100) % 2) == 0) ? -1 : 1;
                                int a = 0;
                            }
                            while ((!inBounds(swapPiece)) || (!inBounds(i + swapDir)) || madeCluster(i, swapPiece, i + swapDir, swapPiece, true));

                            System.out.println("MOVE " + i + "|" + swapPiece + " TO " + (i + swapDir) + "|" + swapPiece);
                        }
                    }
                    clusterLen = 1;
                }
            }
        }



        // recurse until no clusters
        if (clusterFound){
            resolveClusters(pop);
        }
    }

    public boolean checkMove(String oldPos, String newPos){
        if (oldPos.equals(newPos)) {
            return false;
        }

        int oldX = oldPos.charAt(0) - '0';
        int oldY = oldPos.charAt(1) - '0';
        int newX = newPos.charAt(0) - '0';
        int newY = newPos.charAt(1) - '0';

        return (pieces[oldY][oldX].checkMove(oldX, oldY, newX, newY) && madeCluster(oldX, oldY, newX, newY, false));
    }

    private void popAndShift(int x, int y, int clusterLen, char direction){
        if (direction == 'v'){
            int i;
            for (i = y; i > y - clusterLen; i--){
                grid[i][x].removeView(grid[i][x].getChildAt(0));
                pieces[i][x] = null;
            }
            shift(x, i, clusterLen);
            renewPieces(x);
        }
        else if (direction == 'h'){
            for (int i = x; i > x - clusterLen; i--){
                grid[y][i].removeView(grid[y][i].getChildAt(0));
                pieces[y][i] = null;
                shift(i, y-1, 1);
                renewPieces(i);
            }
        }
    }

    private void shift (int column, int startingRow, int amount){
        while (startingRow >= 0){
            // swap(int oldX, int oldY, int newX, int newY)
            grid[startingRow][column].removeView(grid[startingRow][column].getChildAt(0));
            grid[startingRow + amount][column].addView(pieces[startingRow][column].getIcon());
            swap(column, startingRow, column, startingRow + amount);
            startingRow--;
        }
    }

    private void renewPieces(int column){
        int i = 0;
        while(pieces[i][column] == null){
            pieces[i][column] = new Piece(context);
            grid[i][column].addView(pieces[i][column].getIcon());
            i++;
        }
    }

    private boolean madeCluster(int oldX, int oldY, int newX, int newY, boolean swapBack){
        if (pieces[oldY][oldX].getType() == pieces[newY][newX].getType()){
            return true;
        }

        swap(oldX, oldY, newX, newY);

        boolean a = checkHandV(newX, newY);
        boolean b = checkHandV(oldX, oldY);
        boolean clusters = checkHandV(newX, newY) || checkHandV(oldX, oldY);



        if (clusters) {
            if (swapBack){
                swap(newX, newY, oldX, oldY);
            }
            return true;
        }
        else{
            if (!swapBack) {
                swap(newX, newY, oldX, oldY);
            }
            return false;
        }


    }




    private boolean checkHandV(int x, int y){
        int clusterLen = 1;
        int tempX = x, tempY = y;
        //check horizontal cluster
        while(tempX > 0){
            if (pieces[tempY][tempX].getType() != pieces[tempY][tempX-1].getType()){
                break;
            }
            else{
                clusterLen++;
                tempX--;
            }
        }
        tempX = x;
        while(tempX < 7){
            if (pieces[tempY][tempX].getType() != pieces[tempY][tempX+1].getType()){
                break;
            }
            else{
                clusterLen++;
                tempX++;
            }
        }
        if (clusterLen >= 3)
            return true;

        //reset temps
        clusterLen = 1;
        tempX = x;
        tempY = y;
        //check vertical clusters
        while(tempY > 0){
            if (pieces[tempY][tempX].getType() != pieces[tempY-1][tempX].getType()){
                break;
            }
            else{
                clusterLen++;
                tempY--;
            }
        }
        tempY = y;
        while(tempY < 7){
            if (pieces[tempY][tempX].getType() != pieces[tempY+1][tempX].getType()){
                break;
            }
            else{
                clusterLen++;
                tempY++;
            }
        }
        if (clusterLen >= 3)
            return true;

        return false;
    }

    private boolean inBounds(int pos){
        if (pos < 0 || pos > 7) {
            return false;
        }
        return true;
    }

    private void swap(int oldX, int oldY, int newX, int newY){
        Piece temp = pieces[oldY][oldX];
        pieces[oldY][oldX] = pieces[newY][newX];
        pieces[newY][newX] = temp;

    }

    private void printBoard(){
        for (int i = 0; i < COLUMNS; i++){
            for (int j = 0; j < ROWS; j++){
                System.out.print(pieces[i][j].getType() + " ");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

}
