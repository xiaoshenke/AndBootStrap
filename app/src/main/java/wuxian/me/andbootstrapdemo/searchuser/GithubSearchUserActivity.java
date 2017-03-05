package wuxian.me.andbootstrapdemo.searchuser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import wuxian.me.andbootstrapdemo.ActivityUtils;
import wuxian.me.andbootstrapdemo.R;
import wuxian.me.andbootstrapdemo.base.BaseActionbarActivity;

public class GithubSearchUserActivity extends BaseActionbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected View getSubview() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_github_trendlist, null);

        if (false) {
            GithubSearchUserFragment fragment = (GithubSearchUserFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (fragment == null) {
                fragment = new GithubSearchUserFragment();

                GithubSearchUserPresenter presenter = new GithubSearchUserPresenter();  //set presenter
                fragment.setPresenter(presenter);
                presenter.setView(fragment);

                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                        fragment, R.id.fragment_container);
            }
        }


        return view;
    }

    @Override
    protected boolean useCustomToolbar() {
        return false;
    }

    @Override
    protected String pageTitle() {
        return "GitHub SearchUser";
    }

    private static final int MENU_1 = 0;
    private static final int MENU_2 = 1;

    @Nullable
    @Override
    protected List<MenuItemData> getMenuItemDatas() {
        List<MenuItemData> menuItemDatas = new ArrayList<>();

        MenuItemData data = new MenuItemData();
        data.itemId = MENU_1;
        data.title = "menu 1";
        menuItemDatas.add(data);

        MenuItemData data1 = new MenuItemData();
        data1.itemId = MENU_2;
        data1.title = "menu 2";
        data1.atTitle = true;
        menuItemDatas.add(data1);

        return menuItemDatas;
    }
}
