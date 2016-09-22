package stark.skplay;

import android.media.MediaPlayer;
import android.support.annotation.Nullable;

/**
 * Created by jihongwen on 16/9/21.
 */

public class SKMediaPlayerListener implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener {

    @Nullable
    protected MediaPlayer.OnCompletionListener onCompletionListener;
    @Nullable
    protected MediaPlayer.OnPreparedListener onPreparedListener;
    @Nullable
    protected MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener;
    @Nullable
    protected MediaPlayer.OnSeekCompleteListener onSeekCompleteListener;
    @Nullable
    protected MediaPlayer.OnErrorListener onErrorListener;
    @Nullable
    protected MediaPlayer.OnInfoListener onInfoListener;

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

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

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }
}
