package com.cfjn.javacf.activity.bookkeeping;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.adapter.CommonAdapter;
import com.cfjn.javacf.adapter.ViewHolder;
import com.cfjn.javacf.adapter.bookkeeping.ClassfiyRootAdapter;
import com.cfjn.javacf.base.MybookkeepingDbHepler;
import com.cfjn.javacf.modle.ChildType;
import com.cfjn.javacf.modle.RootType;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.widget.NavigationBar;
import com.cfjn.javacf.widget.SetBudgetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 作者： wh
 * 时间： 2016-6-27
 * 名称： 记账-选择分类
 * 版本说明：代码规范整改
 * 附加注释：点击列表右侧的五角星，添加或删除常用分类
 * 主要接口：无
 *
 */
public class ClasstifyActivity extends Activity {

    /**
     * 添加新分类
     */
    // private LinearLayout ll_add;

    /**
     * 父listView
      */
    private ListView lv_root;
    /**
     * 子listView
     */
    private ListView lv_submit;
    private ClassfiyRootAdapter rootAdapter;
    private CommonAdapter<ChildType> childAdapter;
    /**
     * 所有列表的内容
     */
    private List<RootType> rootTypeLists;
    /**
     *  父列表的内容
     */
    private List<RootType> rootTypes;
    /**
     * 父类收入列表的内容
     */
    List<RootType> rootTypeList;
    /**
     * 父类收入列表的内容
     */
    List<RootType> incomeroottypes;

