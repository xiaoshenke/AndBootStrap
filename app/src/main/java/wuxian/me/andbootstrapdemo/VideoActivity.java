package wuxian.me.andbootstrapdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import wuxian.me.andbootstrap.BaseActivity;
import wuxian.me.andbootstrap.video.BaseControllerView;
import wuxian.me.andbootstrap.video.DefaultControllerView;
import wuxian.me.andbootstrap.video.VideoView;
import wuxian.me.andbootstrapdemo.R;

/**
 * Created by wuxian on 16/3/2017.
 * <p>
 */

public class VideoActivity extends BaseActivity {
    private static final String TAG = "VideoActivity";
    private final String mTestUrl = "http://172.16.113.130:8081/testVideo.mp4";

    private VideoView mVideoView;

    @BindView(R.id.contianer)
    FrameLayout mContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoView = new VideoView(this);
        mVideoView.setLandscapeMode(true);           //default value
        BaseControllerView controllerView = new DefaultControllerView(this, mVideoView);
        mVideoView.setControllerView(controllerView); //defaultValue
        mVideoView.addOnVideoPlayListener(new VideoView.OnVideoPlay() {
            @Override
            public void onPlayStart() {
                Log.e(TAG, "onPlayStart");
            }

            @Override
            public void onPlayEnd() {
                Log.e(TAG, "onPlayEnd");
            }

            @Override
            public void onPlayError() {
                Log.e(TAG, "onPlayError");
            }
        });

        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        ViewGroup.LayoutParams fllp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mVideoView.getView().setLayoutParams(fllp);
        mContainer.addView(mVideoView.getView());

        mVideoView.startPlay(Uri.parse(mTestUrl));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mVideoView.destroy(); //dont forget this
    }
}
