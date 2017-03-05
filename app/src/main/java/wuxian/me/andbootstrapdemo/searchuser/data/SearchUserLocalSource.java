package wuxian.me.andbootstrapdemo.searchuser.data;

import android.util.LruCache;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


/**
 * Created by wuxian on 5/3/2017.
 * <p>
 * memory cache层 or db
 */

public class SearchUserLocalSource implements ISearchUserSource {

    private static SearchUserLocalSource source = null;

    public static SearchUserLocalSource getInstance() {
        if (source == null) {
            source = new SearchUserLocalSource();
        }
        return source;
    }

    private SearchUserLocalSource() {
    }

    private LruCache<String, List<SearchRet>> mCache = new LruCache<>(1 * 1024 * 1204);

    @Override
    public Observable<List<SearchRet>> searchUser(String search) {
        List<SearchRet> result = mCache.get(search);
        if (result == null) {
            result = new ArrayList<>();
            return Observable.just(result);
        } else {
            return Observable.just(result);
        }
    }
}
