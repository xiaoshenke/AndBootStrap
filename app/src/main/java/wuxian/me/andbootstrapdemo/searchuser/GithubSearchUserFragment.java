package wuxian.me.andbootstrapdemo.searchuser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Preconditions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wuxian.me.andbootstrapdemo.R;
import wuxian.me.andbootstrapdemo.searchuser.data.SearchRet;

/**
 * Created by wuxian on 4/3/2017.
 */

public class GithubSearchUserFragment extends Fragment implements IGithubSearchUserView {

    private GithubSearchUserPresenter mPresenter;
    private View mView;

    private SearchAdapter mAdapter;

    @BindView(R.id.et_search)
    EditText mSearchEditText;

    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    @OnClick(R.id.iv_clear)
    void clearEditText() {
        mSearchEditText.setText("");
        mRecyclerView.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null) {
            return mView;
        }
        mView = inflater.inflate(R.layout.fragment_github_trendlist, null, false);
        ButterKnife.bind(this, mView);

        initView();
        return mView;
    }

    private void initView() {
        mAdapter = new SearchAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        mSearchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  //点击确定的时候 键盘收起
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    if (mRecyclerView.getVisibility() == View.VISIBLE) {
                        //hideIME();
                    } else {
                        if (mSearchEditText.length() != 0) {
                            mPresenter.searchUser(mSearchEditText.getText().toString());
                        }
                        mRecyclerView.setVisibility(View.GONE);
                    }
                    return true;

                }
                return false;
            }
        });
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    mPresenter.searchUser(s.toString());
                }
                mRecyclerView.setVisibility(View.GONE);  //这里先做这样的简化 --> 只要有文字变化就认为应该清空之前的搜索
            }
        });

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

    @Override
    public void showResult(List<SearchRet> searchRetList) {
        if (searchRetList != null && searchRetList.size() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.resetData(searchRetList);
            mAdapter.notifyDataSetChanged();
        }
    }
}
