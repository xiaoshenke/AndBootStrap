package wuxian.me.andbootstrapdemo;


import android.support.v4.app.Fragment;

import com.google.common.base.Preconditions;

/**
 * Created by wuxian on 4/3/2017.
 */

public class GithubTrendListFragment extends Fragment implements GithubTrendListContract.View {

    private GithubTrendListContract.Presenter mPresenter;

    @Override
    public void onResume() {
        super.onResume();

        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
    }

    @Override
    public void setPresenter(GithubTrendListContract.Presenter presenter) {
        mPresenter = Preconditions.checkNotNull(presenter);
    }
}
