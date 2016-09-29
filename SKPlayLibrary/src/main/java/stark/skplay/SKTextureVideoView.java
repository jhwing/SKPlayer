package stark.skplay;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.widget.MediaController;

import java.io.IOException;
import java.util.Map;

/**
 * A "Native" VideoView implementation using the {@link android.view.TextureView}
 * as a backing instead of the older {@link android.view.SurfaceView}.  This
 * resolves issues with the SurfaceView because the TextureView is an actual
 * View that follows the normal drawing paths; allowing the view to be animated,
 * scaled, etc.
 * <br><br>
 * NOTE: This does remove some of the functionality from the VideoView including:
 * <ul>
 * <li>The {@link MediaController}</li>
 * </ul>
 */
public class SKTextureVideoView extends ResizingTextureView implements VideoViewApi {

    private static final String TAG = "SKTextureVideoView";

    protected enum State {
        IDLE,
        PREPARING,
        PREPARED,
        PLAYING,
        PAUSED,
        COMPLETED,
        ERROR,
    }

    protected Map<String, String> headers;

    protected State currentState = State.IDLE;

    protected MediaPlayer mediaPlayer;

    protected Uri mUri;

    protected boolean playRequested = false;
    protected int requestedSeek;
    protected int currentBufferPercent;

    @NonNull
    protected InternalListeners internalListeners = new InternalListeners();

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

    public SKTextureVideoView(Context context) {
        super(context);
        setup(context, null);
    }

