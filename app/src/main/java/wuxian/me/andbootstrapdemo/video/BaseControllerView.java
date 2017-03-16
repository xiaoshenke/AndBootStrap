package wuxian.me.andbootstrapdemo.video;

import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by wuxian on 17/3/2017.
 * <p>
 * Todo
 * 封装了一个controller应该有的一些功能
 * 最基础的和VideoView交互的就是 拉取进度条,暂停
 */

public abstract class BaseControllerView {

    private ProgressBar mProgressBar;
    private IMediaPlayer mPlayer;

    public abstract View getView();

    public abstract void show();

    public abstract void hide();
}
