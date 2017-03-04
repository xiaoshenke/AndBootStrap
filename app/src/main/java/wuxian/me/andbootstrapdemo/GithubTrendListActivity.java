package wuxian.me.andbootstrapdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import wuxian.me.andbootstrap.BaseActionbarActivity;

public class GithubTrendListActivity extends BaseActionbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected View getSubview() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_github_trendlist, null);

        GithubTrendListFragment fragment = (GithubTrendListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new GithubTrendListFragment();
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
