package com.example.daniel.pawnpopper;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Daniel on 2016-06-26.
 */
public class myDragListener implements View.OnDragListener {
    @Override
    public boolean onDrag(View oldPos, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:

                // Dropped, reassign View to ViewGroup
                View newPiece = (View) event.getLocalState();
                ViewGroup newPos = (ViewGroup) newPiece.getParent();

                if (MainActivity.board.checkMove((String)newPos.getTag(), (String)oldPos.getTag())){
                    View oldPiece = ((ViewGroup) oldPos).getChildAt(0);

                    newPos.removeView(newPiece);
                    ((ViewGroup) oldPos).removeView(oldPiece);

                    newPos.addView(oldPiece);
                    ((ViewGroup) oldPos).addView(newPiece);

                    MainActivity.board.resolveClusters(true);


                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                ((View) event.getLocalState()).setVisibility(View.VISIBLE);
            default:
                break;
        }
        return true;
    }
}
