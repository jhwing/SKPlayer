package stark.skplay;

import android.support.annotation.IntRange;

/**
 * Created by jihongwen on 16/9/21.
 */

public interface SKPlaybackControl {

    void updatePlaybackState(boolean b);

    void setVideoView(VideoViewApi textureVideoView);

    void setDuration(@IntRange(from = 0) long duration);

    void showLoading(boolean show);

}
