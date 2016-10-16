package squarerock.celluloid.adapters.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by pranavkonduru on 10/15/16.
 */

public class ItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    GestureDetector gestureDetector;

    public ItemClickListener(Context context, RecyclerView recyclerView, OnItemClickListener listener) {
        itemClickListener = listener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && itemClickListener != null && gestureDetector.onTouchEvent(e)) {
            itemClickListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

}
