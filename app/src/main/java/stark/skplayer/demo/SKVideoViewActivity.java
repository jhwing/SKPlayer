package stark.skplayer.demo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import stark.skplay.SKPlaybackControlView;
import stark.skplay.SKVideoView;

/**
 * Created by jihongwen on 16/9/18.
 * <p>
 * switch screen onConfigurationChanged
 */

public class SKVideoViewActivity extends AppCompatActivity {

    Uri uri = Samples.maxUri;

    SKVideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sk_video_view);
        videoView = (SKVideoView) findViewById(R.id.skVideoView);

        videoView.setVideoURI(uri);
        videoView.setOnFullScreenListener(new SKPlaybackControlView.OnFullScreenListener() {
            @Override
            public void onFullScreen() {
                if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        videoView.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }
}
