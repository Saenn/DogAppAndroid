package main.dogappandroid;

import android.view.MotionEvent;
import android.view.View;

public interface ClickListener {

    void onClick (View view, int position, boolean isLongClick, MotionEvent motionEvent);
}
