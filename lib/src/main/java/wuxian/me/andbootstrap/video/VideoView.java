package wuxian.me.andbootstrap.video;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by wuxian on 17/3/2017.
 */

public class VideoView {

    private BaseControllerView mControllerView;

    public void setControllerView(@NonNull BaseControllerView controllerView) {
        mControllerView = controllerView;
    }

    public View getView() {
        return null;
    }


    private OnVideoPlay onVideoPlay;

    public void setOnVideoPlay(OnVideoPlay onVideoPlay) {
        this.onVideoPlay = onVideoPlay;
    }

    public interface OnVideoPlay {
        void onPlayStart();

        void onPlayEnd();

        void onPlayError();
    }
}
