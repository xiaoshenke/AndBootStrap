package wuxian.me.andbootstrapdemo.searchuser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wuxian.me.andbootstrapdemo.R;
import wuxian.me.andbootstrapdemo.utils.view.BaseHeaderAdapter;

/**
 * Created by wuxian on 8/3/2017.
 */

public class SearchAdapter<T> extends BaseHeaderAdapter<SearchAdapter.ViewHolder, T> {
    @Override
    protected SearchAdapter.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user, null, false);

        return new ViewHolder(v);
    }

    //Todo
    @Override
    protected void onBindItemViewHolder(SearchAdapter.ViewHolder holder, int position) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        SimpleDraweeView avatar;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.repos)
        TextView repos;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(itemView);
        }
    }
}