    public SKTextureVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    public SKTextureVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SKTextureVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup(context, attrs);
    }

    protected void setup(Context context, @Nullable AttributeSet attrs) {

        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();

        //initMediaView();
    }

    private void initMediaView() {
        initMediaPlayer();
        setSurfaceTextureListener(new TextureVideoViewSurfaceListener());
        updateVideoSize(0, 0);
        currentState = State.IDLE;
    }

    protected void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnInfoListener(internalListeners);
        mediaPlayer.setOnErrorListener(internalListeners);
        mediaPlayer.setOnPreparedListener(internalListeners);
        mediaPlayer.setOnCompletionListener(internalListeners);
        mediaPlayer.setOnSeekCompleteListener(internalListeners);
        mediaPlayer.setOnBufferingUpdateListener(internalListeners);
        mediaPlayer.setOnVideoSizeChangedListener(internalListeners);

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setScreenOnWhilePlaying(true);
    }

    @Override
    public void start() {
        if (isInPlaybackState()) {
            mediaPlayer.start();
            requestFocus();
            currentState = State.PLAYING;
        }

        playRequested = true;
    }

    @Override
    public void pause() {
        if (isInPlaybackState() && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            currentState = State.PAUSED;
        }

        playRequested = false;
    }

    @Override
    public int getDuration() {
        if (isInPlaybackState()) {
            return mediaPlayer.getDuration();
        }

        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return mediaPlayer.getCurrentPosition();
        }

        return 0;
    }

    @Override
    public void seekTo(int msec) {
        if (isInPlaybackState()) {
            mediaPlayer.seekTo(msec);
            Log.d("jihongwen", " seekTo :" + msec);
            requestedSeek = 0;
        } else {
            Log.d("jihongwen", "seekToTime seekTo isInPlaybackState not");
            requestedSeek = msec;
        }
    }

    @Override
    public boolean isPlaying() {
        return isInPlaybackState() && mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        if (mediaPlayer != null) {
            return currentBufferPercent;
        }

        return 0;
    }

    protected boolean isInPlaybackState() {
        return mediaPlayer != null &&
                currentState != State.ERROR &&
                currentState != State.IDLE &&
                currentState != State.PREPARING;
    }

    @Override
    public boolean canPause() {
        return currentState == State.PREPARED || currentState == State.PLAYING;
    }

    @Override
    public boolean canSeekBackward() {
        return currentState == State.PREPARED || currentState == State.PLAYING || currentState == State.PAUSED;
    }

    @Override
    public boolean canSeekForward() {
        return currentState == State.PREPARED || currentState == State.PLAYING || currentState == State.PAUSED;
    }

    /**
     * Performs the functionality to stop the video in playback
     */
    @Override
    public void stopPlayback() {

        if (isInPlaybackState()) {
            try {
                mediaPlayer.stop();
            } catch (Exception e) {
                Log.d(TAG, "stopPlayback: error calling mediaPlayer.stop()", e);
            }
        }
        playRequested = false;
    }

    /**
     * Cleans up the resources being held.  This should only be called when
     * destroying the video view
     */
    @Override
    public void suspend() {

        try {
            mediaPlayer.reset();
            mediaPlayer.release();
        } catch (Exception e) {
            Log.d(TAG, "stopPlayback: error calling mediaPlayer.reset() or mediaPlayer.release()", e);
        }

        currentState = State.IDLE;
        playRequested = false;
    }

    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }

    /**
     * Sets video URI.
     *
     * @param uri the URI of the video.
     */
    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    @Override
    public void prepareAsync() {
        try {
            mediaPlayer.prepareAsync();

            currentState = State.PREPARING;
        } catch (IllegalStateException | IllegalArgumentException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            currentState = State.ERROR;

            internalListeners.onError(mediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }
    }

    @Override
    public void setSKListenerMux(SKListenerMux listenerMux) {
        setOnBufferingUpdateListener(listenerMux);
        setOnSeekCompleteListener(listenerMux);
        setOnCompletionListener(listenerMux);
        setOnPreparedListener(listenerMux);
        setOnErrorListener(listenerMux);
        setOnInfoListener(listenerMux);
    }

    /**
     * Sets video URI using specific headers.
     *
     * @param uri     The Uri for the video to play
     * @param headers the headers for the URI request.
     *                Note that the cross domain redirection is allowed by default, but that can be
     *                changed with key/value pairs through the headers parameter with
     *                "android-allow-cross-domain-redirect" as the key and "0" or "1" as the value
     *                to disallow or allow cross domain redirection.
     */
    public void setVideoURI(Uri uri, @Nullable Map<String, String> headers) {
        mUri = uri;
        this.headers = headers;
        requestedSeek = 0;
        playRequested = false;

        openVideo(uri);
        //requestLayout();
        //invalidate();
    }

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param listener The callback that will be run
     */
    public void setOnPreparedListener(@Nullable MediaPlayer.OnPreparedListener listener) {
        onPreparedListener = listener;
    }

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param listener The callback that will be run
     */
    public void setOnCompletionListener(@Nullable MediaPlayer.OnCompletionListener listener) {
        onCompletionListener = listener;
    }

    /**
     * Register a callback to be invoked when the status of a network
     * stream's buffer has changed.
     *
     * @param listener the callback that will be run.
     */
    public void setOnBufferingUpdateListener(@Nullable MediaPlayer.OnBufferingUpdateListener listener) {
        onBufferingUpdateListener = listener;
    }

    /**
     * Register a callback to be invoked when a seek operation has been
     * completed.
     *
     * @param listener the callback that will be run
     */
    public void setOnSeekCompleteListener(@Nullable MediaPlayer.OnSeekCompleteListener listener) {
        onSeekCompleteListener = listener;
    }

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, TextureVideoView will inform
     * the user of any errors.
     *
     * @param listener The callback that will be run
     */
    public void setOnErrorListener(@Nullable MediaPlayer.OnErrorListener listener) {
        onErrorListener = listener;
    }

    /**
     * Register a callback to be invoked when an informational event
     * occurs during playback or setup.
     *
     * @param listener The callback that will be run
     */
    public void setOnInfoListener(@Nullable MediaPlayer.OnInfoListener listener) {
        onInfoListener = listener;
    }

    protected void openVideo(@Nullable Uri uri) {
        if (uri == null) {
            return;
        }

        currentBufferPercent = 0;
        initMediaView();
        requestLayout();
        invalidate();
        try {
            mediaPlayer.setDataSource(getContext().getApplicationContext(), uri, headers);
            mediaPlayer.prepareAsync();

            currentState = State.PREPARING;
        } catch (IOException | IllegalArgumentException ex) {
            Log.w(TAG, "Unable to open content: " + uri, ex);
            currentState = State.ERROR;

            internalListeners.onError(mediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }
    }

    protected class InternalListeners implements MediaPlayer.OnBufferingUpdateListener,
            MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener,
            MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener,
            MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            currentBufferPercent = percent;
            if (onBufferingUpdateListener != null) {
                onBufferingUpdateListener.onBufferingUpdate(mp, percent);
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            currentState = State.COMPLETED;
            if (onCompletionListener != null) {
                onCompletionListener.onCompletion(mediaPlayer);
            }
        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {
            if (onSeekCompleteListener != null) {
                onSeekCompleteListener.onSeekComplete(mp);
            }
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.d(TAG, "Error: " + what + "," + extra);
            currentState = State.ERROR;

            return onErrorListener == null || onErrorListener.onError(mediaPlayer, what, extra);
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            currentState = State.PREPARED;

            if (onPreparedListener != null) {
                onPreparedListener.onPrepared(mediaPlayer);
            }

            updateVideoSize(mp.getVideoWidth(), mp.getVideoHeight());

            if (requestedSeek != 0) {
                seekTo(requestedSeek);
            }

            if (playRequested) {
                start();
            }
        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return onInfoListener == null || onInfoListener.onInfo(mp, what, extra);
        }

        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            if (updateVideoSize(mp.getVideoWidth(), mp.getVideoHeight())) {
                requestLayout();
            }
        }
    }

    protected class TextureVideoViewSurfaceListener implements SurfaceTextureListener {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            try {
                mediaPlayer.setSurface(new Surface(surfaceTexture));
                if (playRequested) {
                    start();
                }
            } catch (IllegalStateException e) {
                Log.d(TAG, "mediaPlayer IllegalStateException ");
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            if (mediaPlayer == null || width <= 0 || height <= 0) {
                return;
            }

            if (requestedSeek != 0) {
                seekTo(requestedSeek);
            }

            if (playRequested) {
                start();
            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            surface.release();
            suspend();
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            //Purposefully left blank
        }
    }
}