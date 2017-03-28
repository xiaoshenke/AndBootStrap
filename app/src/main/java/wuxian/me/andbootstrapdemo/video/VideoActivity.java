package wuxian.me.andbootstrapdemo.video;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wuxian.me.andbootstrap.BaseActivity;
import wuxian.me.andbootstrap.video.VideoView;
import wuxian.me.andbootstrapdemo.R;

/**
 * Created by wuxian on 16/3/2017.
 * <p>
 */

public class VideoActivity extends BaseActivity {
    private boolean mOnTop = false;
    private boolean mLandscapeMode = false;
    private final String mTestUrl = "http://172.16.113.130:8081/testVideo.mp4";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VideoView videoView = new VideoView(this);

        setContentView(videoView.getView());
        ButterKnife.bind(this);

        videoView.startPlay(Uri.parse(mTestUrl));
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mOnTop) {
            super.onConfigurationChanged(newConfig);
            if (mLandscapeMode) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            return;
        }
        super.onConfigurationChanged(newConfig);
        mLandscapeMode = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

    }

    private void updateSoftInputModeSetting() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void cleanupAndFinish() {
        /*
        if (mPlayer != null) {

            mSurfaceview.getHolder().removeCallback(mSufaceCallback);
            mSurfaceview = null;
            mPlayer.setDisplay(null);
            ((MediaPlayerControl) (mediaPlayerControl)).stop();
            mPlayer.release();
            mPlayer.setOnCompletionListener(null);
        }

        if (mVideoControllerView != null) {
            mVideoControllerView.setAnchorView(null);
            mVideoControllerView.hideImmediate();
            mVideoControllerView = null;
        }
        mSoundSeekBar = null;
        mGestureDetector = null;
        */
    }

    @OnClick(R.id.always_on_top)
    void setAlwaysOntop() {
        mOnTop = true;
        mOntopView.setVisibility(View.VISIBLE);
        mNotOntopView.setVisibility(View.GONE);
    }

    @OnClick(R.id.not_always_on_top)
    void setNotOntop() {
        mOntopView.setVisibility(View.GONE);
        mNotOntopView.setVisibility(View.VISIBLE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    @BindView(R.id.always_on_top)
    View mOntopView;

    @BindView(R.id.not_always_on_top)
    View mNotOntopView;

    private void pause() {
        /*
        mVideoControllerView.pause();
        */
    }

    @OnClick(R.id.main_videoview_contianer)
    void setVideoControlVisibility() {
        /*
        if (mVideoControllerView == null) {
            return;
        }
        if (mVideoControllerView.getVisibility() == View.VISIBLE) {
            mSoundView.setVisibility(View.GONE);
            mVideoControllerView.hide();
        } else {
            if (mSoundView.getVisibility() != View.VISIBLE) {
                mSoundView.setVisibility(View.VISIBLE);
            }
            mVideoControllerView.show();
            mSoundView.setVisibility(View.VISIBLE);
        }
        */
    }


}
