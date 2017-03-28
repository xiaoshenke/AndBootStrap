package wuxian.me.andbootstrap.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

/**
 * Created by wuxian on 17/3/2017.
 * <p>
 * 封装了一个controller拉取进度条功能
 */

public abstract class BaseControllerView implements VideoView.OnVideoPlay {

    protected Context mContext;
    protected IPlayer mPlayer;

    protected Context context() {
        return mContext;
    }

    public IPlayer player() {
        return mPlayer;
    }

    public BaseControllerView(@NonNull Context context, @NonNull IPlayer player) {
        mContext = context;
        mPlayer = player;
    }

    protected int updateProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        try {
            int position = mPlayer.getCurrentPosition();
            int duration = mPlayer.getDuration();
            if (duration > 0) {
                long pos = 1000L * position / duration;
                getProgressbar().setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            getProgressbar().setSecondaryProgress(percent * 10);
            return position;
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    @NonNull
    protected abstract ProgressBar getProgressbar();

    private boolean mDragging = false;
    protected boolean isDragging() {
        return mDragging;
    }

    protected abstract void onStartDragProgressbar();
    protected abstract void onProgressChanged(int progress);
    protected abstract void onStopDragProgressbar();

    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            show();
            mDragging = true;
            onStartDragProgressbar();
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
            long newposition = pos / getProgressbar().getMax();
            mPlayer.start();
            mPlayer.seekTo((int) newposition);
            BaseControllerView.this.onProgressChanged(progress);
        }

        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            show();
            onStopDragProgressbar();
        }
    };

    private View addControlFunctionToView(View view) {
        ((SeekBar) getProgressbar()).setOnSeekBarChangeListener(mSeekListener);
        return view;
    }


    @NonNull
    protected abstract View initView();
    private View mView;

    @NonNull
    public final View getView() {
        if (mView != null) {
            return mView;
        }
        mView = addControlFunctionToView(initView());
        return mView;
    }

    protected abstract boolean showing();

    public abstract void show();

    public abstract void hide();

    public abstract void hideImmediate();
}
