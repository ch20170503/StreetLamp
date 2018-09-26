
package com.zzb.sl.group.groupCheck.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzb.sl.R;
import com.zzb.sl.group.groupCheck.bean.Book;
import com.zzb.sl.group.groupCheck.bean.SelectEvent;
import com.zzb.sl.group.groupCheck.widget.ItemTouchHelperAdapter;
import com.zzb.sl.group.groupCheck.widget.OnStartDragListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 微信公众号 aikaifa 欢迎关注
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    private List<Book> mItems;
    private List<Book> selected;
    public HashMap<String, Boolean> map;
    private EventBus eventBus;
    private final OnStartDragListener mDragStartListener;

    public ListAdapter(List<Book> mItems, OnStartDragListener dragStartListener, EventBus eventBus) {
        mDragStartListener = dragStartListener;
        this.mItems = mItems;
        this.eventBus = eventBus;
        map = new HashMap<>();
        selected = new ArrayList<>();
        init();
    }



    private void init() {
        if (null == mItems || mItems.size() <= 0) {
            return;
        }
        for (int i = 0, p = mItems.size(); i < p; i++) {
            map.put(mItems.get(i).getId()+"", false);
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_item_groupcheck, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        if (null == mItems || mItems.size() <= 0) {
            return;
        }
        holder.name.setText(mItems.get(position).getName());
        holder.desc.setText(mItems.get(position).getDesc());
        holder.checkBox.setTag(new Integer(mItems.get(position).getId()));//防止划回来时选中消失
        if (map != null) {
            ((ItemViewHolder) holder).checkBox.setChecked((map.get(mItems.get(position).getId()+"")));
        } else {
            ((ItemViewHolder) holder).checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String mFlags = (String) view.getTag();
                if (map != null) {
                    if (map.get(mItems.get(position).getId()+"")) {
                        map.put(mItems.get(position).getId()+"", false);
                        eventBus.post(new SelectEvent(selected(map)));
                    } else {
                        map.put(view.getTag().toString(), Boolean.TRUE);
                        eventBus.post(new SelectEvent(selected(map)));
                    }
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(holder,holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mItemClickListener.onItemLongClick(holder,holder.getAdapterPosition());
                return true;
            }
        });
    }

    private int selected(HashMap<String, Boolean> map) {
        int size = 0;
        for (String key : map.keySet()) {
            if(map.get(key)){
                size++;
            }
        }
        return size;
    }
    @Override
    public int getItemCount() {
        return mItems == null? 0 :mItems.size();
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public final CheckBox checkBox;
        public final TextView name;
        public final TextView desc;
        public final ImageView handleView;
        public final ImageView up;
        public ItemViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            desc = (TextView) itemView.findViewById(R.id.tv_desc);
            handleView = (ImageView) itemView.findViewById(R.id.handle);
            up = (ImageView) itemView.findViewById(R.id.up);
        }
    }

    public HashMap<String, Boolean> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Boolean> map) {
        this.map = map;
        notifyDataSetChanged();
    }

    /**
     * 点击事件和长按事件
     */
    public interface ItemClickListener{
        void onItemClick(RecyclerView.ViewHolder holder, int position);
        void onItemLongClick(RecyclerView.ViewHolder holder, int position);
    }

    private ItemClickListener mItemClickListener;
    public void setOnItemClickListener(ItemClickListener listener){
        this.mItemClickListener=listener;
    }




}
