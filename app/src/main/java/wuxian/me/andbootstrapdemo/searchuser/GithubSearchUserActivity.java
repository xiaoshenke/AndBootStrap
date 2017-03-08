package wuxian.me.andbootstrapdemo.searchuser;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import wuxian.me.andbootstrap.BaseActivity;
import wuxian.me.andbootstrapdemo.utils.ActivityUtils;
import wuxian.me.andbootstrapdemo.R;
import wuxian.me.andbootstrapdemo.base.BaseActionbarActivity;

public class GithubSearchUserActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_trendlist);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
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

    protected String pageTitle() {
        return "GitHub SearchUser";
    }

    private static final int MENU_1 = 0;
    private static final int MENU_2 = 1;


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
