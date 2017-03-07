package wuxian.me.andbootstrapdemo.api;


import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by wuxian on 4/3/2017.
 */

public interface GithubService {

    @GET("search/users")
    Observable<SearchUserBean> searchUser(@QueryMap Map<String, String> map);
}
