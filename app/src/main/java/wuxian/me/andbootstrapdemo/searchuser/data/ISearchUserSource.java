package wuxian.me.andbootstrapdemo.searchuser.data;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by wuxian on 4/3/2017.
 * DataSource接口 一般来说会有一个本地实现,一个服务器请求实现。
 * 如果两者都用到,那么需要一个interface和三个impl共四个文件
 */

public interface ISearchUserSource {

    //Todo: github 接口
    Observable<List<SearchRet>> searchUser(String search);
}
