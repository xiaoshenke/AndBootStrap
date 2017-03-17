package wuxian.me.andbootstrap.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

/**
 * Created by wuxian on 17/3/2017.
 * <p>
 * 封装了一个controller应该有的一些功能
 * 最基础的和VideoView交互的就是 拉取进度条,暂停
 */

public abstract class BaseControllerView {

    protected Context mContext;
    private VideoView mPlayer;

    protected
    @NonNull
    abstract ProgressBar getProgressbar();

    public BaseControllerView(@NonNull Context context, @NonNull VideoView player) {
        mContext = context;
        mPlayer = player;

        addControlFunctionToView(initView());
    }

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
            //mHandler.removeMessages(VideoProgressHandler.MESSGAE_SHOW_PROGRESS);
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

            BaseControllerView.this.onProgressChanged(progress);
            /*
            if (mCurrentTime != null)
                mCurrentTime.setText(format((int) newposition));
                */
        }

        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            show();

            onStopDragProgressbar();
            /*
            updateProgress();
            updatePauseView();
            mHandler.sendEmptyMessage(VideoProgressHandler.MESSGAE_SHOW_PROGRESS);
            */
        }
    };

    private void addControlFunctionToView(View view) {
        ((SeekBar) getProgressbar()).setOnSeekBarChangeListener(mSeekListener);
    }

    protected
    @NonNull
    abstract View initView();

    //init view
    public abstract
    @NonNull
    View getView();

    public abstract void show();

    public abstract void hide();
}
