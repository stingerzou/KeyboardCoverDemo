package com.stingerzou.keyboardcoverdemo;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

public class KeyboardStatusDetector {
    private static int SOFT_KEY_BOARD_HEIGHT = -1;
    private KeyboardVisibilityListener mVisibilityListener;


    public KeyboardStatusDetector registerActivity(final Activity a) {
        final View v = a.getWindow().getDecorView();
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                v.getWindowVisibleDisplayFrame(r);

                int heightDiff = v.getHeight() - (r.bottom - r.top) - getNavigationBarHeight(a) - getStatusBarHeight(a);

                if (heightDiff >= getScreenHeight(a) / 3 && SOFT_KEY_BOARD_HEIGHT == -1) {
                    SOFT_KEY_BOARD_HEIGHT = heightDiff;
                }

                if (SOFT_KEY_BOARD_HEIGHT != -1 && heightDiff >= SOFT_KEY_BOARD_HEIGHT) {

                    if (mVisibilityListener != null) {

                        mVisibilityListener.onVisibilityChanged(true, v.findFocus());
                    }
                } else if (SOFT_KEY_BOARD_HEIGHT != -1){

                    if (mVisibilityListener != null) {
                        mVisibilityListener.onVisibilityChanged(false, v.findFocus());
                    }
                }
            }
        });

        return this;
    }

    public KeyboardStatusDetector setVisibilityListener(KeyboardVisibilityListener listener) {
        mVisibilityListener = listener;
        return this;
    }

    public interface KeyboardVisibilityListener {
        void onVisibilityChanged(boolean keyboardVisible, View view);
    }

    public int getStatusBarHeight(Context context) {
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }

    private int getNavigationBarHeight(Context context) {

        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        int height = context.getResources().getDimensionPixelSize(resourceId);
        return height;
    }

    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        //int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        return height;
    }

    public boolean isViewUnderKeyBoard(View view, int offset) {

        int[] location = new int[2] ;
        view.getLocationInWindow(location);

        return location[1] > (getScreenHeight(view.getContext()) - SOFT_KEY_BOARD_HEIGHT - getNavigationBarHeight(view.getContext()) - offset);
    }

}
