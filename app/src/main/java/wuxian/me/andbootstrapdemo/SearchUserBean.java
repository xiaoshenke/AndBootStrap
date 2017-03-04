package wuxian.me.andbootstrapdemo;

import java.util.List;

/**
 * Created by wuxian on 4/3/2017.
 */

public class SearchUserBean {

    public int total_count;

    public boolean imcomplete_results;

    public List<SearchItem> items;

    public static class SearchItem {
        public String login;

        public int id;

        public String avatar_url;

        public String url;

        public String repos_url;

        public String score;
    }
}
