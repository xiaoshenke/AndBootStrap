package wuxian.me.andbootstrapdemo.searchuser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import wuxian.me.andbootstrapdemo.ActivityUtils;
import wuxian.me.andbootstrapdemo.GithubActionBarActivity;
import wuxian.me.andbootstrapdemo.R;

public class GithubSearchUserActivity extends GithubActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected View getSubview() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_github_trendlist, null);

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

        return view;
    }

    @Override
    protected boolean useCustomToolbar() {
        return false;
    }
}
