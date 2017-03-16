package wuxian.me.andbootstrapdemo.utils.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView.Adapter extension created to add header capability support and a generic List of
 * items really useful most of the cases. You should extend from this class and override
 * onCreateViewHolder to create your ViewHolder instances and onBindViewHolder methods to draw your
 * user interface as you wish.
 * <p/>
 * The usage of List<T> items member is not mandatory. If you are going to provide your custom
 * implementation remember to override getItemCount method.
 */
public abstract class BaseHeaderAdapter<VH extends RecyclerView.ViewHolder, T>
        extends RecyclerView.Adapter<VH> {

    protected static final int TYPE_HEADER = -2;
    protected static final int TYPE_ITEM = -1;
    protected static final int TYPE_FOOTER = -3;

    protected View mHeaderView;
    protected List<T> mDatas = new ArrayList<>();
    protected View mFooterView;
    private boolean showFooter = false;

    /**
     * Invokes onCreateHeaderViewHolder, onCreateItemViewHolder or onCreateFooterViewHolder methods
     * based on the view type param.
     */
    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH viewHolder;
        if (isHeaderType(viewType)) {
            viewHolder = onCreateHeaderViewHolder(parent, viewType);
        } else if (isFooterType(viewType)) {
            viewHolder = onCreateFooterViewHolder(parent, viewType);
        } else {
            viewHolder = onCreateItemViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    /**
     * If you don't need header feature, you can bypass overriding this method.
     */
    protected VH onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    protected abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    /**
     * If you don't need footer feature, you can bypass overriding this method.
     */
    protected VH onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * Invokes onBindHeaderViewHolder, onBindItemViewHolder or onBindFooterViewHOlder methods based
     * on the position param.
     */
    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (isHeaderPosition(position)) {
            onBindHeaderViewHolder(holder, position);
        } else if (isFooterPosition(position)) {
            onBindFooterViewHolder(holder, position);
        } else {
            onBindItemViewHolder(holder, position);
        }
    }

    /**
     * If you don't need header feature, you can bypass overriding this method.
     */
    protected void onBindHeaderViewHolder(VH holder, int position) {
    }

    protected abstract void onBindItemViewHolder(VH holder, int position);

    /**
     * If you don't need footer feature, you can bypass overriding this method.
     */
    protected void onBindFooterViewHolder(VH holder, int position) {
    }

    /**
     * Invokes onHeaderViewRecycled, onItemViewRecycled or onFooterViewRecycled methods based
     * on the holder.getAdapterPosition()
     */
    @Override
    public final void onViewRecycled(VH holder) {
        int position = holder.getAdapterPosition();

        if (isHeaderPosition(position)) {
            onHeaderViewRecycled(holder);
        } else if (isFooterPosition(position)) {
            onFooterViewRecycled(holder);
        } else {
            onItemViewRecycled(holder);
        }
    }

    protected void onHeaderViewRecycled(VH holder) {
    }

    protected void onItemViewRecycled(VH holder) {
    }

    protected void onFooterViewRecycled(VH holder) {
    }

    /**
     * Returns the type associated to an item given a position passed as arguments. If the position
     * is related to a header item returns the constant TYPE_HEADER or TYPE_FOOTER if the position is
     * related to the footer, if not, returns TYPE_ITEM.
     * <p/>
     * If your application has to support different types override this method and provide your
     * implementation. Remember that TYPE_HEADER, TYPE_ITEM and TYPE_FOOTER are internal constants
     * can be used to identify an item given a position, try to use different values in your
     * application.
     */
    @Override
    public int getItemViewType(int position) {
        int viewType = TYPE_ITEM;
        if (isHeaderPosition(position)) {
            viewType = TYPE_HEADER;
        } else if (isFooterPosition(position)) {
            viewType = TYPE_FOOTER;
        }
        return viewType;
    }

    /**
     * Returns the items list size if there is no a header configured or the size taking into account
     * that if a header or a footer is configured the number of items returned is going to include
     * this elements.
     */
    @Override
    public int getItemCount() {
        int size = mDatas.size();
        if (hasHeader()) {
            size++;
        }
        if (hasFooter()) {
            size++;
        }
        return size;
    }

    /**
     * Get header data in this adapter
     * in the adapter initialization code to set header data.
     *
     * @return header data
     */
    public View getHeader() {
        return mHeaderView;
    }

    /**
     * Get item data in this adapter with the specified postion,
     * in the adapter initialization code to set header data.
     *
     * @return item data in the specified postion
     */
    public T getItem(int position) {
        if (hasHeader() && hasItems()) {
            --position;
        }
        return mDatas.get(position);
    }

    /**
     * Get footer data in this adapter
     * in the adapter initialization code to set footer data.
     *
     * @return footer data
     */
    public View getFooter() {
        return mFooterView;
    }

    /**
     * If you need a header, you should set header data in the adapter initialization code.
     *
     * @param header header data
     */
    public void setHeadView(View header) {
        this.mHeaderView = header;
    }

    /**
     * You should set header data in the adapter initialization code.
     *
     * @param items item data list
     */
    public void resetData(List<T> items) {
        validateItems(items);
        mDatas = new ArrayList<>(items);
    }

    public void addData(List<T> items) {
        validateItems(items);
        mDatas.addAll(items);
    }

    /**
     * If you need a footer, you should set footer data in the adapter initialization code.
     */
    public void setFootView(View footer) {
        this.mFooterView = footer;
    }

    /**
     * Call this method to show hiding footer.
     */
    public void showFooter() {
        this.showFooter = true;
        notifyDataSetChanged();
    }

    /**
     * Call this method to hideImmediate footer.
     */
    public void hideFooter() {
        this.showFooter = false;
        notifyDataSetChanged();
    }

    /**
     * Returns true if the position type parameter passed as argument is equals to 0 and the adapter
     * has a not null header already configured.
     */
    public boolean isHeaderPosition(int position) {
        return hasHeader() && position == 0;
    }

    /**
     * Returns true if the position type parameter passed as argument is equals to
     * <code>getItemCount() - 1</code>
     * and the adapter has a not null header already configured.
     */
    public boolean isFooterPosition(int position) {
        int lastPosition = getItemCount() - 1;
        return hasFooter() && position == lastPosition;
    }

    /**
     * Returns true if the view type parameter passed as argument is equals to TYPE_HEADER.
     */
    protected boolean isHeaderType(int viewType) {
        return viewType == TYPE_HEADER;
    }

    /**
     * Returns true if the view type parameter passed as argument is equals to TYPE_FOOTER.
     */
    protected boolean isFooterType(int viewType) {
        return viewType == TYPE_FOOTER;
    }

    /**
     * Returns true if the header configured is not null.
     */
    public boolean hasHeader() {  //暴露给pushNotifyView
        return getHeader() != null;
    }

    /**
     * Returns true if the footer configured is not null.
     */
    public boolean hasFooter() {
        return getFooter() != null && showFooter;
    }

    /**
     * Returns true if the item configured is not empty.
     */
    private boolean hasItems() {
        return mDatas.size() > 0;
    }

    private void validateItems(List<T> items) {
        if (items == null) {
            throw new IllegalArgumentException("You can't use a null List<Item> instance.");
        }
    }

    public List<T> getData() {
        return mDatas;
    }
}
