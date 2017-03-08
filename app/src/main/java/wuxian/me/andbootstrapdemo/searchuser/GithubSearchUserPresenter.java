package wuxian.me.andbootstrapdemo.searchuser;

import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import wuxian.me.andbootstrap.BasePresenter;
import wuxian.me.andbootstrap.scheduler.SchedulerProvider;
import wuxian.me.andbootstrapdemo.searchuser.data.SearchRet;
import wuxian.me.andbootstrapdemo.searchuser.data.SearchUserRemoteSource;
import wuxian.me.andbootstrapdemo.searchuser.data.SearchUserSource;

/**
 * Created by wuxian on 4/3/2017.
 */

public class GithubSearchUserPresenter implements BasePresenter {

    private IGithubSearchUserView mView;

    private SearchUserSource mSource = SearchUserSource.getInstance();

    public void setView(IGithubSearchUserView view) {
        mView = Preconditions.checkNotNull(view);
    }

    public void searchUser(String search) {
        SearchUserRemoteSource.getInstance().searchUser(search)
                //.subscribeOn(SchedulerProvider.getInstance().io())
                //.observeOn(SchedulerProvider.getInstance().ui())
                .subscribe(new Observer<List<SearchRet>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<SearchRet> value) {
                        mView.showResult(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {

    }
}
