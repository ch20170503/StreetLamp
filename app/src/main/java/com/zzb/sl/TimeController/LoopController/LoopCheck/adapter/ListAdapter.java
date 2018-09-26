
package com.zzb.sl.TimeController.LoopController.LoopCheck.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzb.bean.HostDataSet;
import com.zzb.sl.R;
import com.zzb.sl.TimeController.LoopController.LoopCheck.bean.SelectEvent;
import com.zzb.sl.TimeController.LoopController.LoopCheck.widget.ItemTouchHelperAdapter;
import com.zzb.sl.TimeController.LoopController.LoopCheck.widget.OnStartDragListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 微信公众号 aikaifa 欢迎关注
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    private static final String TAG ="ListAdapter" ;
    private List<HostDataSet> mItems;
    private List<HostDataSet> selected;
    public HashMap<String, Boolean> map;
    private EventBus eventBus;
    private final OnStartDragListener mDragStartListener;

    public ListAdapter(List<HostDataSet> mItems, OnStartDragListener dragStartListener, EventBus eventBus) {
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
            map.put(mItems.get(i).getsS_RegPackage(), false);
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout_looplist, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        if (null == mItems || mItems.size() <= 0) {
            return;
        }
        holder.loop_list_item_content.setText(mItems.get(position).getsS_Fname());
        if (mItems.get(position).getsS_Online() == 1){
            holder.loop_list_play_img3.setImageResource(R.drawable.greed);
        }else{
            holder.loop_list_play_img3.setImageResource(R.drawable.red);
        }
        String[] timeString = mItems.get(position).getsS_UpdateTime().split("T");
        holder.loop_list_text1.setText(timeString[0]);
        String time2 = timeString[1];
        String tmmm;
        if( time2.indexOf(".") != -1){
            tmmm = time2.substring(0, time2.indexOf("."));
            Log.e(TAG,"赋值1:"+tmmm);
        }else{
            tmmm = time2;
            Log.e(TAG,"赋值2:"+tmmm);
        }
        holder.loop_list_text2.setText(tmmm);
        holder.loop_list_item_content1.setTag(new Integer(mItems.get(position).getsS_RegPackage()));//防止划回来时选中消失
        if (map != null) {
            ((ItemViewHolder) holder).loop_list_item_content1.setChecked((map.get(mItems.get(position).getsS_RegPackage())));
        } else {
            ((ItemViewHolder) holder).loop_list_item_content1.setChecked(false);
        }
        holder.loop_list_item_content1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String mFlags = (String) view.getTag();
                if (map != null) {
                    if (map.get(mItems.get(position).getsS_RegPackage())) {
                        map.put(mItems.get(position).getsS_RegPackage(), false);
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

        public final CheckBox loop_list_item_content1;
        public final TextView loop_list_item_content;
        public final ImageView loop_list_play_img3;
        public final TextView loop_list_text1;
        public final TextView loop_list_text2;

        public ItemViewHolder(View itemView) {
            super(itemView);
            loop_list_item_content1 = (CheckBox) itemView.findViewById(R.id.loop_list_item_content1);
            loop_list_item_content = (TextView) itemView.findViewById(R.id.loop_list_item_content);
            loop_list_play_img3 = (ImageView) itemView.findViewById(R.id.loop_list_play_img3);
            loop_list_text1 = (TextView) itemView.findViewById(R.id.loop_list_text1);
            loop_list_text2 = (TextView) itemView.findViewById(R.id.loop_list_text2);
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
