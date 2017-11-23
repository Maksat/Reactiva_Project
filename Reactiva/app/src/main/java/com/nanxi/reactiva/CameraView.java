package com.nanxi.reactiva;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by maksat on 11/11/17.
 */

public class CameraView extends SurfaceView {

    private boolean isVisible;

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(isVisible) {
            super.onLayout(changed, left, top, right, bottom);
        }else
        {
            android.view.ViewGroup.LayoutParams lp = getLayoutParams();
            super.onLayout(changed, left, top, left+1, top+1);
            lp.width = 1;
            lp.height = 1;
            setLayoutParams(lp);
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
