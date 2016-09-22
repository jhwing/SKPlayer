package stark.skplayer.demo;

import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

/**
 * Created by jihongwen on 16/9/21.
 * <p>
 * videoView sample
 */

public class VideoViewActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    ProgressBar progressBar;
    VideoView videoView;

    boolean isStopped = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(Samples.uri);
        videoView.setOnPreparedListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
        super.onConfigurationChanged(newConfig);
    }

    public void play(View view) {
        if (videoView.isPlaying()) {
            return;
        }
        if (isStopped) {
            videoView.setVideoURI(Samples.uri);
            isStopped = false;
        }
        videoView.start();
        progressBar.setVisibility(View.VISIBLE);
    }

    public void stop(View view) {
        videoView.stopPlayback();
        isStopped = true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        progressBar.setVisibility(View.GONE);
    }
}
