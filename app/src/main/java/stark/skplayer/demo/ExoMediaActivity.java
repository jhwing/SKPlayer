package stark.skplayer.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;

/**
 * Created by jihongwen on 16/9/21.
 */

public class ExoMediaActivity extends AppCompatActivity implements OnPreparedListener {
    EMVideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exomedia);
        videoView = (EMVideoView) findViewById(R.id.video_play_activity_video_view);
        videoView.setOnPreparedListener(this);
        videoView.setVideoURI(Samples.uri);
    }

    @Override
    public void onPrepared() {
        videoView.start();
    }
}
