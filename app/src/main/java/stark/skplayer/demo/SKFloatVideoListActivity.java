package stark.skplayer.demo;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import stark.skplayer.demo.widget.FloatVideoLayout;
import stark.skplayer.demo.widget.MyVideoView;

/**
 * Created by jihongwen on 2016/10/10.
 */

public class SKFloatVideoListActivity extends AppCompatActivity {

    List<Samples.Sample> sampleList = Samples.sampleList();

    RecyclerView videoList;

    LayoutInflater mInflater;

    Context mContext;

    LinearLayoutManager linearLayoutManager;

    FloatVideoLayout floatVideoView;

    MyVideoView mVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(this);
        setContentView(R.layout.activity_sk_float_video_view);
        floatVideoView = (FloatVideoLayout) findViewById(R.id.floatVideoView);
        videoList = (RecyclerView) findViewById(R.id.recyclerList);
        linearLayoutManager = new LinearLayoutManager(this);
        videoList.setLayoutManager(linearLayoutManager);
        videoList.setAdapter(new ViewAdapter());
        videoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                floatVideoView.updateVideoViewLocation(mVideoView);
            }
        });
    }

    public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.VideoViewHolder> {

        @Override
        public ViewAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewAdapter.VideoViewHolder(mInflater.inflate(R.layout.view_video_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewAdapter.VideoViewHolder holder, int position) {
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
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mVideoView = videoView;
                        final MyVideoView myVideoView = new MyVideoView(v.getContext());
                        myVideoView.setTag("videoView");
                        floatVideoView.init();
                        floatVideoView.addVideoView(myVideoView);
                        //floatVideoView.addView(myVideoView, videoView.getWidth(), videoView.getHeight());
                        Button button = new Button(v.getContext());
                        button.setText("full screen");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                floatVideoView.setFullScreen(true);
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                myVideoView.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().heightPixels;
                                myVideoView.getLayoutParams().height = Resources.getSystem().getDisplayMetrics().widthPixels;
                            }
                        });
                        myVideoView.addView(button);
                        floatVideoView.updateVideoViewLocation(videoView);
                    }
                });
            }
        }
    }
}
