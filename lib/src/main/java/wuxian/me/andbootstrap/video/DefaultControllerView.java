package wuxian.me.andbootstrap.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by wuxian on 17/3/2017.
 * Todo: 把VideoControllerView2的代码移植过来
 */

public class DefaultControllerView extends BaseControllerView {

    public DefaultControllerView(@NonNull Context context, @NonNull VideoView player) {
        super(context, player);
    }

    @Override
    protected void onStartDragProgressbar() {

    }

    @Override
    protected void onProgressChanged(int progress) {

    }

    @Override
    protected void onStopDragProgressbar() {

    }

    @NonNull
    @Override
    protected ProgressBar getProgressbar() {
        return null;
    }

    @NonNull
    @Override
    protected View initView() {
        return null;
    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }
}
