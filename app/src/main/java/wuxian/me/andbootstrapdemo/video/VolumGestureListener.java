package wuxian.me.andbootstrapdemo.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ProgressBar;

/**
 * Created by wuxian on 16/3/2017.
 */

public class VolumGestureListener extends GestureDetector.SimpleOnGestureListener {

    private IVolumListener mListener;
    private int mScreenWidth;

    public VolumGestureListener(@NonNull Context context, @NonNull IVolumListener volumListener) {
        this.mListener = volumListener;

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        mScreenWidth = display.getWidth();
    }

    //目前只允许屏幕的1/3出滑动可以进行调整音量
    public boolean onScroll(MotionEvent motion1, MotionEvent motion2, float distanceX, float distanceY) {

        float oldX = motion1.getX();
        if (oldX < mScreenWidth / 3.0) {
            /*
            int height = mProgressBar.getMeasuredHeight();
            int max = mProgressBar.getMax();
            int pro = mProgressBar.getProgress();


            pro += distanceY * max / height;
            pro = pro < max ? pro : max;
            pro = pro >= 0 ? pro : 0;
            mProgressBar.setProgress(pro);
            */

            mListener.updateVolumBy((int) distanceX, (int) distanceY);
        }

        return true;
    }

    public boolean onSingleTapUp(MotionEvent e) {
        //setVideoControlVisibility(); // --> Todo
        return true;
    }
}
