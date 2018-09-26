
package com.zzb.sl.TimeController.GroupController.GroupCcheck.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zzb.bean.Group;
import com.zzb.sl.R;
import com.zzb.sl.TimeController.GroupController.GroupCcheck.bean.SelectEvent;
import com.zzb.sl.TimeController.GroupController.GroupCcheck.widget.ItemTouchHelperAdapter;
import com.zzb.sl.TimeController.GroupController.GroupCcheck.widget.OnStartDragListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 微信公众号 aikaifa 欢迎关注
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    private List<Group> mItems;
    private List<Group> selected;
    public HashMap<Integer, Boolean> map;
    private EventBus eventBus;
    private final OnStartDragListener mDragStartListener;

    public ListAdapter(List<Group> mItems, OnStartDragListener dragStartListener, EventBus eventBus) {
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
            map.put(mItems.get(i).getsS_Number(), false);
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout_groupclist, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        if (null == mItems || mItems.size() <= 0) {
            return;
        }
        holder.group_list_item_content.setText(mItems.get(position).getsS_Number()+"");
        holder.group_list_item_content1.setTag(mItems.get(position).getsS_Number());//防止划回来时选中消失
        if (map != null) {
            ((ItemViewHolder) holder).group_list_item_content1.setChecked((map.get(mItems.get(position).getsS_Number())));
        } else {
            ((ItemViewHolder) holder).group_list_item_content1.setChecked(false);
        }
        holder.group_list_item_content1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String mFlags = (String) view.getTag();
                if (map != null) {
                    if (map.get(mItems.get(position).getsS_Number())) {
                        map.put(mItems.get(position).getsS_Number(), false);
                        eventBus.post(new SelectEvent(selected(map)));
                    } else {
                        map.put((Integer) view.getTag(), Boolean.TRUE);
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

    private int selected(HashMap<Integer, Boolean> map) {
        int size = 0;
        for (Integer key : map.keySet()) {
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

        public final CheckBox group_list_item_content1;
        public final TextView group_list_item_content;

        public ItemViewHolder(View itemView) {
            super(itemView);
            group_list_item_content1 = (CheckBox) itemView.findViewById(R.id.groupc_list_item_content1);
            group_list_item_content = (TextView) itemView.findViewById(R.id.groupc_list_item_content);
        }
    }

    public HashMap<Integer, Boolean> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, Boolean> map) {
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
