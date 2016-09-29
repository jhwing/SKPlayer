package stark.skplayer.demo;

import android.graphics.Rect;
import android.view.View;

/**
 * Created by jihongwen on 16/9/28.
 */

public class ViewUtils {

    public static int getVisibilityPercents(View view) {
        int percents = 100;
        final Rect viewRect = new Rect();
        view.getLocalVisibleRect(viewRect);

        int height = view.getHeight();

        if (viewRect.top > 0) {
            percents = (height - viewRect.top) * 100 / height;
        } else if (viewRect.bottom > 0 && viewRect.bottom < height) {
            percents = viewRect.bottom * 100 / height;
        }

        return percents;
    }
}
