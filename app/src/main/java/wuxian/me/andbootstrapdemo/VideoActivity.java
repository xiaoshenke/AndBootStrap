package wuxian.me.andbootstrapdemo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import wuxian.me.andbootstrap.BaseActivity;
import wuxian.me.andbootstrapdemo.utils.view.VerticalSeekBar;
import wuxian.me.andbootstrapdemo.utils.view.VideoControllerView;

/**
 * Created by wuxian on 16/3/2017.
 */

public class VideoActivity extends BaseActivity implements View.OnClickListener {

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
    private int mMaxSound = 0;
    private boolean mPlayError = false;
    private VideoControllerView mVideoControllerView;
    private Uri mVideoUri = null;
    private boolean mPlayingWhenPause = true;
    private boolean mLandscapeMode = false;
    private GestureDetector mGestureDetector;

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
        setContentView(inflate(R.layout.activity_video));
        ButterKnife.bind(this);
        initView();
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
                if (mVideoControllerView.getVisibility() == View.VISIBLE) {
                    mVideoControllerView.updatePausePlay();
                }
                if (mPlayError) {
                    return;
                }
                return;
            }
        });

        mVideoControllerView = new VideoControllerView(this) {
            @Override
            protected int layoutId() {
                return R.layout.fullscreen_media_controller;
            }

            protected void initSpecialControl() {
                View root = getControlView();
                final ImageButton live_comment = (ImageButton) root.findViewById(R.id.live_comment);
                live_comment.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
                ImageButton send_comment = (ImageButton) root.findViewById(R.id.btnComment);
                send_comment.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mVideoControllerView.hide();
                    }
                });
            }
        };
        mVideoControllerView.setAnchorView(mVideoContainer);
        mVideoControllerView.setMediaPlayer(mediaPlayerControl);
        mVideoControllerView.setVisibility(View.GONE);
        updatePlayerSeekEnable();

        mSurfaceview.getHolder().addCallback(mSufaceCallback);

        initZoominView();

        updateVideoContainerView();
        updateSurfaceView();

        updateSoundView();

        playVideo();
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

    private boolean ziIsAlwaysOnTop = false;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        if (ziIsAlwaysOnTop) {
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

        updateVideoContainerView();
        updateSurfaceView();
        updateSoundView();
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
        mVideoContainer.setOnClickListener(null);
        mVideoContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return mGestureDetector.onTouchEvent(event);
            }
        });

        RelativeLayout.LayoutParams videoRllp = new RelativeLayout.LayoutParams(0, 0);
        videoRllp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        videoRllp.addRule(RelativeLayout.CENTER_VERTICAL, 1);
        mVideoContainer.setLayoutParams(videoRllp);
    }

    private void initZoominView() {
        mAudioManager = (AudioManager) getSystemService(this.AUDIO_SERVICE);
        mMaxSound = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);

        mSoundSeekBar.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(VerticalSeekBar seekBar) {
                updateVolumn();
            }

            @Override
            public void onStopTrackingTouch(VerticalSeekBar seekBar) {
                //updateVolumn();
            }
        });

        mGestureDetector = new GestureDetector(this, new MyGestureListener());
    }

    private void updateSoftInputModeSetting() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private int dp2px(int dp) {
        return (int) (getApplicationContext().getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        // 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
        public boolean onScroll(MotionEvent motion1, MotionEvent motion2, float distanceX, float distanceY) {

            float oldX = motion1.getX();
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            if (oldX > width * 2.0 / 3) {     //右边滑动
                ;
            } else if (oldX < width / 3.0) { //左边滑动 设置声音
                int pHeight = mSoundSeekBar.getMeasuredHeight();
                int max = mSoundSeekBar.getMax();
                int pro = mSoundSeekBar.getProgress();
                pro += distanceY * max / pHeight;
                pro = pro < max ? pro : max;
                pro = pro >= 0 ? pro : 0;
                mSoundSeekBar.setProgress(pro);
                updateVolumn();
            }

            return true;
        }

        public boolean onDown(MotionEvent e) {
            return true;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            setVideoControlVisibility();
            return true;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return true;
        }
    }

    private void updateVolumn() {

        if (mSoundSeekBar == null) {
            return;
        }
        int progress = mSoundSeekBar.getProgress();
        int max = mSoundSeekBar.getMax();
        int sound = progress * mMaxSound / max;
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sound, AudioManager.FLAG_PLAY_SOUND);
    }

    //Todo:
    private void updatePlayerSeekEnable() {
        /*
        if(UGirlApplication.getSession().isLogined()){
            mVideoControllerView.enableSeek(true);
            zoVideoController.enableSeek(true);
        }else{
            mVideoControllerView.enableSeek(false);
            zoVideoController.enableSeek(false);
        }
        */
    }

    private VideoControllerView.MediaPlayerControl mediaPlayerControl = new MyMediaPlayerControl();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_videoview_contianer:
                setVideoControlVisibility();
                break;
            case R.id.back:
                mediaPlayerControl.toggleFullScreen();
                break;
            case R.id.always_on_top:
                setLockState();
                break;
            case R.id.not_always_on_top:
                setLockState();
                break;
        }

    }

    private void cleanupAndFinish() {
        if (mPlayer != null) {

            mSurfaceview.getHolder().removeCallback(mSufaceCallback);
            mSurfaceview = null;
            mPlayer.setDisplay(null);
            ((MyMediaPlayerControl) (mediaPlayerControl)).stop();
            mPlayer.release();
            mPlayer.setOnCompletionListener(null);
        }

        if (mVideoControllerView != null) {
            mVideoControllerView.setAnchorView(null);
            mVideoControllerView.hide(-1);
            mVideoControllerView = null;
        }
        mSoundSeekBar = null;
        mGestureDetector = null;
    }

    private void setLockState() {
        ziIsAlwaysOnTop = !ziIsAlwaysOnTop;
        if (ziIsAlwaysOnTop) {
            findViewById(R.id.always_on_top).setVisibility(View.VISIBLE);
            findViewById(R.id.not_always_on_top).setVisibility(View.GONE);
        } else {
            findViewById(R.id.always_on_top).setVisibility(View.GONE);
            findViewById(R.id.not_always_on_top).setVisibility(View.VISIBLE);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
    }

    private SurfaceHolder.Callback mSufaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mPlayer.setDisplay(holder);
            //holder.setFormat(PixelFormat.OPAQUE);
            mSurfaceview.setZOrderOnTop(false);

            if (mVideoUri == null) {
                //requestFreeVideoData(); //Fixme
                return;
            } else {
                if (mPlayingWhenPause) {
                    mPlayingWhenPause = false;

                    if (mediaPlayerControl.isPlaying() == false) {
                        doPauseResume();
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

    private void doPauseResume() {
        mVideoControllerView.doPauseResume();
    }

    private class MyMediaPlayerControl implements VideoControllerView.MediaPlayerControl {
        public void stop() {
            try {
                mPlayer.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void toggleFullScreen() {
             /*
                if(!ziIsAlwaysOnTop){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                    mLandscapeMode=getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE;
                }else{
                    if(mLandscapeMode){
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }else{
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                }
                */
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
                boolean b = mPlayer.isPlaying();
                return b;
            } catch (IllegalStateException e) {
                return false;
            }
        }

        @Override
        public boolean isFullScreen() {
            return true;
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

    private void setVideoControlVisibility() {
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

            mVideoControllerView.show(-1);
            mSoundView.setVisibility(View.VISIBLE);
        }
    }

}
