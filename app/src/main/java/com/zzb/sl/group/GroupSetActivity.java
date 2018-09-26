
package com.zzb.sl.group;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzb.bean.ControlS;
import com.zzb.bean.Host;
import com.zzb.bean.TotalUrl;
import com.zzb.bean.User;
import com.zzb.sl.group.groupCheck.adapter.ListAdapter;
import com.zzb.sl.group.groupCheck.bean.Book;
import com.zzb.sl.group.groupCheck.bean.SelectEvent;
import com.zzb.sl.group.groupCheck.widget.OnStartDragListener;
import com.zzb.sl.group.groupCheck.widget.SimpleItemTouchHelperCallback;
import com.zzb.http.GroupHttp;
import com.zzb.http.SubHttp;
import com.zzb.sl.R;
import com.zzb.util.ActivityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class GroupSetActivity extends SwipeBackActivity implements OnStartDragListener, View.OnClickListener {
    private static final String TAG = "GroupSetActivity";
    private RecyclerView recyclerView;
    private CheckBox checkbox;
    private TextView selected;

    private EventBus event;
    private boolean isChange = false;
    private ArrayList<Book> list = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    private String a = "";
    private ImageView img;
    HashMap<String, Boolean> map;

    private Host host = new Host();
    private List<ControlS> lampLists;
    private String groupNumbs ="";
    private SwipeRefreshLayout sub_list_srfl;
    private ProgressDialog progressDialog = null;
    private static final int MESSAGETYPE_01 = 0x0001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_set);
        img = (ImageView) findViewById(R.id.groupset_info_img3);
        Intent intent = getIntent();
        //主机
        host = (Host) intent.getSerializableExtra("SELECTUSER_GROUPINFOHOST");
        //分组编号
        groupNumbs = (String) intent.getSerializableExtra("SELECTUSER_GROUPINFOHO");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        event = EventBus.getDefault();
        event.register(this);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        selected = (TextView) findViewById(R.id.selected);
        findViewById(R.id.groupset_list_img2).setOnClickListener(this);
        img.setOnClickListener(this);
        ButterKnife.bind(this);

        initData();
    }


    private void initData() {
        progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        //查询用户所属信息中的所属项目字段
        final User user = TotalUrl.getUser();
        final String pcke = host.getsS_RegPackage();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //主机
                lampLists = SubHttp.selectPackSub(user,pcke);
                if (lampLists == null || lampLists.size()<1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.showToasts(GroupSetActivity.this,R.string.Currentlynoinformation,1*1000);
                        }
                    });
                    //发送handler
                    Message msg_listData = new Message();
                    msg_listData.what = MESSAGETYPE_01;
                    handler.sendMessage(msg_listData);
                    return;
                }
                Collections.sort(lampLists, new Comparator<ControlS>() {
                    @Override
                    public int compare(ControlS o1, ControlS o2) {
                        if(o1.getsS_Number() > o2.getsS_Number()){
                            return 1;
                        }
                        if(o1.getsS_Number() == o2.getsS_Number()){
                            return 0;
                        }
                        return -1;
                    }
                });

                for (int i = 0; i < lampLists.size(); i++) {
                    Book model = new Book();
                    model.setId(lampLists.get(i).getsS_Number());
                    model.setName(lampLists.get(i).getsS_Number()+"");
                    model.setDesc(getString(R.string.describe) + i);
                    list.add(model);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final ListAdapter adapter = new ListAdapter(list, GroupSetActivity.this, event);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(GroupSetActivity.this));

                        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                        mItemTouchHelper = new ItemTouchHelper(callback);
                        mItemTouchHelper.attachToRecyclerView(recyclerView);
                        checkbox.setChecked(true);
                        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                map = new HashMap<String, Boolean>();
                                try {
                                    int count = 0;
                                    if (isChecked) {
                                        isChange = false;
                                    }

                                    for (int i = 0, p = list.size(); i < p; i++) {
                                        if (isChecked) {
                                            map.put(list.get(i).getId()+"", true);

                                            count++;
                                            a += list.get(i).getId() + ",";
                                        } else {
                                            if (!isChange) {
                                                map.put(list.get(i).getId()+"", false);

                                                count = 0;
                                                a = "";
                                            } else {
                                                map = adapter.getMap();

                                                count = map.size();
                                            }

                                        }
                                    }

                                    selected.setText(getString(R.string.Selected) + count + getString(R.string.item));
                                    adapter.setMap(map);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        adapter.setOnItemClickListener(new ListAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(RecyclerView.ViewHolder holder, int positon) {
                                Log.e("onItemClick", "" + positon);

                            }

                            @Override
                            public void onItemLongClick(final RecyclerView.ViewHolder holder, final int positon) {
                                Log.e("onItemLongClick", "" + positon);

                            }
                        });
                    }
                });
                //发送handler
                Message msg_listData = new Message();
                msg_listData.what = MESSAGETYPE_01;
                handler.sendMessage(msg_listData);

            }
        }).start();


    }

    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MESSAGETYPE_01:
                    //刷新UI，显示数据，并关闭进度条
                    progressDialog.dismiss(); //关闭进度条
                    break;
            }
        }
    };



    public void onEventMainThread(SelectEvent event) {
        int size = event.getSize();
        if (size < list.size()) {
            isChange = true;
            checkbox.setChecked(false);

        } else {
            checkbox.setChecked(true);
            isChange = false;

        }
        selected.setText(getString(R.string.Selected) + size + getString(R.string.item));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        event.unregister(this);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    //获取数据
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.groupset_list_img2:
                finish();
                break;
            case R.id.groupset_info_img3:
                String numb = "";
                if (map == null || map.size() < 1) {
                    ActivityUtil.showToasts(GroupSetActivity.this,R.string.PleaseSelectData, 1 * 1000);
                } else {
                    for (String in : map.keySet()) {
                        boolean str = map.get(in);
                        Log.e(TAG, "获取数据:" + in + "  " + str);
                        if (str) {
                            numb += in + ",";
                        }
                    }

                    String[] numbString = numb.substring(0, numb.length() - 1).split(",");
                    int[] intTemp = new int[numbString.length];
                    for (int i = 0; i < numbString.length; i++) {
                        intTemp[i] = Integer.parseInt(numbString[i]);
                    }
                    for (int m = 0; m < intTemp.length; m++) {
                        for (int n = intTemp.length-1; n > 0; n--) {
                            if (intTemp[n] < intTemp[n - 1]) {
                                int temp = intTemp[n];
                                intTemp[n] = intTemp[n - 1];
                                intTemp[n - 1] = temp;
                            }
                        }
                    }
                    String numbS="";
                    for (int i = 0; i <intTemp.length ; i++) {
                        Log.e(TAG, "排序:" + intTemp[i]);
                        numbS+=intTemp[i]+",";
                    }


                    //获取注册包
                    String prack = host.getsS_RegPackage();
                    //获取分组内容
                    String nub = setStringNumb(numbS);
                    nub = nub.substring(0,nub.length()-1);
                    //获取机构
                    String org = host.getsSL_Organize_S_Id();

                    //分组配置
                    final String Purl = prack+","+groupNumbs+","+nub+","+org;
                    //分组信息获取
                    final String Hurl = prack+","+groupNumbs+","+org;
                    new AlertDialog.Builder(this).setTitle(getString(R.string.Add))
                            .setMessage(getString(R.string.WhetherToAdd))
                            .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    addGroup(Hurl,Purl);
                                }
                            }).setNegativeButton(getResources().getString(R.string.No),new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                        }
                    }).show();//在按键响应事件中显示此对话框
                }
                break;
        }
    }
     private void addGroup(final String Purl, final String Hurl){
         progressDialog = ProgressDialog.show(this,getResources().getString(R.string.Loading), getResources().getString(R.string.PleaseLoading));

         new Thread(new Runnable() {
             @Override
             public void run() {
                boolean b = GroupHttp.SubGroup(Purl);
                 if (b){
                     boolean c = GroupHttp.SubGroupConfig(Hurl);
                     if (c){
                         runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 ActivityUtil.showToasts(GroupSetActivity.this, R.string.Addsuccess, 1 * 1000);
                             }
                         });
                         //发送handler
                         Message msg_listData = new Message();
                         msg_listData.what = MESSAGETYPE_01;
                         handler2.sendMessage(msg_listData);
                         return;
                     }
                 }else{
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             ActivityUtil.showToasts(GroupSetActivity.this,R.string.PleaseCheckToSeeIfItIsOnline, 1 * 1000);
                         }
                     });
                     //发送handler
                     Message msg_listData = new Message();
                     msg_listData.what = MESSAGETYPE_01;
                     handler2.sendMessage(msg_listData);
                     return;
                 }
             }
         }).start();
     }

    private Handler handler2 = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case MESSAGETYPE_01:
                    //刷新UI，显示数据，并关闭进度条
                    progressDialog.dismiss(); //关闭进度条
                    break;
            }
        }
    };
    /**
     *
     * @param sNumb  传递过来的字符串用于遍历数组
     * @return
     */
    //数组对比将String 类型的字符串转换为int数组
    private String setStringNumb(String sNumb){
        //去除，
        String [] stringArr= sNumb.split(",");
        int[] arry=new int[512];
        for(int i=0;i<arry.length;i++){
            arry[i] = i+1;
            if(arry[i] ==1 ){
                arry[i]=-1;
            }
            for (int j=0;j<stringArr.length;j++) {
                String ids = stringArr[j];
                String str2 = ids.replaceAll(" ", "");
                int str3 = Integer.parseInt(str2);
                if(str3 == 1){
                    str3 = -1;
                }
                //设置选中的如果和512其中的数相等就把他替换成1
                if(arry[i] == str3){
                    arry[i] = 1;
                }
            }
        }
        int splitSize = 8;//分割的块大小
        Object[] subAry = splitAry(arry, splitSize);//分割后的子块数组
        List<String> list = new ArrayList<String>();
        //循环的64个数组
        for(Object obj: subAry){//打印输出结果
            int[] aryItem = (int[]) obj;
            //遍历（一个a就代表一个数组）
            int [] a = getSortD_X(aryItem);
            for(int i = 0;i<a.length;i++){
                if(a[i] > 1 || a[i] == -1){
                    a[i] = 0;
                }
            }
            //将数组添加进List集合中(转换成16进制)
            /**
             * 将数组a转换成int整数a1
             * 将a1转换成String字符a2
             * 将a2转换成16进制然后添加进集合
             */

            int a1 =  ConvertIntArrByInt(a);
            String a2 = String.valueOf(a1);
            System.out.println("将a1转换成String字符a2:"+a2);
            list.add(binaryString2hexString(a2));
        }
        String str = "";
        for(int i=0;i<list.size();i++){
            str += list.get(i)+".";

        }
        //将数组整合
        String str2 = str.replace("[","").replace("]","").replace(" ", "").replace(",", "");


        System.out.println("这是输出结果:-----"+str2);
        return str2;
    }


    /**
     *
     * @param ary 总数组
     * @param subSize  位数
     * @return
     */
    private static Object[] splitAry(int[] ary, int subSize) {
        //计数 计算能分割多少个数组
        int count = ary.length % subSize == 0 ? ary.length / subSize: ary.length / subSize + 1;
        List<List<Integer>> subAryList = new ArrayList<List<Integer>>();
        for (int i = 0; i < count; i++) {
            int index = i * subSize;
            List<Integer> list = new ArrayList<Integer>();
            int j = 0;
            while (j < subSize && index < ary.length) {
                list.add(ary[index++]);
                j++;
            }
            subAryList.add(list);
        }

        Object[] subAry = new Object[subAryList.size()];
        for(int i = 0; i < subAryList.size(); i++){
            List<Integer> subList = subAryList.get(i);
            int[] subAryItem = new int[subList.size()];
            for(int j = 0; j < subList.size(); j++){
                subAryItem[j] = subList.get(j).intValue();
            }
            subAry[i] = subAryItem;
        }
        return subAry;
    }



    /**
     *
     * @param a  8位的数组
     * @return
     */
    static int i = 1;
    public static int[] getSortD_X(int[] a) {

        // new一个新数组。
        int[] newa = new int[a.length];
        for (int i = a.length - 1; i >= 0; i--) {
            // 将源数组从最后元素开始，一次复制给新数组。
            System.arraycopy(a, i, newa, a.length - i - 1,  1);
        }
        System.out.println("我是第:"+i+"个数组"+ Arrays.toString(newa));
        i++;
        return newa;
    }

    /**
     *将int数组转换为int整数
     * @param ints
     * @return
     */
    public int ConvertIntArrByInt(int[] ints)
    {
        String str = "";
        for (int i = 0; i < ints.length; i++) {
            str += ints[i]+"";
        }

        return Integer.valueOf(str);

    }

    /**
     * 将二进制转换为10进制
     * @param bString
     * @return
     */
    public static String binaryString2hexString(String bString)
    {
        //System.out.println("二进制转换为16进制bString:"+bString);
        //return Long.toHexString(Long.parseLong(bString,2));
        return Integer.valueOf(bString,2).toString();
    }


}
