package stark.skplayer.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView videoTestList;

    LayoutInflater mInflater;

    List<Class> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activities.add(SKVideoViewActivity.class);
        activities.add(VideoViewActivity.class);
        activities.add(ExoMediaActivity.class);
        activities.add(SKVideoViewListActivity.class);
//        activities.add(WebVideoActivity.class);
//        activities.add(WebVideoListActivity.class);

        mInflater = LayoutInflater.from(this);
        videoTestList = (RecyclerView) findViewById(R.id.videoTestList);
        videoTestList.setLayoutManager(new LinearLayoutManager(this));
        videoTestList.setAdapter(new VideoTestAdapter());
    }

    private class VideoTestAdapter extends RecyclerView.Adapter<VideoTestAdapter.VideoTestViewHolder> {

        @Override
        public VideoTestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VideoTestViewHolder(mInflater.inflate(R.layout.view_main_item, parent, false));
        }

        @Override
        public void onBindViewHolder(VideoTestViewHolder holder, int position) {
            holder.activityText.setText(activities.get(position).getSimpleName());
        }

        @Override
        public int getItemCount() {
            return activities == null ? 0 : activities.size();
        }

        class VideoTestViewHolder extends RecyclerView.ViewHolder {

            public TextView activityText;

            public VideoTestViewHolder(View itemView) {
                super(itemView);
                activityText = (TextView) itemView.findViewById(R.id.activityText);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, activities.get(getLayoutPosition())));
                    }
                });
            }
        }
    }
}
