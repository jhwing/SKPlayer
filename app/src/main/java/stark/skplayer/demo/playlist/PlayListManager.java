package stark.skplayer.demo.playlist;

import android.util.SparseArray;

/**
 * Created by jihongwen on 16/9/28.
 */

public class PlayListManager {

    int index = 0;

    SparseArray<String> videoList = new SparseArray<>();

    public void setVideoList(SparseArray<String> videoList) {
        this.videoList = videoList;
    }

    public String play() {
        return videoList.get(index);
    }

    public String next() {
        return videoList.get(++index);
    }

    public String prev() {
        return videoList.get(--index);
    }

}
