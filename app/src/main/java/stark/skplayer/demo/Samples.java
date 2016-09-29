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
        public int id;
        public String thumbUrl;
        public String videoUrl;

        public Sample(String thumbUrl, String videoUrl) {
            this.thumbUrl = thumbUrl;
            this.videoUrl = videoUrl;
        }

        public Sample(int id, String thumbUrl, String videoUrl) {
            this(thumbUrl, videoUrl);
            this.id = id;
        }
    }

    public static Sample sample() {
        return new Sample(1, "http://img31.mtime.cn/mg/2016/06/20/093700.49306764_270X405X4.jpg",
                "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4");
    }

    public static List<Sample> sampleList() {
        ArrayList<Sample> list = new ArrayList<>();
        list.add(new Sample(
                1,
                "",
                "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4"));
        list.add(new Sample(
                2,
                "",
                "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4"));
        list.add(new Sample(
                3,
                "",
                "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4"));
        list.add(new Sample(
                4,
                "",
                "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4"));
        list.add(new Sample(
                5,
                "",
                "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4"));
        list.add(new Sample(
                6,
                "",
                "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4"));
        list.add(new Sample(
                7,
                "",
                "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4"));
        list.add(new Sample(
                8,
                "",
                "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4"));
        list.add(new Sample(
                9,
                "",
                "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4"));
        list.add(new Sample(
                10,
                "",
                "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4"));
        list.add(new Sample(
                11,
                "",
                "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4"));
        list.add(new Sample(
                12,
                "",
                "http://vf1.mtime.cn/Video/2016/07/24/mp4/160724091914077918.mp4"));
        return list;
    }
}
