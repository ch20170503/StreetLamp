
package com.zzb.fragment.sc.stl.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zzb.bean.Strategy;
import com.zzb.fragment.sc.stl.bean.SelectEvent;
import com.zzb.fragment.sc.stl.widget.ItemTouchHelperAdapter;
import com.zzb.fragment.sc.stl.widget.OnStartDragListener;
import com.zzb.sl.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 微信公众号 aikaifa 欢迎关注
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = "ListAdapter";
    private List<Strategy> mItems;
    private List<Strategy> selected;
    public HashMap<String, Boolean> map;
    private EventBus eventBus;
    private final OnStartDragListener mDragStartListener;

    public ListAdapter(List<Strategy> mItems, OnStartDragListener dragStartListener, EventBus eventBus) {
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
            map.put(mItems.get(i).getsS_Id(), false);
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout_alllamplist, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        if (null == mItems || mItems.size() <= 0) {
            return;
        }
        holder.alllamp_list_item_content.setText(mItems.get(position).getsS_Number()+"     "+mItems.get(position).getsS_FullName());

        holder.alllamp_list_item_content1.setTag(mItems.get(position).getsS_Id());//防止划回来时选中消失
        if (map != null) {
            Log.e(TAG,"map.get(mItems.get(position).getsS_Id()):---"+map.get(mItems.get(position).getsS_Id()));
            ((ItemViewHolder) holder).alllamp_list_item_content1.setChecked((map.get(mItems.get(position).getsS_Id())));
        } else {
            ((ItemViewHolder) holder).alllamp_list_item_content1.setChecked(false);
        }
        holder.alllamp_list_item_content1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String mFlags = (String) view.getTag();
                if (map != null) {
                    if (map.get(mItems.get(position).getsS_Id())) {
                        map.put(mItems.get(position).getsS_Id(), false);
                        eventBus.post(new SelectEvent(selected(map)));
                    } else {
                        map.put((String) view.getTag(), Boolean.TRUE);
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

        public final CheckBox alllamp_list_item_content1;
        public final TextView alllamp_list_item_content;

        public ItemViewHolder(View itemView) {
            super(itemView);
            alllamp_list_item_content1 = (CheckBox) itemView.findViewById(R.id.alllamp_list_item_content1);
            alllamp_list_item_content = (TextView) itemView.findViewById(R.id.alllamp_list_item_content);
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
