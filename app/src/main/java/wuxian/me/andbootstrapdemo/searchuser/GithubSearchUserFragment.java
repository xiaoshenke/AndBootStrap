package wuxian.me.andbootstrapdemo.searchuser;


import android.support.v4.app.Fragment;

import com.google.common.base.Preconditions;

/**
 * Created by wuxian on 4/3/2017.
 */

public class GithubSearchUserFragment extends Fragment implements IGithubSearchUserView {

    private GithubSearchUserPresenter mPresenter;

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
    public void setPresenter(GithubSearchUserPresenter presenter) {
        mPresenter = Preconditions.checkNotNull(presenter);
    }
}
