package stark.skplay;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jihongwen on 16/9/22.
 */

public class VideoSpeedView extends RelativeLayout {

    ImageView speed_icn;
    TextView currentTime;
    TextView endTime;

    public VideoSpeedView(Context context) {
        super(context);
        init(context);
    }

    public VideoSpeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoSpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoSpeedView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.sk_view_video_speed, this);
        speed_icn = (ImageView) findViewById(R.id.speed_icn);
        currentTime = (TextView) findViewById(R.id.currentTime);
        endTime = (TextView) findViewById(R.id.endTime);
    }

    public void convertBack() {
        speed_icn.setScaleX(-1);
        speed_icn.setScaleY(1);
    }

    public void convertForward() {
        speed_icn.setScaleX(1);
        speed_icn.setScaleY(-1);
    }

    public void setCurrentTime(String time) {
        currentTime.setText(time);
    }

    public void setEndTime(String time) {
        endTime.setText(time);
    }

}
