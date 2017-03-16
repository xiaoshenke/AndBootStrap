package wuxian.me.andbootstrapdemo.video;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wuxian.me.andbootstrap.BaseActivity;
import wuxian.me.andbootstrapdemo.R;
import wuxian.me.andbootstrapdemo.utils.view.VerticalSeekBar;

/**
 * Created by wuxian on 16/3/2017.
 * <p>
 * Todo: 把这个封装到一个view里面 便于移植
 */

public class VideoActivity extends BaseActivity implements IVolumListener {

    private int SYSTEM_MAX_SOUND = 0;

    @BindView(R.id.video_seekbar)
    VerticalSeekBar mSoundSeekBar;

    @BindView(R.id.video)
    SurfaceView mSurfaceview;

    @BindView(R.id.product_progress)
    ProgressBar mVideoProgressbar;

    @BindView(R.id.main_videoview_contianer)
    ViewGroup mVideoContainer;

    @BindView(R.id.sound_control)
    View mSoundView;

    private MediaPlayer mPlayer;
    private AudioManager mAudioManager;

    private VideoControllerView mVideoControllerView;
    private GestureDetector mGestureDetector;

    private boolean mOnTop = false;
    private boolean mPlayError = false;
    private Uri mVideoUri = null;
    private boolean mPlayingWhenPause = true;
    private boolean mLandscapeMode = false;

    private MediaPlayer.OnPreparedListener mPlayerPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mPlayError = false;
            if (mVideoProgressbar.getVisibility() == View.VISIBLE) {
                mVideoProgressbar.setVisibility(View.GONE);
            }
            mPlayer.start();
            mVideoControllerView.show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAudioManager = (AudioManager) getSystemService(this.AUDIO_SERVICE);
        SYSTEM_MAX_SOUND = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);

        View v = inflate(R.layout.activity_video);
        setContentView(v);
        ButterKnife.bind(this);

        mPlayer = new MediaPlayer();
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                mediaPlayer.reset();
                if (what != MediaPlayer.MEDIA_ERROR_UNKNOWN) {
                    //Todo
                }
                mPlayError = true;
                return false;
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mVideoControllerView != null && mVideoControllerView.getVisibility() == View.VISIBLE) {
                    mVideoControllerView.updatePauseView();
                }
                if (mPlayError) {
                    return;
                }
                return;
            }
        });

        initView();
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                playVideo();    //Todo:
            }
        }, 2000);
    }

    private void playVideo() {
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(this, mVideoUri);
            mPlayer.setOnPreparedListener(mPlayerPreparedListener);
            mPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mSurfaceview.getHolder().addCallback(mSufaceCallback);

        mGestureDetector = new GestureDetector(this, new VolumGestureListener(this, this));

        mVideoControllerView = new VideoControllerView(this);
        mVideoControllerView.setAnchorView(mVideoContainer);
        mVideoControllerView.setMediaPlayer(mediaPlayerControl);
        mVideoControllerView.setVisibility(View.GONE);

        mVideoContainer.setOnClickListener(null);
        mVideoContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });

        updateOtherView();
    }

    private void updateOtherView() {
        updateVideoContainerView();
        updateSurfaceView();
        updateSoundView();
    }

    private void updateSoundView() {
        FrameLayout.LayoutParams fllp;
        Display display = getWindowManager().getDefaultDisplay();
        int height;
        if (!mLandscapeMode) {
            height = display.getWidth() * 9 * 4 / (16 * 5);  //16比9视频高度的4/5
            //height=display.getHeight()-dp2px(65+65+15);
            height = height > 480 ? 480 : height;
            fllp = new FrameLayout.LayoutParams(100, height);  //should calculate
        } else {
            height = display.getHeight() - (65 + 65 + 15) * 2;  //Fixme: dp2px
            height = height > 540 ? 540 : height;
            fllp = new FrameLayout.LayoutParams(100, height);
        }
        fllp.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        mSoundView.setLayoutParams(fllp);
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

        updateOtherView();
    }

    private void updateSurfaceView() {
        FrameLayout.LayoutParams fllp;
        if (mLandscapeMode) {
            fllp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            int width = getWindowManager().getDefaultDisplay().getWidth();
            fllp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width * 10 / 16);
        }
        fllp.gravity = Gravity.CENTER;
        mSurfaceview.setLayoutParams(fllp);
    }

    private void updateVideoContainerView() {
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rllp.addRule(RelativeLayout.CENTER_VERTICAL, 1);
        mVideoContainer.setLayoutParams(rllp);
    }

    private void updateSoftInputModeSetting() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void updateVolum(float percent) {
        mSoundSeekBar.setProgress((int) (percent * mSoundSeekBar.getMax()));
        int sound = (int) (SYSTEM_MAX_SOUND * percent);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sound, AudioManager.FLAG_PLAY_SOUND);
    }

    @Override
    public void updateVolumBy(int distanceX, int distanceY) {
        int height = mSoundSeekBar.getMeasuredHeight();
        int max = mSoundSeekBar.getMax();
        int pro = mSoundSeekBar.getProgress();
        pro += distanceY * max / height;
        pro = pro < max ? pro : max;
        pro = pro >= 0 ? pro : 0;
        updateVolum((float) pro / max);
    }

    private IMediaPlayer mediaPlayerControl = new MediaPlayerControl();

    @OnClick(R.id.back)
    void toggle() {
        mediaPlayerControl.toggleFullScreen();
    }

    private void cleanupAndFinish() {
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

    private SurfaceHolder.Callback mSufaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mPlayer.setDisplay(holder);
            mSurfaceview.setZOrderOnTop(false);
            if (mVideoUri == null) {
                //requestFreeVideoData(); //Todo: 找一个可用的uri
                return;
            } else {
                if (mPlayingWhenPause) {
                    mPlayingWhenPause = false;
                    if (mediaPlayerControl.isPlaying() == false) {
                        pause();
                    }
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int i, int i2, int i3) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };

    private void pause() {
        mVideoControllerView.pause();
    }

    @OnClick(R.id.main_videoview_contianer)
    void setVideoControlVisibility() {
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
    }

    private class MediaPlayerControl implements IMediaPlayer {
        public void stop() {
            try {
                mPlayer.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void toggleFullScreen() {
            if (!mOnTop) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                mLandscapeMode = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            } else {
                if (mLandscapeMode) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
            setVideoControlVisibility();
        }

        @Override
        public void start() {
            try {
                mPlayer.start();
            } catch (IllegalStateException e) {
                return;
            }
        }

        @Override
        public void seekTo(int pos) {
            try {
                mPlayer.seekTo(pos);
            } catch (IllegalStateException e) {
                return;
            }
        }

        @Override
        public void pause() {
            try {
                mPlayer.pause();
            } catch (IllegalStateException e) {
                return;
            }
        }

        @Override
        public boolean isPlaying() {
            try {
                return mPlayer.isPlaying();
            } catch (IllegalStateException e) {
                return false;
            }
        }

        @Override
        public int getDuration() {
            try {
                return mPlayer.getDuration();
            } catch (IllegalStateException e) {
                return 0;
            }
        }

        @Override
        public int getCurrentPosition() {
            if (mPlayer == null) {
                return 0;
            }
            try {
                return mPlayer.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return 0;
            }
        }

        @Override
        public boolean isFullScreen() {
            return true;
        }

        @Override
        public int getBufferPercentage() {
            return 0;
        }

        @Override
        public boolean canSeekForward() {
            return true;
        }

        @Override
        public boolean canSeekBackward() {
            return true;
        }

        @Override
        public boolean canPause() {
            return true;
        }
    }
}
