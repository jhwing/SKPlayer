package stark.skplay;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.MediaController;

/**
 * Created by jihongwen on 16/9/21.
 */

public interface VideoViewApi extends MediaController.MediaPlayerControl {

    void stopPlayback();

    void suspend();

    void setVideoURI(Uri uri);

    void setSKListenerMux(SKListenerMux listenerMux);

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param listener The callback that will be run
     */
    void setOnPreparedListener(@Nullable MediaPlayer.OnPreparedListener listener);

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param listener The callback that will be run
     */
    void setOnCompletionListener(@Nullable MediaPlayer.OnCompletionListener listener);

    /**
     * Register a callback to be invoked when the status of a network
     * stream's buffer has changed.
     *
     * @param listener the callback that will be run.
     */
    void setOnBufferingUpdateListener(@Nullable MediaPlayer.OnBufferingUpdateListener listener);

    /**
     * Register a callback to be invoked when a seek operation has been
     * completed.
     *
     * @param listener the callback that will be run
     */
    void setOnSeekCompleteListener(@Nullable MediaPlayer.OnSeekCompleteListener listener);

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, TextureVideoView will inform
     * the user of any errors.
     *
     * @param listener The callback that will be run
     */
    void setOnErrorListener(@Nullable MediaPlayer.OnErrorListener listener);

    /**
     * Register a callback to be invoked when an informational event
     * occurs during playback or setup.
     *
     * @param listener The callback that will be run
     */
    void setOnInfoListener(@Nullable MediaPlayer.OnInfoListener listener);

}
