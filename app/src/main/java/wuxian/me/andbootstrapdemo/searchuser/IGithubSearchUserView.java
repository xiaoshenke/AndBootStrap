package wuxian.me.andbootstrapdemo.searchuser;

import java.util.List;

import wuxian.me.andbootstrap.BaseView;
import wuxian.me.andbootstrapdemo.searchuser.data.SearchRet;

/**
 * Created by wuxian on 4/3/2017.
 */

public interface IGithubSearchUserView extends BaseView<GithubSearchUserPresenter> {

    void showResult(List<SearchRet> searchRetList);
}
