package wuxian.me.andbootstrap.video;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;

import wuxian.me.andbootstrap.R;

import static android.view.View.GONE;

/**
 * Created by wuxian on 17/3/2017.
 */

public class DefaultControllerView extends BaseControllerView {

    private static final int DEFAULT_CONTROLLER_SHOWTIME = 3000; //controller的消失时间

    private Handler mHandler = new VideoProgressHandler(this);
    public DefaultControllerView(@NonNull Context context, @NonNull VideoView player) {
        super(context, player);

        formatBuilder = new StringBuilder();
        mFormatter = new Formatter(formatBuilder, Locale.getDefault());
    }

    private boolean mShowing = false;

    @Override
    protected boolean showing() {
        return mShowing;
    }

    @Override
    protected void onStartDragProgressbar() {
        mHandler.removeMessages(VideoProgressHandler.MESSGAE_SHOW_PROGRESS);
    }

    @Override
    protected void onProgressChanged(int progress) {  //progress范围[0,bar.getMax()];
        if (mCurrentTime != null) {
            long duration = mPlayer.getDuration();
            long pos = duration * progress;
            long newposition = pos / getProgressbar().getMax();

            mCurrentTime.setText(format((int) newposition));
        }
    }

    @Override
    protected void onStopDragProgressbar() {
        updateProgress();
        updatePauseView();
        mHandler.sendEmptyMessage(VideoProgressHandler.MESSGAE_SHOW_PROGRESS);
    }

    private String format(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        formatBuilder.setLength(0);
        return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

    private Formatter mFormatter;
    StringBuilder formatBuilder;

    @Override
    protected int updateProgress() {
        int ret = super.updateProgress();
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        mEndTime.setText(format(duration));
        mCurrentTime.setText(format(position));

        return ret;
    }

    @NonNull
    @Override
    protected ProgressBar getProgressbar() {
        return mProgress;
    }

    @NonNull
    @Override
    protected View initView() {
        LayoutInflater inflate = (LayoutInflater) context().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.fullscreen_media_controller, null, false);
        mView = view;
        initView(view);
        return view;
    }

    private View mView;

    private ProgressBar mProgress;
    private TextView mEndTime;
    private TextView mCurrentTime;
    private ImageButton mPauseButton;
    private ImageButton mFullscreenButton;

    private void initView(View view) {
        mPauseButton = (ImageButton) view.findViewById(R.id.pause);
        mPauseButton.requestFocus();
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pause();
            }
        });

        mFullscreenButton = (ImageButton) view.findViewById(R.id.fullscreen);
        mFullscreenButton.requestFocus();
        mFullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toggleFullscreen();
            }
        });

        mProgress = (SeekBar) view.findViewById(R.id.mediacontroller_progress);
        mProgress.setMax(1000);

        mEndTime = (TextView) view.findViewById(R.id.time);
        mCurrentTime = (TextView) view.findViewById(R.id.time_current);

        updatePauseView();
        updateFullscreenView();
        updateUnsupportedButtons();
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

    /**
     * Stream不支持的时候 不能进行停止或快进
     */
    private void updateUnsupportedButtons() {
        try {
            if (mPauseButton != null && !mPlayer.canPause()) {
                mPauseButton.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {
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

    @Override
    public void show() {
        boolean origin = mShowing;
        if (!mShowing) {
            updateProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            updateUnsupportedButtons();
        }
        mShowing = true;
        updatePauseView();
        updateFullscreenView();

        mHandler.sendEmptyMessage(VideoProgressHandler.MESSGAE_SHOW_PROGRESS);

        if (!origin) {
            Animation bottom_in = AnimationUtils.loadAnimation(context(), R.anim.slide_in_from_bottom);
            mView.startAnimation(bottom_in);
            mView.setVisibility(View.VISIBLE);
        }

        Message msg = mHandler.obtainMessage(VideoProgressHandler.MESSAGE_FADE_OUT);
        mHandler.removeMessages(VideoProgressHandler.MESSAGE_FADE_OUT);
        mHandler.sendMessageDelayed(msg, DEFAULT_CONTROLLER_SHOWTIME);
    }

    @Override
    public void hide() {

        mView.clearAnimation();
        mShowing = false;
        try {
            Animation bottom_out = AnimationUtils.loadAnimation(context(), R.anim.slide_out_to_bottom);
            mView.startAnimation(bottom_out);
            bottom_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mView.setVisibility(GONE);
                    mHandler.removeMessages(VideoProgressHandler.MESSGAE_SHOW_PROGRESS);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

        } catch (IllegalArgumentException ex) {
        }
        mShowing = false;

    }

    @Override
    public void hideImmediate() {

        mHandler.removeMessages(VideoProgressHandler.MESSGAE_SHOW_PROGRESS);
        mView.clearAnimation();
        mView.setVisibility(GONE);
        mShowing = false;
        return;
    }

    public static class VideoProgressHandler extends Handler {
        public static final int MESSAGE_FADE_OUT = 101;
        public static final int MESSGAE_SHOW_PROGRESS = 102;

        private final WeakReference<BaseControllerView> mControllerView;

        VideoProgressHandler(BaseControllerView view) {
            mControllerView = new WeakReference<BaseControllerView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseControllerView view = mControllerView.get();
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
                    if (!view.isDragging() && view.showing() && view.player().isPlaying()) {
                        removeMessages(MESSGAE_SHOW_PROGRESS);
                        msg = obtainMessage(MESSGAE_SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    }
}
