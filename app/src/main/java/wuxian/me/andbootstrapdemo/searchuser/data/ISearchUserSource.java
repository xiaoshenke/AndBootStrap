package wuxian.me.andbootstrapdemo.searchuser.data;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by wuxian on 4/3/2017.
 */

public interface ISearchUserSource {

    //Todo: github 接口
    Observable<List<SearchRet>> searchUser(String search);
}
