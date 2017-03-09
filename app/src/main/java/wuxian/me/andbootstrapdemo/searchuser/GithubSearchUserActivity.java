package wuxian.me.andbootstrapdemo.searchuser;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import wuxian.me.andbootstrap.BaseActivity;
import wuxian.me.andbootstrapdemo.base.DemoActionbarFragment;
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

        /*
        DemoActionbarFragment fragment = (DemoActionbarFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new DemoActionbarFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.fragment_container);
        }
        */
    }
}
