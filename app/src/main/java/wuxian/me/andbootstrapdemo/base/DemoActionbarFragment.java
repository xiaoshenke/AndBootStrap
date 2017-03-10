package wuxian.me.andbootstrapdemo.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import wuxian.me.andbootstrapdemo.R;

/**
 * Created by wuxian on 9/3/2017.
 */

public class DemoActionbarFragment extends BaseActionbarFragment {
    @NonNull
    @Override
    protected View getSubview() {
        return inflate(R.layout.fragment_demo);
    }

    @Override
    protected boolean useCustomToolbar() {
        return false;
    }

    @NonNull
    @Override
    protected String pageTitle() {
        return "Demo";
    }

    private static final int MENU_1 = 0;
    private static final int MENU_2 = 1;

    @Nullable
    @Override
    protected List<BaseActionbarActivity.MenuItemData> getMenuItemDatas() {

        List<BaseActionbarActivity.MenuItemData> menuItemDatas = new ArrayList<>();

        BaseActionbarActivity.MenuItemData data = new BaseActionbarActivity.MenuItemData();
        data.itemId = MENU_1;
        data.title = "menu 1";
        menuItemDatas.add(data);

        BaseActionbarActivity.MenuItemData data1 = new BaseActionbarActivity.MenuItemData();
        data1.itemId = MENU_2;
        data1.title = "menu 2";
        data1.atTitle = true;
        menuItemDatas.add(data1);
        return menuItemDatas;
    }
}
