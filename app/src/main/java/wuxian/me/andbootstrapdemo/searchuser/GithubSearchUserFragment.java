package wuxian.me.andbootstrapdemo.searchuser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.common.base.Preconditions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wuxian.me.andbootstrapdemo.R;

/**
 * Created by wuxian on 4/3/2017.
 */

public class GithubSearchUserFragment extends Fragment implements IGithubSearchUserView {

    private GithubSearchUserPresenter mPresenter;
    private View mView;

    @BindView(R.id.et_search)
    EditText mSearchEditText;

    @BindView(R.id.iv_clear)
    View mClearView;

    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    //Todo:
    @OnClick(R.id.iv_clear)
    void clearEditText() {
        ;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView != null) {
            return mView;
        }
        mView = inflater.inflate(R.layout.fragment_github_trendlist, null, false);
        ButterKnife.bind(mView);

        initView();

        return mView;
    }

    //Todo
    private void initView() {
        ;
    }

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