    private List<List<ChildType>> list;
    /**
     * 导航条
     */
    private NavigationBar bar;
    private int type;
    private static final int EXPEND = 0;
    private static final int INCOME = 1;
    /**
     * 数据库
     */
    private MybookkeepingDbHepler hepler;
    private SQLiteDatabase database;
    /**
     * 添加分类对话框
     */
    private SetBudgetDialog dialog;
    private int groupposition = -1;
    private int childposition = -1;
    public Map<Integer, Boolean> MAP;
    /**
     * 记录当前位置是否收藏列表
     */
    private  List<Map<Integer, Boolean>> mlist;
    /**
     * 记录当前位置是否收藏支出列表
     */
    private  List<Map<Integer, Boolean>> expendlist;
    /**
     * 记录当前位置是否收藏收入列表
     */
    private  List<Map<Integer, Boolean>> incomelist;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0x00){
                setAlldata(database);
                getCollection(database);//改变收藏与取消图标颜色
            }
            rootAdapter.notifyDataSetChanged();
            childAdapter.notifyDataSetChanged();
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeepingclasstify);
        lv_root = (ListView) findViewById(R.id.lv_root);
        lv_submit = (ListView) findViewById(R.id.lv_submit);
        //ll_add = (LinearLayout) findViewById(R.id.b_classify_ll);
        mlist = new ArrayList<>();
        expendlist = new ArrayList<>();
        incomelist = new ArrayList<>();
        MAP = new HashMap<>();
        hepler = new MybookkeepingDbHepler(this);
        database = hepler.getWritableDatabase();
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        setNavigationbar();//设置导航栏的信息
        setDatabase();//添加数据库
    }

    /**
     * 添加数据库
     */
    private void setDatabase() {
        List<RootType> rootTypeList = G.type.ROOT_TYPES;
        //判断数据库里是否有数据，有数据不执行此操作。没数据添加数据
        if (database.rawQuery("select * from user", null).getCount() == 0) {
            //插入支出的常用数据
            hepler.insert(database, 1000000, -1, "常用", 0, R.drawable.ic_accident, 0);
            //插入所有支出的数据信息
            for (int i = 0; i < rootTypeList.size() - 2; i++) {
                RootType roottype = rootTypeList.get(i);
                hepler.insert(database, roottype.getCode(), -1, roottype.getName(), roottype.getType(), roottype.getIcon(), 0);
            }
            //插入收入的常用数据
            hepler.insert(database, 6000000, -1, "常用", 1, R.drawable.ic_wage, 1);
            for (int i = rootTypeList.size() - 2; i < rootTypeList.size(); i++) {
                //插入所有收入的数据信息
                RootType roottype = rootTypeList.get(i);
                hepler.insert(database, roottype.getCode(), -1, roottype.getName(), roottype.getType(), roottype.getIcon(), 0);
            }
            // Toast.makeText(getApplicationContext(), "父类添加成功！", Toast.LENGTH_LONG).show();
            //添加常用子类
            hepler.insert(database, 1011000+1, 1000000, "餐饮-早午晚餐", 0, R.drawable.ic_meals, 1);
            hepler.insert(database, 1012000+1, 1000000, "餐饮-水果零食", 0, R.drawable.ic_fruits, 1);
            hepler.insert(database,6011000+1,6000000,"职业收入-工资收入",1,R.drawable.ic_wage,1);
            List<ChildType> childTypeList = G.type.CHILD_TYPES;
            for (int i = 0; i < childTypeList.size(); i++) {
                ChildType childtype = childTypeList.get(i);
                if (i==0||i==1 || childTypeList.get(i).getCode()==6011000){
                    hepler.insert(database, childtype.getCode(), childtype.getRootCode(), childtype.getName(), childtype.getType(), childtype.getIcon(), 1);
                }
                else {
                    hepler.insert(database, childtype.getCode(), childtype.getRootCode(), childtype.getName(), childtype.getType(), childtype.getIcon(), 0);
                }
            }
//            Toast.makeText(getApplicationContext(), "子类添加成功！", Toast.LENGTH_LONG).show();
        }
        //    database.execSQL("delete from user ");
    }
    /**
     * 设置所有数据,从数据库中获取数据，并且按type分类成收入支出两个List<List<ChildType>>类型的数据
     */
    private  void setAlldata(SQLiteDatabase database){
        rootTypeLists = new ArrayList<RootType>();
        rootTypeList = new ArrayList<RootType>();
        incomeroottypes = new ArrayList<RootType>();
        List<List<ChildType>> Expendlist = new ArrayList<List<ChildType>>();
        List<List<ChildType>> Incoemlist = new ArrayList<List<ChildType>>();
        Cursor cursor = database.rawQuery("select distinct * from user where rcode=-1", null);
        while (cursor.moveToNext()) {
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            int code = cursor.getInt(cursor.getColumnIndex("code"));
            RootType roottype = new RootType();
            roottype.setCode(cursor.getInt(cursor.getColumnIndex("code")));
            roottype.setName(cursor.getString(cursor.getColumnIndex("title")));
            roottype.setIcon(cursor.getInt(cursor.getColumnIndex("image")));
            roottype.setType(type);
            roottype.setFlag(cursor.getInt(cursor.getColumnIndex("flag")));
            Cursor c = database.rawQuery("select distinct * from user where rcode !=-1", null);
            List<ChildType> childList = new ArrayList<ChildType>();
            while (c.moveToNext()) {
                if (code == c.getInt(c.getColumnIndex("rcode"))) {
                    ChildType childtype = new ChildType();
                    childtype.setRootCode(c.getInt(c.getColumnIndex("rcode")));
                    childtype.setCode(c.getInt(c.getColumnIndex("code")));
                    childtype.setName(c.getString(c.getColumnIndex("title")));
                    childtype.setType(c.getInt(c.getColumnIndex("type")));
                    childtype.setFlag(c.getInt(c.getColumnIndex("flag")));
                    childtype.setIcon(c.getInt(c.getColumnIndex("image")));
                    childList.add(childtype);
                }
            }
            roottype.setChildList(childList);
            rootTypeLists.add(roottype);
            if (type == 0) {
                rootTypeList.add(roottype);
            } else {
                incomeroottypes.add(roottype);
            }
            c.close();
        }
        cursor.close();
        if (type == EXPEND) {
            for (RootType roottype : rootTypeList) {
                rootTypes = rootTypeList;
                List<ChildType> ExpendchildTypes = roottype.getChildList();
                Expendlist.add(ExpendchildTypes);
                list = Expendlist;
            }
        } else if (type == INCOME) {
            for (RootType roottype : incomeroottypes) {
                rootTypes = incomeroottypes;
                List<ChildType> incomechildtype = roottype.getChildList();
                Incoemlist.add(incomechildtype);
                list = Incoemlist;
            }
        }
    }

    /**
     * 设置相应的数据
     */
    private void setData() {
        setAlldata(database);
        rootAdapter = new ClassfiyRootAdapter(ClasstifyActivity.this, rootTypes);
        lv_root.setAdapter(rootAdapter);
        selectDefult();
        lv_root.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {
                groupposition = position;
                rootAdapter.SelectionPosition(position);
                rootAdapter.notifyDataSetInvalidated();
                childAdapter = new CommonAdapter<ChildType>(ClasstifyActivity.this, list.get(groupposition), R.layout.item_bookeepingclassify_child) {
                    @Override
                    public void convert(ViewHolder viewHolder, final ChildType item, final int childposition) {
                        ClasstifyActivity.this.childposition = childposition;
                        viewHolder.setText(R.id.tv_child_name, item.getName());
                        viewHolder.setTextColor(R.id.tv_child_name, getResources().getColor(R.color.black));
                        viewHolder.setImageResource(R.id.ib_collection, R.drawable.ic_attention_off);
                        final ImageButton ib_connection = viewHolder.getView(R.id.ib_collection);
                        //判断是否为第一个常用数据，根据数据判断不同操作
                        if (groupposition != 0) {
                            if (mlist.get(groupposition).get(childposition) == true) {
                                ib_connection.setImageResource(R.drawable.ic_attention_on);
                            } else {
                                ib_connection.setImageResource(R.drawable.ic_attention_off);
                            }
                            ib_connection.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RootType rootType = G.type.getRootTypeByCode(item.getRootCode());
                                    //判断是否收藏，如果收藏先把标记为不可收藏，删除数据
                                    if (mlist.get(groupposition).get(childposition) == true) {
                                        //更换取消收藏图标
                                        ib_connection.setImageResource(R.drawable.ic_attention_off);
                                        //删除数据
                                        hepler.delete(database, rootType.getName() + "-" + item.getName());
                                        //更新标记
                                        hepler.update(database, item.getCode(), 0);
                                        //更新当前位置为取消收藏
                                        updateList(0, groupposition, childposition);
                                        Toast.makeText(getApplicationContext(), "取消成功", Toast.LENGTH_LONG).show();
                                    } else {
                                        //更新标记
                                        hepler.update(database, item.getCode(), 1);
                                        //更换收藏图标
                                        ib_connection.setImageResource(R.drawable.ic_attention_on);
                                        //添加数据
                                        if (type == 0) {
                                            hepler.insert(database, item.getCode() + 1, 1000000, rootType.getName() + "-" + item.getName(), 0, item.getIcon(), 1);
                                            Log.i("item_code", item.getCode() + "");
                                        } else {
                                            hepler.insert(database, item.getCode() + 1, 6000000, rootType.getName() + "-" + item.getName(), 1, item.getIcon(), 1);
                                        }
                                        //更新当前位置为收藏
                                        updateList(1, groupposition, childposition);
                                        Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_LONG).show();
                                    }
                                    //此时的数据库改变，重新刷新数据库  改变数据库
                                    handler.sendEmptyMessage(0x00);
                                }
                            });
                        } else if (groupposition == 0) {
                            ib_connection.setImageResource(R.drawable.ic_attention_on);
                            ib_connection.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    for (int i = 0; i < list.get(0).size(); i++) {
                                        if (i == childposition) {
                                            hepler.delete(database, item.getName());//删除当前位置的数据
                                            hepler.update(database, item.getCode() - 1, 0);//设置单项为取消关注
                                            handler.sendEmptyMessage(0x00);
                                            ChildType childType = G.type.getChildTypeByCode(item.getCode() - 1);
                                            RootType rootType = G.type.getRootTypeByCode(childType.getRootCode());
                                            //     Log.i("11111", item.getCode() - 1 + "====" + childType.getRootCode());
                                            if (childType.getType() == 0) {
                                                updateList(0, rootType.getPosition() + 1, childType.getPosition());//更新当前位置为收藏
                                            } else {
                                                if (rootType.getCode() == 6010000) {
                                                    updateList(0, 1, childType.getPosition());//更新当前位置为收藏
                                                } else {
                                                    updateList(0, 2, childType.getPosition());//更新当前位置为收藏
                                                }
                                            }
                                            setData();
                                            Toast.makeText(getApplicationContext(), "取消成功", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });
                        }
                    }
                };
                lv_submit.setAdapter(childAdapter);
                setSublistitemclick(groupposition);
            }
        });
    }
    /**
     * 更新数据
     */
    private  void updateList(int collection,int groupposition,int childposition){
        Map<Integer, Boolean> map = mlist.get(groupposition);
        if (collection==0){
            for (int i=0;i<map.size();i++){
                if (i==childposition){
                    map.put(i,false);
                }
            }
        }
        else {
            for (int i=0;i<map.size();i++){
                if (i==childposition){
                    map.put(i,true);
                }
            }
        }
        mlist.set(groupposition, map);
        rootAdapter.notifyDataSetChanged();
        childAdapter.notifyDataSetChanged();
    }
    private void setNavigationbar() {
        bar = (NavigationBar) findViewById(R.id.tc_nacigation);
        bar.setTitle("选择类别");
        bar.setLeftButton(null, R.drawable.ic_arrow_lift);
        bar.setNavigationBarClickListener(new NavigationBar.OnNavigationBarClickListener() {
            @Override
            public void OnNavigationBarClick(int tag) {
                if (tag == NavigationBar.TAG_LEFT_BUTTON) {
                    finish();
                }
            }
        });
    }
    /**
     * 设置是否收藏的图标，定义收藏图片，数据库中flag=1表示收藏图标，数据库中flag=0表示取消收藏图标
     */
    private  void getCollection(SQLiteDatabase database){
        Cursor cursor1 = database.rawQuery("select code ,type from user where rcode==-1", null);
        while (cursor1.moveToNext()) {
            int rcode = cursor1.getInt(cursor1.getColumnIndex("code"));
            int type =cursor1.getInt(cursor1.getColumnIndex("type"));
            Cursor cursor = database.rawQuery("select flag ,title ,code from user where rcode==" + rcode, null);
            int i = 0;
            Map<Integer, Boolean> map = new HashMap<>();
            while (cursor.moveToNext()) {
                int flag = cursor.getInt(cursor.getColumnIndex("flag"));
                if (flag == 1) {
                    map.put(i, true);
                } else {
                    map.put(i, false);
                }
                i++;
            }
            if (type==0){
                expendlist.add(map);
            }
            else {
                incomelist.add(map);
            }
            cursor.close();
        }
        if (type==0){
            mlist = expendlist;
        }
        else if (type==1){
            mlist = incomelist;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setAlldata(database);//更新数据库
        getCollection(database);//改变收藏与取消图标颜色
        setData();//设置childAdapter中的内容
        rootAdapter.notifyDataSetChanged();
        childAdapter.notifyDataSetChanged();
    }

    /**
     * 设置默认的数据，刚刚进入页面时初始化的所有数据
     */
    private void selectDefult() {
        setAlldata(database);
        rootAdapter.SelectionPosition(0);
        rootAdapter.notifyDataSetInvalidated();
        groupposition = 0;
        childAdapter = new CommonAdapter<ChildType>(ClasstifyActivity.this, list.get(0), R.layout.item_bookeepingclassify_child) {
            @Override
            public void convert(ViewHolder viewHolder,final ChildType item,final int childposition) {
                viewHolder.setText(R.id.tv_child_name, item.getName());
                viewHolder.setTextColor(R.id.tv_child_name, getResources().getColor(R.color.black));
                viewHolder.setImageResource(R.id.ib_collection, R.drawable.ic_attention_off);
                final ImageButton ib_connection = viewHolder.getView(R.id.ib_collection);
                if (groupposition==0){
                    ib_connection.setImageResource(R.drawable.ic_attention_on);
                    ib_connection.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < list.get(0).size(); i++) {
                                if (i == childposition) {
                                    hepler.delete(database, item.getName());//删除当前位置的数据
                                    hepler.update(database, item.getCode() - 1, 0);//设置单项为取消关注
                                    handler.sendEmptyMessage(0x00);
                                    ChildType childType =   G.type.getChildTypeByCode(item.getCode()-1);
                                    RootType rootType = G.type.getRootTypeByCode(childType.getRootCode());
                                    if (childType.getType()==0){
                                        updateList(0, rootType.getPosition() + 1, childType.getPosition());//更新当前位置为收藏
                                    }else {
                                        if (rootType.getCode()==6010000){
                                            updateList(0,1, childType.getPosition());//更新当前位置为收藏
                                        }
                                        else {
                                            updateList(0, 2, childType.getPosition());//更新当前位置为收藏
                                        }
                                    }
                                    setData();
                                    Toast.makeText(getApplicationContext(), "取消成功", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        };
        lv_submit.setAdapter(childAdapter);
        setSublistitemclick(0);
    }

    /**
     * 选中相应的的数据后，跳转进入前一个页面的操作
     * @param groupposition
     */
    private  void  setSublistitemclick(final int groupposition){
        lv_submit.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                childposition = position;
                for (int i = 0; i < list.get(groupposition).size(); i++) {
                    ChildType childType = list.get(groupposition).get(i);
                    if (i == position) {
                        Log.i("code", childType.getCode() + "");
                        Intent intent = new Intent();
                        intent.setClass(ClasstifyActivity.this, VoiceAccountActivity.class);
                        intent.putExtra("type_image", childType.getIcon());
                        intent.putExtra("type_name", childType.getName());
                        intent.putExtra("type_type", type);
                        intent.putExtra("type_code", childType.getCode());
                        intent.putExtra("type_rcode", childType.getRootCode());
                        intent.putExtra("source", VoiceAccountActivity.SOURCE_TYPE_ADD);
                        setResult(200, intent);
                        finish();
                    }
                }
            }
        });
    }
    //添加分类
    private void add() {
       /* ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new MyDialog(BookkeepingClasstifyActivity.this, R.style.CustomDialog, "添加类别", "请输入类别",
                        new MyDialog.OnCustomDialogListener() {
                            @Override
                            public void back(String ed_text) {
                                int rootcode;
                                if (!ed_text.equals("")) {
                                    if (type == 0) {
                                        rootcode = rootTypeList.get(groupposition).getCode();
                                    } else {
                                        rootcode = incomeroottypes.get(groupposition).getCode();
                                        Log.i("rocde", rootcode + "");
                                    }
                                    RootType rootType = G.type.getRootTypeByCode(rootcode);
                                    switch (rootcode) {
                                        case 1000000:
                                            hepler.insert(database, 1000000 + childposition, rootcode, ed_text, 0, R.drawable.meals, 0);
                                            break;
                                        case 6000000:
                                            hepler.insert(database, 6000000 + childposition, rootcode, ed_text, 1, R.drawable.wage, 0);
                                            break;
                                        default:
                                            hepler.insert(database, childTypes.get(childposition).getCode() + childposition, childTypes.get(childposition).getRootCode(), ed_text, rootType.getType(), R.drawable.amounts, 0);
                                            break;
                                    }
                                    dialog.dismiss();
                                    handler.sendEmptyMessage(0x11);
                                    rootAdapter.notifyDataSetInvalidated();
                                    subAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(getApplicationContext(), "数据不能为空！！", Toast.LENGTH_LONG).show();
                                }
                            }

                        });

                dialog.show();

            }
        });*/
    }

}