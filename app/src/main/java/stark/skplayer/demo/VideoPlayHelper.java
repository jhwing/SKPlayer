package stark.skplayer.demo;

import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import stark.skplay.SKVideoView;
import stark.skplayer.demo.widget.MyVideoView;

/**
 * Created by jihongwen on 16/9/28.
 */

public class VideoPlayHelper {

    static SparseArray<MyVideoView> requestInfos = new SparseArray<>();

    static MyVideoView myVideoView;

    public static void addPlayList(MyVideoView myVideoView, int position) {
        if (requestInfos.get(position) == null) {
            requestInfos.append(position, myVideoView);
            Log.d("jihongwen", "addPlayList position:" + position);
        } else {
            //Log.d("jihongwen", "has in list position:" + position);
        }
    }

    public static void tryPlay(final int direction) {
        if (requestInfos.size() != 0) {

            if (checkPlay()) {
                return;
            }

            int size = requestInfos.size();

            int key = requestInfos.keyAt(0);
            for (int i = 0; i < size; i++) {
                int tempKey = requestInfos.keyAt(i);
                if (direction < 0) {
                    if (tempKey > key) {
                        key = tempKey;
                    }
                } else {
                    if (tempKey < key) {
                        key = tempKey;
                    }
                }
            }


            myVideoView = requestInfos.get(key);
            if (myVideoView.getSkVideoView() != null) {
                if (myVideoView.isPlaying()) {
                    Log.d("jihongwen", "myVideoView is playing");
                    return;
                }
            }
            myVideoView.setSkVideoView(new SKVideoView(myVideoView.getContext()));
            myVideoView.setPosition(key);
            myVideoView.start();
        }
    }

    private static boolean checkPlay() {
        for (int i = 0; i < requestInfos.size(); i++) {
            int key = requestInfos.keyAt(i);
            if (requestInfos.get(key).isPlaying()) {
                return true;
            }
        }
        return false;
    }

    public static void clear() {
        requestInfos.clear();
    }


    public static class Factory {

        public static SKVideoView create(ViewGroup parent) {
            SKVideoView skVideoView = new SKVideoView(parent.getContext());
            parent.addView(skVideoView);
            return skVideoView;
        }
    }

}
