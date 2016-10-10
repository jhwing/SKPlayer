package stark.skplay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by jihongwen on 16/9/29.
 */

public class SKSurfaceVideoView extends SurfaceView {

    private static final String TAG = SKSurfaceVideoView.class.getSimpleName();

    private int mPosition = -1;
    private SurfaceHolder holder;
    private RenderThread renderThread;
    private Paint paint;
    private Path path;
    private int mWidth, mHeight;
    private boolean isDraw = false;// 控制绘制的开关

    public SKSurfaceVideoView(Context context) {
        super(context);
        init(context);
    }

    public SKSurfaceVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SKSurfaceVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SKSurfaceVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.RED);

        holder = getHolder();
        holder.addCallback(mSHCallback);
        isDraw = true;
        renderThread = new RenderThread();
        // this value is set automatically when needed.
        // getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    /**
     * 绘制界面的线程
     *
     * @author Administrator
     */
    private class RenderThread extends Thread {
        @Override
        public void run() {
            // 不停绘制界面
            while (isDraw) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                drawUI();
            }
            super.run();
        }
    }

    /**
     * 界面绘制
     */
    public void drawUI() {
        Canvas canvas = holder.lockCanvas();
        try {
            drawCanvas(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawCanvas(Canvas canvas) {
        // 在 canvas 上绘制需要的图形
        int pWidth = 20;
        int line = 200;
        int startX = mWidth / 2 - line / 2;
        int startY = mHeight / 2 - pWidth / 2;
        int stopX = mWidth / 2 + line / 2;
        int stopY = startY;

        paint.setStrokeWidth(pWidth);
        path.reset();
        path.moveTo(startX, startY);

        path.quadTo(startX + 100, startY + 100, stopX + 200, stopX + 200);
        canvas.drawPath(path, paint);
        canvas.drawPoint(startX + 100, startY + 100, paint);

        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mWidth = getWidth();
            mHeight = getHeight();
            renderThread.start();
            Log.d(TAG, "surfaceCreated position:[" + mPosition + "]");
            Log.d(TAG, "surfaceCreated mWidth:[" + mWidth + "]" + " mHeight:[" + mHeight + "]");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mWidth = width;
            mHeight = height;
            Log.d(TAG, "surfaceChanged position:[" + mPosition + "]");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            isDraw = false;
            Log.d(TAG, "surfaceDestroyed position:[" + mPosition + "]");
        }
    };
}
