package stark.skplayer.demo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import stark.skplayer.demo.R;
import stark.skplayer.demo.ViewUtils;

/**
 * Created by jihongwen on 2016/10/10.
 */

public class FloatVideoLayout extends RelativeLayout {

    RecyclerView videoList;

    boolean isFullScreen = false;

    FrameLayout frameLayout;

    public FloatVideoLayout(Context context) {
        super(context);
    }

    public FloatVideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatVideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FloatVideoLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init() {
        videoList = (RecyclerView) findViewById(R.id.recyclerList);

        frameLayout = new FrameLayout(getContext());

        frameLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("jihongwen", "frameLayout onTouch");
                return false;
            }
        });
        addView(frameLayout);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (videoList != null) {
            videoList.onInterceptTouchEvent(ev);
            Log.d("jihongwen", "videoList  onInterceptTouchEvent");
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
        LayoutParams layoutParams = (LayoutParams) frameLayout.getLayoutParams();
        layoutParams.topMargin = 0;
        layoutParams.leftMargin = 0;
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.MATCH_PARENT;
        frameLayout.setLayoutParams(layoutParams);
    }

    public void addVideoView(View videoView) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        frameLayout.addView(videoView, layoutParams);
    }

    public void updateVideoViewLocation(View targetView) {
        if (targetView == null || isFullScreen) {
            return;
        }
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        Rect rect = new Rect();
        //targetView.getLocalVisibleRect(rect);
        targetView.getGlobalVisibleRect(rect);
        Log.d("jihongwen", "rect.left=" + rect.left
                + "  rect.top=" + rect.top
                + "  rect.right=" + rect.right
                + "  rect.bottom=" + rect.bottom
        );
//
//        int statusBarHeight = ViewUtils.getStatusBarHeight(targetView.getContext());
//        int left = rect.left;
//        int top = rect.top - statusBarHeight;
//        int right = rect.right;
//        int bottom = rect.bottom - statusBarHeight;
//        if (rect.top <= 0) {
//            top = frameLayout.getHeight() - rect.bottom;
//        }
//        if (rect.bottom <= 0) {
//            bottom = rect.top + frameLayout.getHeight();
//        }
//        Log.d("jihongwen", "left=" + left
//                + "  top=" + top
//                + "  right=" + right
//                + "  bottom=" + bottom
//        );
//        frameLayout.layout(left, top, right, bottom);
        LayoutParams layoutParams = (LayoutParams) frameLayout.getLayoutParams();
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int statusBarHeight = ViewUtils.getStatusBarHeight(targetView.getContext());
        layoutParams.leftMargin = location[0];
        layoutParams.topMargin = location[1] - statusBarHeight;
        layoutParams.rightMargin = screenWidth - layoutParams.leftMargin - targetView.getWidth();
        layoutParams.bottomMargin = frameLayout.getHeight() - location[1] - targetView.getHeight();

        Log.d("jihongwen", "targetView.getWidth()=" + targetView.getWidth() + "  targetView.getHeight()=" + targetView.getHeight());
        Log.d("jihongwen", "layoutParams.leftMargin=" + layoutParams.leftMargin
                + "  layoutParams.topMargin=" + layoutParams.topMargin
                + "  layoutParams.rightMargin=" + layoutParams.rightMargin
                + "  layoutParams.bottomMargin=" + layoutParams.bottomMargin
        );
        frameLayout.setLayoutParams(layoutParams);
    }
}
