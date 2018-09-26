package com.zzb.sl.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.zzb.bean.Host;
import com.zzb.sl.group.groupinfoCustomView.ItemRemoveRecyclerView;
import com.zzb.sl.group.groupinfoCustomView.MyAdapter;
import com.zzb.sl.group.groupinfoCustomView.OnItemClickListener;
import com.zzb.sl.R;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class GroupInfoActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG ="GroupHostActivity" ;
    private ItemRemoveRecyclerView itrr;
    private SwipeRefreshLayout proj_srfl;
    private List<String> grouplist = new ArrayList<>();
    private Host host = new Host();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        findViewById(R.id.grouphost_list_img2).setOnClickListener(this);
        itrr = (ItemRemoveRecyclerView) findViewById(R.id.grouphost_re_container);
        Intent intent = getIntent();
        host = (Host) intent.getSerializableExtra("SELECTUSER_GROUPHOST");

        //设置数据
        for (int i = 0; i <16 ; i++) {
            grouplist.add((i+1)+"");
        }
        MyAdapter adapter = new MyAdapter(GroupInfoActivity.this,grouplist);
        itrr.setLayoutManager(new LinearLayoutManager(GroupInfoActivity.this));
        itrr.setAdapter(adapter);
        LinearLayoutManager l = new LinearLayoutManager(GroupInfoActivity.this);
        itrr.setLayoutManager(l);
        itrr.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String ho = grouplist.get(position);
                Log.e(TAG,"跳转的:"+ho);
                Intent intent = new Intent();
                intent.setClass(GroupInfoActivity.this, GroupSetActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("SELECTUSER_GROUPINFOHO", ho);
                bundle.putSerializable("SELECTUSER_GROUPINFOHOST", host);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
