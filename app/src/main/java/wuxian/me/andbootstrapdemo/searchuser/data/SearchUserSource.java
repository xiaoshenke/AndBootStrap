package wuxian.me.andbootstrapdemo.searchuser.data;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


/**
 * Created by wuxian on 5/3/2017.
 */

public class SearchUserSource implements ISearchUserSource {

    private SearchUserLocalSource localSource = SearchUserLocalSource.getInstance();
    private SearchUserRemoteSource remoteSource = SearchUserRemoteSource.getInstance();

    private static SearchUserSource source = null;

    public static SearchUserSource getInstance() {
        if (source == null) {
            source = new SearchUserSource();
        }
        return source;
    }

    private SearchUserSource() {
    }

    @Override
    public Observable<List<SearchRet>> searchUser(String search) {
        return Observable.concat(localSource.searchUser(search), getAndCacheRemote(search))
                .filter(new Predicate<List<SearchRet>>() {
                    @Override
                    public boolean test(List<SearchRet> searchRets) throws Exception {
                        return searchRets != null && searchRets.size() != 0;
                    }
                });  //.first();  // 调用first操作符就是说如果local有数据 就会过滤掉网络请求的数据
    }

    private Observable<List<SearchRet>> getAndCacheRemote(String search) {
        return remoteSource.searchUser(search)
                .flatMap(new Function<List<SearchRet>, ObservableSource<List<SearchRet>>>() {
                    @Override
                    public ObservableSource<List<SearchRet>> apply(List<SearchRet> searchRets) throws Exception {
                        return new ObservableSource<List<SearchRet>>() {
                            @Override
                            public void subscribe(Observer<? super List<SearchRet>> observer) {
                                //save to local
                            }
                        };
                    }
                });
    }
}
