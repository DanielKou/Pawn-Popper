package com.example.daniel.pawnpopper;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Daniel on 2016-06-28.
 */
public class Piece {
    public enum Type {king, queen, knight, rook, bishop}

    private Type type;
    private ImageView icon;

    // factory for generating random piece
    public Piece(Context context) {
        icon = new ImageView(context);
        icon.setOnTouchListener(new myTouchListener());

        // bigger number for more randomness lol
        int pieceType = (int) (Math.random()*1000) % 5;
        switch (pieceType) {
            case 0:
                type = Type.king;
                icon.setBackgroundResource(R.drawable.king);
                break;
            case 1:
                type = Type.queen;
                icon.setBackgroundResource(R.drawable.queen);
                break;
            case 2:
                type = Type.knight;
                icon.setBackgroundResource(R.drawable.knight);
                break;
            case 3:
                type = Type.rook;
                icon.setBackgroundResource(R.drawable.rook);
                break;
            case 4:
                type = Type.bishop;
                icon.setBackgroundResource(R.drawable.bishop);
                break;
        }
    }

    // generates a set piece, for when resuming a saved state
    public Piece(Context context, char pieceType){
        icon = new ImageView(context);
        icon.setOnTouchListener(new myTouchListener());

        switch (pieceType - '0') {
            case 0:
                type = Type.king;
                icon.setBackgroundResource(R.drawable.king);
                break;
            case 1:
                type = Type.queen;
                icon.setBackgroundResource(R.drawable.queen);
                break;
            case 2:
                type = Type.knight;
                icon.setBackgroundResource(R.drawable.knight);
                break;
            case 3:
                type = Type.rook;
                icon.setBackgroundResource(R.drawable.rook);
                break;
            case 4:
                type = Type.bishop;
                icon.setBackgroundResource(R.drawable.bishop);
                break;
        }
    }

    // GETTERS ===============================================

    public ImageView getIcon() {
        return icon;
    }

    public Type getType(){
        return type;
    }

    // =========================================================

    // checks move of a piece (same as regular chess piece)
    public boolean checkMove(int oldX, int oldY, int newX, int newY){
        switch (type){
            case king:
                // diagonal move
                if (Math.abs(newX - oldX) == Math.abs(newY - oldY)){
                    return (Math.abs(newX - oldX) == 1);
                }
                else if (newX == oldX || newY == oldY){
                    return (Math.abs(newX - oldX) == 1) || (Math.abs(newY - oldY) == 1);
                }
            case queen:
                return ((newX == oldX || newY == oldY) || (Math.abs(newX - oldX) == Math.abs(newY - oldY)));
            case knight:
                return (((newX == oldX + 2 && newY == oldY + 1) || (newX == oldX - 2 && newY == oldY + 1) || (newX == oldX + 2 && newY == oldY - 1) || (newX == oldX - 2 && newY == oldY - 1) || (newX == oldX + 1 && newY == oldY + 2) || (newX == oldX - 1 && newY == oldY + 2) || (newX == oldX + 1 && newY == oldY - 2) || (newX == oldX - 1 && newY == oldY - 2)));
            case rook:
                return (newX == oldX || newY == oldY);
            case bishop:
                return (Math.abs(newX - oldX) == Math.abs(newY - oldY));
        }
        return false;
    }
}
