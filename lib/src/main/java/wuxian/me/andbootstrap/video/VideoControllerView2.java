/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wuxian.me.andbootstrap.video;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.util.Formatter;
import java.util.Locale;

import wuxian.me.andbootstrap.R;


@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VideoControllerView2 extends FrameLayout {
    private static final String TAG = "VideoControllerView";
    private static final int DEFAULT_CONTROLLER_SHOWTIME = 3000; //controller的消失时间
    private ViewGroup mAnchorView;
    private VideoView mPlayer;

    private ProgressBar mProgress;
    private TextView mEndTime;
    private TextView mCurrentTime;
    private ImageButton mPauseButton;
    private ImageButton mFullscreenButton;

    private Handler mHandler = null;//new VideoProgressHandler(this);

    private boolean mDragging = false;
    private boolean mShowing = false;
    private Formatter mFormatter;
    StringBuilder formatBuilder;

    public VideoView player() {
        return mPlayer;
    }

    public boolean showing() {
        return mShowing;
    }

    public boolean dragging() {
        return mDragging;
    }

    public VideoControllerView2(Context context) {
        super(context);

        formatBuilder = new StringBuilder();

        mFormatter = new Formatter(formatBuilder, Locale.getDefault());
    }

    public void setAnchorView(@NonNull ViewGroup view) {
        mAnchorView = view;
        removeAllViews();

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
        addView(makeControllerView(), lp);
    }

    @NonNull
    private View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.fullscreen_media_controller, null, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mPauseButton = (ImageButton) view.findViewById(R.id.pause);
        mPauseButton.requestFocus();
        mPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });

        mFullscreenButton = (ImageButton) view.findViewById(R.id.fullscreen);
        mFullscreenButton.requestFocus();
        mFullscreenButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFullscreen();
            }
        });

        mProgress = (SeekBar) view.findViewById(R.id.mediacontroller_progress);
        if (mProgress instanceof SeekBar) {
            SeekBar seeker = (SeekBar) mProgress;
            seeker.setOnSeekBarChangeListener(mSeekListener);
        }
        mProgress.setMax(1000);

        mEndTime = (TextView) view.findViewById(R.id.time);
        mCurrentTime = (TextView) view.findViewById(R.id.time_current);
    }

    public void setMediaPlayer(@NonNull VideoView player) {
        mPlayer = player;

        updatePauseView();
        updateFullscreenView();
    }

    /**
     * Stream不支持的时候 不能进行停止或快进
     */
    private void updateUnsupportedButtons() {
        if (mPlayer == null) {
            return;
        }
        try {
            if (mPauseButton != null && !mPlayer.canPause()) {
                mPauseButton.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {
        }
    }

    public void show() {
        boolean origin = mShowing;
        if (!mShowing && mAnchorView != null) {
            updateProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            updateUnsupportedButtons();

            if (this.getParent() == null) { //anim out的时候,把这个view卸载了,这里重新装上
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
                mAnchorView.addView(this, lp);
            }
        }
        mShowing = true;
        updatePauseView();
        updateFullscreenView();

        mHandler.sendEmptyMessage(VideoProgressHandler.MESSGAE_SHOW_PROGRESS);
        this.setVisibility(VISIBLE);

        if (!origin) {
            Animation bottom_in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_from_bottom);
            this.startAnimation(bottom_in);
            this.setVisibility(VISIBLE);
        }

        Message msg = mHandler.obtainMessage(VideoProgressHandler.MESSAGE_FADE_OUT);
        mHandler.removeMessages(VideoProgressHandler.MESSAGE_FADE_OUT);
        mHandler.sendMessageDelayed(msg, DEFAULT_CONTROLLER_SHOWTIME);
    }

    public boolean isShowing() {
        return mShowing;
    }

    public void hideImmediate() {
        if (mAnchorView == null) {
            return;
        }
        mAnchorView.removeView(this);
        mHandler.removeMessages(VideoProgressHandler.MESSGAE_SHOW_PROGRESS);
        this.clearAnimation();
        this.setVisibility(GONE);
        mShowing = false;
        return;
    }

    public void hide() {
        if (mAnchorView == null) {
            return;
        }
        this.clearAnimation();
        this.setVisibility(GONE);
        mShowing = false;

        try {
            Animation bottom_out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_to_bottom);
            this.startAnimation(bottom_out);
            final View controlbar = this;
            bottom_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mAnchorView.removeView(controlbar);
                    mHandler.removeMessages(VideoProgressHandler.MESSGAE_SHOW_PROGRESS);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mAnchorView.removeView(this);
            //setVisibility(GONE);    //Fixme:

        } catch (IllegalArgumentException ex) {
        }
        mShowing = false;
    }

    private String format(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        formatBuilder.setLength(0);
        return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

    public int updateProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        try {
            int position = mPlayer.getCurrentPosition();
            int duration = mPlayer.getDuration();
            if (duration > 0) {
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
            mEndTime.setText(format(duration));
            mCurrentTime.setText(format(position));

            return position;
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    public void updatePauseView() {
        if (mPauseButton == null || mPlayer == null) {
            return;
        }
        if (mPlayer.isPlaying()) {
            mPauseButton.setImageResource(R.mipmap.icon_video_pause);
        } else {
            mPauseButton.setImageResource(R.mipmap.icon_video_start);
        }
    }

    public void updateFullscreenView() {
        if (mFullscreenButton == null || mPlayer == null) {
            return;
        }
        if (mPlayer.isLandscape()) {
            mFullscreenButton.setImageResource(R.mipmap.icon_video_zoom_out);
        } else {
            mFullscreenButton.setImageResource(R.mipmap.icon_video_zoom_in);
        }
    }

    public void pause() {
        if (mPlayer == null) {
            return;
        }
        if (mPlayer.isPlaying()) {
            mPlayer.pausePlay();
            mHandler.removeMessages(VideoProgressHandler.MESSAGE_FADE_OUT);  //暂停状态下不自动隐藏
        } else {
            mPlayer.start();
            show();
        }
        updatePauseView();
    }

    //Todo
    private void toggleFullscreen() {
        /*
        if (mPlayer == null) {
            return;
        }
        mPlayer.toggleFullScreen();
        */
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show();
        super.onTrackballEvent(ev);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mPlayer == null) {
            return true;
        }
        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                pause();
                show();
                if (mPauseButton != null) {
                    mPauseButton.requestFocus();
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (uniqueDown && !mPlayer.isPlaying()) {
                mPlayer.start();
                updatePauseView();
                show();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (uniqueDown && mPlayer.isPlaying()) {
                mPlayer.pausePlay();
                updatePauseView();
                show();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
            return super.dispatchKeyEvent(event);
        }

        show();
        return super.dispatchKeyEvent(event);
    }

    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            show();

            mDragging = true;
            mHandler.removeMessages(VideoProgressHandler.MESSGAE_SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
            if (mPlayer == null) {
                return;
            }
            if (!fromUser) {
                return;
            }

            long duration = mPlayer.getDuration();
            long pos = duration * progress;
            long newposition = pos / 1000;
            mPlayer.start();
            mPlayer.seekTo((int) newposition);
            if (mCurrentTime != null)
                mCurrentTime.setText(format((int) newposition));
        }

        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            updateProgress();
            updatePauseView();
            show();
            mHandler.sendEmptyMessage(VideoProgressHandler.MESSGAE_SHOW_PROGRESS);
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        updateUnsupportedButtons();
        super.setEnabled(enabled);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(VideoControllerView2.class.getName());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(VideoControllerView2.class.getName());
    }
}