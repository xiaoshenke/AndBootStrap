package wuxian.me.andbootstrapdemo.utils.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class SolveNestVpViewPager extends ViewPager {
    //当父viewpager嵌套了一个子viewpager的时候 使用这个viewpager作为父viewpager 能够提升滑动体验


    public SolveNestVpViewPager(Context context) {
        super(context);
    }

    public SolveNestVpViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v != this && v instanceof ViewPager) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
}
