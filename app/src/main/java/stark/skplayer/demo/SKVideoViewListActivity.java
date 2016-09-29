package stark.skplayer.demo;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.List;

import stark.skplayer.demo.widget.MyVideoView;

/**
 * Created by jihongwen on 16/9/18.
 */

public class SKVideoViewListActivity extends AppCompatActivity implements MyVideoView.Callback {

    List<Samples.Sample> sampleList = Samples.sampleList();

    RecyclerView videoList;

    LayoutInflater mInflater;

    Context mContext;

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sk_video_view_list);
        mContext = this;
        mInflater = LayoutInflater.from(this);
        videoList = (RecyclerView) findViewById(R.id.videoList);
        linearLayoutManager = new LinearLayoutManager(this);
        videoList.setLayoutManager(linearLayoutManager);

        videoList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int direction = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Log.d("jihongwen", "onScrollStateChanged SCROLL_STATE_IDLE");
                        checkPlayView(recyclerView);
                        VideoPlayHelper.tryPlay(direction);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    direction = 1;// down
                } else if (dy < 0) {
                    direction = -1;// up
                }
                int state = recyclerView.getScrollState();
                switch (state) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Log.d("jihongwen", "onScrolled SCROLL_STATE_IDLE");
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
        videoList.setAdapter(new ViewAdapter());
        final ViewTreeObserver treeObserver = videoList.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                Log.d("jihongwen", "onGlobalLayout ");
                checkPlayView(videoList);
                VideoPlayHelper.tryPlay(1);
                if (treeObserver.isAlive()) {
                    treeObserver.removeOnGlobalLayoutListener(this);
                } else {
                    videoList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

    }

    /**
     * 检测是否应该移除播放,或者添加待播放
     *
     * @param recyclerView
     */
    private void checkPlayView(RecyclerView recyclerView) {
        VideoPlayHelper.clear();
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = recyclerView.getChildAt(i);
            ViewAdapter.VideoViewHolder videoViewHolder = (ViewAdapter.VideoViewHolder) recyclerView.getChildViewHolder(childView);
            int percents = ViewUtils.getVisibilityPercents(videoViewHolder.itemView);
            if (percents > 50) {
                // 可播放
                Log.d("jihongwen", "position:" + videoViewHolder.getLayoutPosition() + "  percents" + percents);
                VideoPlayHelper.addPlayList(videoViewHolder.videoView, videoViewHolder.getLayoutPosition());
            } else {
                Log.d("jihongwen", "position:" + videoViewHolder.getLayoutPosition() + "  percents" + percents);
                if (videoViewHolder.videoView.isPlaying()) {
                    videoViewHolder.videoView.remove();
                    videoViewHolder.videoView.resetView();
                }
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d("jihongwen", "onCompletion mp:" + mp.getAudioSessionId() + mp);
    }

    public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.VideoViewHolder> {

        @Override
        public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VideoViewHolder(mInflater.inflate(R.layout.view_video_item, parent, false));
        }

        @Override
        public void onBindViewHolder(VideoViewHolder holder, int position) {
            Samples.Sample sample = sampleList.get(position);
            holder.videoTitle.setText("NO." + sample.id);
            holder.videoView.setVideoURI(sample.videoUrl);
            holder.videoView.checkPlay();
            Log.d("jihongwen", "holder getAdapterPosition" + holder.getAdapterPosition());
        }

        @Override
        public int getItemCount() {
            return sampleList.size();
        }

        public class VideoViewHolder extends RecyclerView.ViewHolder {

            public MyVideoView videoView;

            public TextView videoTitle;

            public VideoViewHolder(View itemView) {
                super(itemView);
                videoView = (MyVideoView) itemView.findViewById(R.id.videoView);
                videoTitle = (TextView) itemView.findViewById(R.id.videoTitle);
                videoView.setCallback(SKVideoViewListActivity.this);
            }
        }
    }
}
