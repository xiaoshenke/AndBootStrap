package wuxian.me.andbootstrapdemo.searchuser;

import com.google.common.base.Preconditions;

import wuxian.me.andbootstrap.BasePresenter;
import wuxian.me.andbootstrapdemo.searchuser.data.SearchUserSource;

/**
 * Created by wuxian on 4/3/2017.
 */

public class GithubSearchUserPresenter implements BasePresenter {

    private IGithubSearchUserView mView;

    private SearchUserSource mSource;

    public void setView(IGithubSearchUserView view) {
        mView = Preconditions.checkNotNull(view);
    }

    @Override
    public void subscribe() {
        //Todo:

    }

    @Override
    public void unsubscribe() {

    }
}
