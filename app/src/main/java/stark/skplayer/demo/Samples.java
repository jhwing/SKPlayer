package stark.skplayer.demo;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jihongwen on 16/9/18.
 */

public class Samples {

    public final static Uri uri = Uri.parse("http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4");
    public final static Uri maxUri = Uri.parse("http://vf2.mtime.cn/Video/2015/04/29/mp4/150429075009585719.mp4");

    public static class Sample {
        public String thumbUrl;
        public String videoUrl;

        public Sample(String thumbUrl, String videoUrl) {
            this.thumbUrl = thumbUrl;
            this.videoUrl = videoUrl;
        }
    }

    public static Sample getVideoBean() {
        return new Sample("http://img31.mtime.cn/mg/2016/06/20/093700.49306764_270X405X4.jpg", "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4");
    }

    public static List<Sample> getVideoBeanList() {
        ArrayList<Sample> list = new ArrayList<>();
        list.add(new Sample("http://img31.mtime.cn/mg/2016/06/20/093700.49306764_270X405X4.jpg", "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4"));
        return list;
    }
}
