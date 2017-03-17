package wuxian.me.andbootstrap.video;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by wuxian on 16/3/2017.
 */

public class VideoProgressHandler extends Handler {
    public static final int MESSAGE_FADE_OUT = 101;
    public static final int MESSGAE_SHOW_PROGRESS = 102;

    private final WeakReference<VideoControllerView2> mControllerView;

    VideoProgressHandler(VideoControllerView2 view) {
        mControllerView = new WeakReference<VideoControllerView2>(view);
    }

    @Override
    public void handleMessage(Message msg) {
        VideoControllerView2 view = mControllerView.get();
        if (view == null || view.player() == null) {
            return;
        }
        int pos;
        switch (msg.what) {
            case MESSAGE_FADE_OUT:
                view.hide();
                break;
            case MESSGAE_SHOW_PROGRESS:
                pos = view.updateProgress();
                if (!view.dragging() && view.showing() && view.player().isPlaying()) {
                    removeMessages(MESSGAE_SHOW_PROGRESS);
                    msg = obtainMessage(MESSGAE_SHOW_PROGRESS);
                    sendMessageDelayed(msg, 1000 - (pos % 1000));
                }
                break;
        }
    }
}
