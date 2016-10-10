package stark.skplayer.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import stark.skplay.SKSurfaceVideoView;

/**
 * Created by jihongwen on 16/9/29.
 */

public class SKSurfaceVideoViewActivity extends AppCompatActivity {

    List<Samples.Sample> sampleList = Samples.sampleList();

    RecyclerView recyclerList;

    ViewAdapter mViewAdapter;

    LinearLayoutManager linearLayoutManager;

    LayoutInflater mLayoutInflater;

    Activity mActivity;

    SKSurfaceVideoView skSurfaceVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sk_surface);
        mActivity = this;
        mLayoutInflater = LayoutInflater.from(this);
        recyclerList = (RecyclerView) findViewById(R.id.recyclerList);
        mViewAdapter = new ViewAdapter();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerList.setLayoutManager(new LinearLayoutManager(this));
        recyclerList.setAdapter(mViewAdapter);

        skSurfaceVideoView = (SKSurfaceVideoView) findViewById(R.id.skSurfaceVideoView);
    }


    class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.view_sk_surface_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.itemContent.setText("NO." + position);
            holder.skSurfaceVideoView.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return sampleList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            SKSurfaceVideoView skSurfaceVideoView;
            TextView itemContent;

            public ViewHolder(View itemView) {
                super(itemView);
                skSurfaceVideoView = (SKSurfaceVideoView) itemView.findViewById(R.id.skSurfaceVideoView);
                itemContent = (TextView) itemView.findViewById(R.id.itemContent);
            }
        }
    }


}
