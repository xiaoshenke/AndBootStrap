package wuxian.me.andbootstrap.video;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.IOException;

import wuxian.me.andbootstrap.R;

/**
 * Created by wuxian on 17/3/2017.
 *
 * 协调@MediaPlayer,SoundView,VideoControllerView
 */

public class VideoView implements IVolumListener {
    private int SYSTEM_MAX_SOUND = 0;

    private MediaPlayer mPlayer;
    private AudioManager mAudioManager;

    private BaseControllerView mVideoControllerView;
    private GestureDetector mGestureDetector;

    private View mView;
    private Context mContext;

    private boolean mPlayError = false;

    public VideoView(@NonNull Context context, @Nullable BaseControllerView controllerView) {
        this.mContext = context;
        this.mControllerView = controllerView;

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        SYSTEM_MAX_SOUND = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);

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
                if (mVideoControllerView != null && mVideoControllerView.getView().getVisibility() == View.VISIBLE) {
                    //Todo
                    //mVideoControllerView.updatePauseView();
                }
                if (mPlayError) {
                    return;
                }
                return;
            }
        });
    }

    public VideoView(@NonNull Context context) {
        this(context, null);
    }

    public Uri mSourceUri;

    private MediaPlayer.OnPreparedListener mPlayerPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mPlayError = false;
            //Todo
            /*
            if (mVideoProgressbar.getVisibility() == View.VISIBLE) {
                mVideoProgressbar.setVisibility(View.GONE);
            }
            */
            mPlayer.start();
            mVideoControllerView.show();
        }
    };

    //Todo
    public void destroy() {
        ;
    }

    //Todo
    public void pausePlay() {
        ;
    }

    //Todo
    public void start() {
        ;
    }

    //Todo
    public int getDuration() {
        return -1;
    }

    //Todo
    public int getBufferPercentage() {
        return -1;
    }

    //Todo
    public boolean canPause() {
        return true;
    }

    //Todo
    public int getCurrentPosition() throws IllegalStateException {
        return -1;
    }

    //Todo
    public void seekTo(int position) {

    }

    //Todo
    public void startPlay(@NonNull Uri uri) {
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(mContext, uri);
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

    //Todo
    public boolean isPlaying() {
        return true;
    }

    private boolean mLandscapeMode = false;

    public boolean isLandscape() {
        return mLandscapeMode;
    }

    public void setLandscapeMode(boolean horizonMode) {
        mLandscapeMode = horizonMode;
    }

    private BaseControllerView mControllerView;

    public void setControllerView(@NonNull BaseControllerView controllerView) {
        mControllerView = controllerView;
    }

    public View getView() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_video, null, false);
        initView(v);
        mView = v;
        return mView;
    }

    private void updateSurfaceView(boolean landscape) {
        FrameLayout.LayoutParams fllp;
        if (mLandscapeMode) {
            fllp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            int width = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
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


    private void updateOtherView() {
        updateVideoContainerView();
        updateSurfaceView(mLandscapeMode);
        updateSoundView(mLandscapeMode);
    }

    private void updateSoundView(boolean landscape) {
        FrameLayout.LayoutParams fllp;
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int height;
        if (!landscape) {
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

    private SurfaceView mSurfaceview;
    private SurfaceHolder.Callback mSufaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mPlayer.setDisplay(holder);
            mSurfaceview.setZOrderOnTop(false);
            //Todo
            /*
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
            */
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int i, int i2, int i3) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };

    private ViewGroup mVideoContainer;
    private IMediaPlayer mediaPlayerControl = new MediaPlayerControl();
    private View mSoundView;
    VerticalSeekBar mSoundSeekBar;

    private void initView(View view) {
        mSoundSeekBar = (VerticalSeekBar) view.findViewById(R.id.video_seekbar);
        mSurfaceview = (SurfaceView) view.findViewById(R.id.video);
        mSurfaceview.getHolder().addCallback(mSufaceCallback);

        mSoundView = view.findViewById(R.id.sound_control);

        VolumGestureListener listener = new VolumGestureListener(mContext, this);
        listener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo
                //setVideoControlVisibility();
            }
        });
        mGestureDetector = new GestureDetector(mContext, listener);

        if (mVideoControllerView == null) {
            mVideoControllerView = new DefaultControllerView(mContext, this);
        }

        mVideoContainer = (ViewGroup) view.findViewById(R.id.main_videoview_contianer);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
        mVideoContainer.addView(mVideoControllerView.getView(), lp);
        mVideoControllerView.getView().setVisibility(View.GONE);  //刚开始不可见 --> fixme

        mVideoContainer.setOnClickListener(null);
        mVideoContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }


    private OnVideoPlay onVideoPlay;

    public void setOnVideoPlay(OnVideoPlay onVideoPlay) {
        this.onVideoPlay = onVideoPlay;
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

    public interface OnVideoPlay {
        void onPlayStart();

        void onPlayEnd();

        void onPlayError();
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
            //Todo:
            /*
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
            */
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
