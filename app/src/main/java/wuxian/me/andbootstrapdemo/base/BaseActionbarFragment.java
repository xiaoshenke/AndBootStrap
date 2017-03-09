package wuxian.me.andbootstrapdemo.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import butterknife.ButterKnife;
import wuxian.me.andbootstrap.BaseFragment;
import wuxian.me.andbootstrapdemo.R;

/**
 * Created by wuxian on 9/3/2017.
 * Todo
 */

public abstract class BaseActionbarFragment extends BaseFragment {
    private View mView;
    protected Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null) {
            return mView;
        }
        mView = inflater.inflate(R.layout.view_base_actionbar, null, false);
        ButterKnife.bind(this, mView);

        mToolbar = (Toolbar) mView.findViewById(R.id.tool_bar);

        mToolbar.setTitle(pageTitle());

        initMenu(mToolbar.getMenu());
        if (useCustomToolbar()) {
            customToolbar(mToolbar);
        }
        //setSupportActionBar(mToolbar);   //set title什么的必须在setSupportActionBar之前
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setElevation(0);

        FrameLayout subContainer = (FrameLayout) mView.findViewById(R.id.container);
        subContainer.addView(getSubview());

        return mView;
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        initMenu(menu);
        super.onPrepareOptionsMenu(menu);
    }

    private void initMenu(Menu menu) {
        menu.clear();
        List<BaseActionbarActivity.MenuItemData> menus = getMenuItemDatas();
        if (menus != null && menus.size() != 0) {
            for (BaseActionbarActivity.MenuItemData menuItemData : menus) {
                MenuItem item = menu.add(0, menuItemData.itemId, menuItemData.itemId, menuItemData.title);
                item.setOnMenuItemClickListener(menuItemData.onClickListener);
                if (menuItemData.iconRes > 0) {
                    item.setIcon(menuItemData.iconRes);
                }
            }
        }
    }

    //子类的view应该在这里控制
    @NonNull
    protected abstract View getSubview();

    protected abstract boolean useCustomToolbar();

    protected void customToolbar(Toolbar toolbar) {
    }

    @NonNull
    protected abstract String pageTitle();

    @Nullable
    protected abstract List<BaseActionbarActivity.MenuItemData> getMenuItemDatas();
}
