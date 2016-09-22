package stark.skplay;

import android.media.MediaPlayer;

/**
 * Created by jihongwen on 16/9/22.
 */

public interface SKListenerMux extends MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener {
}
