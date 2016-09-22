package stark.skplay;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * Created by jihongwen on 16/9/19.
 */

public class SKVideoView extends RelativeLayout {

    protected VideoViewApi textureVideoView;

    protected SKPlaybackControl controlView;

    protected Uri videoUri;

    protected MuxListener muxListener = new MuxListener();

    public SKVideoView(Context context) {
        super(context);
        setup(context, null);
    }

    public SKVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    public SKVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }

    protected void setup(Context context, @Nullable AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        LayoutInflater.from(context).inflate(R.layout.sk_video_view, this);
        textureVideoView = (SKTextureVideoView) findViewById(R.id.textureVideoView);
        controlView = (SKPlaybackControl) findViewById(R.id.control);
        setPlaybackControlView(controlView);
        textureVideoView.setSKListenerMux(muxListener);

    }

    public void setOnFullScreenListener(SKPlaybackControlView.OnFullScreenListener onFullScreenListener) {
        controlView.setOnFullScreenListener(onFullScreenListener);
    }

    public void setVideoURI(@Nullable Uri uri) {
        videoUri = uri;
        textureVideoView.setVideoURI(uri);
    }

    public void start() {
        controlView.showLoading(true);
        textureVideoView.start();
        setKeepScreenOn(true);
        if (controlView != null) {
            controlView.updatePlaybackState(true);
        }
    }

    private int oldHeight = 0;
    private int oldWidth = 0;

    public void onConfigurationChanged(Configuration newConfig) {
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        int heightPixels = Resources.getSystem().getDisplayMetrics().heightPixels;
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // TODO: 16/9/21  full screen
            if (heightPixels != getLayoutParams().height) {
                oldHeight = getLayoutParams().height;
            }
            if (widthPixels != getLayoutParams().width) {
                oldWidth = getLayoutParams().width;
            }
            getLayoutParams().height = heightPixels;
            getLayoutParams().width = widthPixels;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // TODO: 16/9/21 vertical screen
            getLayoutParams().height = oldHeight;
            getLayoutParams().width = oldWidth;
        }
    }

    public void setPlaybackControlView(SKPlaybackControl controlView) {
        this.controlView = controlView;
        controlView.setVideoView(textureVideoView);
    }

    protected class MuxListener implements SKListenerMux {

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Log.d("jihongwen", "onBufferingUpdate percent" + percent);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            controlView.updatePlaybackState(false);
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            controlView.updatePlaybackState(false);
            return false;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            controlView.showLoading(false);
            controlView.setDuration(mp.getDuration());
        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {

        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return false;
        }
    }
}
