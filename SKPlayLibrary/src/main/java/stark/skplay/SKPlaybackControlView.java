package stark.skplay;

import android.content.Context;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import stark.skplay.utils.Repeater;
import stark.skplay.utils.TimeFormatUtil;

/**
 * Created by jihongwen on 16/9/18.
 */

public class SKPlaybackControlView extends RelativeLayout implements SKPlaybackControl {

    VideoViewApi videoView;

    protected ProgressBar progressBar;
    protected VideoSpeedView videoSpeedView;
    protected View shadeView;
    protected View playPauseImg;
    protected Button previous;
    protected Button next;
    protected Button playPause;
    protected Button stop;
    protected SeekBar seekBar;
    protected TextView currentTime;
    protected TextView endTime;

    protected Repeater progressPollRepeater = new Repeater();

    private int seekToTime;

    boolean userInteracting = false;

    boolean isStoped = false;

    public SKPlaybackControlView(Context context) {
        super(context);
        setup(context);
    }

    public SKPlaybackControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public SKPlaybackControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    protected void setup(Context context) {
        View.inflate(context, R.layout.sk_playback_control_view, this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        videoSpeedView = (VideoSpeedView) findViewById(R.id.videoSpeedView);
        shadeView = findViewById(R.id.shadeView);
        playPauseImg = findViewById(R.id.playPauseImg);
        previous = (Button) findViewById(R.id.previous);
        next = (Button) findViewById(R.id.next);
        playPause = (Button) findViewById(R.id.playPause);
        stop = (Button) findViewById(R.id.stop);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        currentTime = (TextView) findViewById(R.id.currentTime);
        endTime = (TextView) findViewById(R.id.remainingTime);

        shadeView.setOnTouchListener(new TouchListener(getContext()));

        previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        playPauseImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying() && videoView.canPause()) {
                    videoView.pause();
                    updatePlaybackState(false);
                } else {
                    videoView.start();
                    updatePlaybackState(true);
                }
            }
        });

        playPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoped) {
                    isStoped = false;
                    videoView.prepareAsync();
                    videoView.start();
                    showLoading(true);
                    updatePlaybackState(true);
                    return;
                }
                if (videoView.isPlaying() && videoView.canPause()) {
                    videoView.pause();
                    updatePlaybackState(false);
                } else {
                    videoView.start();
                    updatePlaybackState(true);
                }
            }
        });

        stop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.stopPlayback();
                updatePlaybackState(false);
                isStoped = true;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekToTime = progress;
                if (currentTime != null) {
                    currentTime.setText(TimeFormatUtil.formatMs(seekToTime));
                }
                if (endTime != null) {
                    endTime.setText(TimeFormatUtil.formatMs(seekBar.getMax() - seekToTime));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                userInteracting = true;
                seekStart();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userInteracting = false;
                seekEnd(seekToTime);
            }
        });
        progressPollRepeater.setRepeatListener(new Repeater.RepeatListener() {
            @Override
            public void onRepeat() {
                updateProgress();
            }
        });
    }

    private void seekEnd(int seekTime) {
        if (videoView != null) {
            videoView.seekTo(seekTime);
        }
        videoView.start();
    }

    private void seekStart() {
        if (videoView != null) {
            videoView.pause();
        }
    }

    @Override
    public void setDuration(@IntRange(from = 0) long duration) {
        if (duration != seekBar.getMax()) {
            endTime.setText(TimeFormatUtil.formatMs(duration));
            seekBar.setMax((int) duration);
        }
    }

    @Override
    public void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(VISIBLE);
        } else {
            progressBar.setVisibility(GONE);
        }
    }

    public void setVideoView(VideoViewApi videoView) {
        this.videoView = videoView;
    }

    public void seekTo(int msec) {
        videoView.seekTo(msec);
    }

    public void updateProgress() {
        if (videoView != null && !userInteracting) {
            updateProgress(videoView.getCurrentPosition(), videoView.getDuration(), videoView.getBufferPercentage());
        }
    }

    public void updateProgress(@IntRange(from = 0) long position, @IntRange(from = 0) long duration, @IntRange(from = 0, to = 100) int bufferPercent) {
        seekBar.setSecondaryProgress((int) (seekBar.getMax() * ((float) bufferPercent / 100)));
        seekBar.setProgress((int) position);
        String cTimeStr = TimeFormatUtil.formatMs(position);
        String eTimeStr = TimeFormatUtil.formatMs(seekBar.getMax() - position);
        currentTime.setText(TimeFormatUtil.formatMs(position));
        endTime.setText(TimeFormatUtil.formatMs(seekBar.getMax() - position));
        videoSpeedView.setCurrentTime(cTimeStr);
        videoSpeedView.setEndTime(eTimeStr);
    }

    public void updatePlaybackState(boolean isPlaying) {
        updatePlayPauseImage(isPlaying);
        if (isPlaying) {
            progressPollRepeater.start();
        } else {
            progressPollRepeater.stop();
        }
    }

    public void updatePlayPauseImage(boolean isPlaying) {
        if (isPlaying) {
            playPause.setText("pause");
            playPauseImg.setVisibility(GONE);
        } else {
            playPause.setText("play");
            playPauseImg.setVisibility(VISIBLE);
        }
    }

    protected int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    class TouchListener extends GestureDetector.SimpleOnGestureListener implements OnTouchListener {

        int STEP_PROGRESS = 100;
        protected GestureDetector gestureDetector;

        boolean isHorizontalScroll = false;

        boolean isVerticalScroll = false;

        boolean firstScroll = false;

        TouchListener(Context context) {
            gestureDetector = new GestureDetector(context, this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    isHorizontalScroll = false;
                    isVerticalScroll = false;
                    videoSpeedView.setVisibility(GONE);
            }
            return gestureDetector.onTouchEvent(event);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            firstScroll = true;
            isHorizontalScroll = false;
            isVerticalScroll = false;
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (firstScroll) {
                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    isHorizontalScroll = true;
                    videoSpeedView.setVisibility(VISIBLE);
                } else {
                    isVerticalScroll = true;
                }
            }

            if (isHorizontalScroll) { // 横向移动大于纵向移动
                // distanceX 向左是正数,向右是负数
                if (distanceX >= 1) {
                    if (seekToTime > STEP_PROGRESS) {
                        seekToTime -= STEP_PROGRESS;
                        Log.d("jihongwen", "seekToTime--");
                        videoSpeedView.convertBack();
                    }
                } else if (distanceX <= -1) {
                    if (seekToTime < seekBar.getMax()) {
                        seekToTime += STEP_PROGRESS;
                        Log.d("jihongwen", "seekToTime++");
                        videoSpeedView.convertForward();
                    }
                }
                if (seekToTime < 0) {
                    seekToTime = 0;
                }
                Log.d("jihongwen", "seekToTime:" + seekToTime + "  seekBar max:" + seekBar.getMax());
                Log.d("jihongwen", "distanceX:" + distanceX + "  distanceY:" + distanceY);
                seekTo(seekToTime);
            }
            firstScroll = false;
            return true;
        }
    }
}
