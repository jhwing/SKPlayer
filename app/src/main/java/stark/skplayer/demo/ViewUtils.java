package stark.skplayer.demo;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import java.lang.reflect.Field;

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

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
