package stark.skplayer.demo.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import stark.skplay.SKListenerMux;
import stark.skplay.SKVideoView;
import stark.skplayer.demo.R;

/**
 * Created by jihongwen on 16/9/28.
 */

public class MyVideoView extends RelativeLayout implements SKListenerMux {

    int mPosition;

    Callback callback;

    Uri mUri;

    ImageView videoPreView;

    SKVideoView skVideoView;

    ImageView playBtn;

    boolean isPrepared = false;

    boolean isPlaying = false;

    public MyVideoView(Context context) {
        super(context);
        init(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.view_my_video_view, this);
        videoPreView = (ImageView) findViewById(R.id.videoPreView);
        playBtn = (ImageView) findViewById(R.id.playBtn);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("jihongwen", " MyVideoView onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("jihongwen", " MyVideoView onTouchEvent");
        return super.onTouchEvent(event);
    }

    public void setSkVideoView(SKVideoView skVideoView) {
        this.skVideoView = skVideoView;
        addView(skVideoView);
    }

    public SKVideoView getSkVideoView() {
        return skVideoView;
    }

    public void hidePreView() {
        videoPreView.setVisibility(GONE);
        playBtn.setVisibility(GONE);
    }

    public void showPreView() {
        videoPreView.setVisibility(VISIBLE);
        playBtn.setVisibility(VISIBLE);
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public void setVideoURI(String videoUrl) {
        if (videoUrl == null) {
            videoUrl = "";
        }
        mUri = Uri.parse(videoUrl);
        Log.d("jihongwen", "setVideoURI:" + mUri);
    }

    public Uri getVideoURI() {
        return mUri;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void start() {
        Log.d("jihongwen", "start position:" + mPosition);
        if (isPlaying) {
            return;
        }
        isPlaying = true;
        if (!isPrepared) {
            skVideoView.setVideoURI(mUri);
        }
        skVideoView.start();
        hidePreView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        remove();
    }

    public void remove() {
        isPlaying = false;
        if (skVideoView != null) {
            if (skVideoView.isPlaying()) {

            }
            skVideoView.suspend();
            removeView(skVideoView);
        }
        Log.d("jihongwen", "stop mUri:" + mUri);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void resetView() {
        showPreView();
        if (skVideoView != null) {
            removeView(skVideoView);
        }
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (callback != null) {
            callback.onCompletion(mp);
            showPreView();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        hidePreView();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    public void checkPlay() {
        if (!isPlaying) {
            resetView();
        }
    }

    public interface Callback {
        void onCompletion(MediaPlayer mp);
    }
}
