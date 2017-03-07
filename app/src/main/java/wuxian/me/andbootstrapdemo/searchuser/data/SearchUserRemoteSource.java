package wuxian.me.andbootstrapdemo.searchuser.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import wuxian.me.andbootstrap.scheduler.SchedulerProvider;
import wuxian.me.andbootstrapdemo.api.GithubService;
import wuxian.me.andbootstrapdemo.utils.MyRetrofit;
import wuxian.me.andbootstrapdemo.api.SearchUserBean;

/**
 * Created by wuxian on 5/3/2017.
 */

public class SearchUserRemoteSource implements ISearchUserSource {

    private static SearchUserRemoteSource source = null;

    public static SearchUserRemoteSource getInstance() {
        if (source == null) {
            source = new SearchUserRemoteSource();
        }
        return source;
    }

    private SearchUserRemoteSource() {
    }

    @Override
    public Observable<List<SearchRet>> searchUser(final String search) {
        GithubService service = MyRetrofit.getInstance().create(GithubService.class);

        Map<String, String> map = new HashMap<>();
        map.put("q", search);
        return service.searchUser(map)
                .subscribeOn(SchedulerProvider.getInstance().io())
                .observeOn(SchedulerProvider.getInstance().ui())
                .map(new Function<SearchUserBean, List<SearchRet>>() {
                    @Override
                    public List<SearchRet> apply(SearchUserBean searchUserBean) throws Exception {
                        List<SearchRet> ret = new ArrayList<SearchRet>();

                        if (searchUserBean != null && searchUserBean.items != null) {
                            for (SearchUserBean.SearchItem item : searchUserBean.items) {
                                SearchRet searchRet = new SearchRet();
                                searchRet.avatar_url = item.avatar_url;

                                ret.add(searchRet);
                            }
                        }
                        return ret;
                    }
                });
    }
}
