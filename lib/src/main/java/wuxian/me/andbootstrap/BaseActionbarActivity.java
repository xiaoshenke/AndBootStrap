package wuxian.me.andbootstrap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toolbar;

/**
 * Created by wuxian on 4/3/2017.
 */

public abstract class BaseActionbarActivity extends BaseActivity {
    //真正实践toolbar的时候 需要再custom一遍toolbar 比如说toolbar的文字是不是居中的....
    protected Toolbar mToolbar;
    private View mRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRoot = LayoutInflater.from(this).inflate(R.layout.activity_base_actionbar, null);
        setContentView(mRoot);
        mToolbar = (Toolbar) mRoot.findViewById(R.id.tool_bar);
        FrameLayout container = (FrameLayout) mRoot.findViewById(R.id.container);
        container.addView(getSubview());

        if (useCustomToolbar()) {
            customToolbar(mToolbar);
        }

        return;
    }

    //子类的view应该在这里控制
    protected abstract View getSubview();

    protected abstract boolean useCustomToolbar();

    protected void customToolbar(Toolbar toolbar) {
    }
}
