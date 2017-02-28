package com.cfjn.javacf.activity.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.adapter.member.BankMangAdapter;
import com.cfjn.javacf.shumi.ShumiBankCard;
import com.cfjn.javacf.shumi.MyShumiSdkTradingHelper;
import com.cfjn.javacf.modle.UserBankcardListVo;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.GetTFMagerUrl;
import com.openhunme.cordova.activity.HMDroidGap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者： zll
 * 时间： 2016-6-3
 * 名称： 银行卡管理
 * 版本说明：代码规范整改
 * 附加注释：1.实现各个柜台添加银行卡
 *           2.松果柜台更换银行卡 并且底部天加图标和文字也相应改变
 * 主要接口：暂无
 */
public class BankMangerActivity extends Activity {
    /**
     * 柜台名称
     */
    @Bind(R.id.tv_bank_title)
    TextView tvBankTitle;
    /**
     * 银行卡列表
     */
    @Bind(R.id.lv_bank)
    ListView lvBank;

    /**
     * 底部添加银行卡布局
     */
    private RelativeLayout rlAddBank;
    /**
     * 底部添加银行卡图标
     */
    private ImageView ivAddType;
    /**
     * 底部添加银行卡文字
     */
    private TextView tvAddtypeName;

    private List<UserBankcardListVo> bankCardList;
    /**
     * 柜台类型
     */
    private int addCadeType = -1;
    /**
     * 柜台名称
     */
    private String titleName;
    private BankMangAdapter bankMangAdapter;
    private List<Map<String, String>> list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_manger);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bankCardList = (List<UserBankcardListVo>) bundle.get("userBankCardList");
        list = new ArrayList<>();
        titleName = getIntent().getStringExtra("name");
        for (int i = 0; i < bankCardList.size(); i++) {
            String cardNuber = bankCardList.get(i).getCardNumber();
            Map<String, String> map = new HashMap<>();
            map.put("cardName", bankCardList.get(i).getCardName());
            map.put("cardNumber", cardNuber);
            map.put("status", bankCardList.get(i).getInUse());
            map.put("tradeAccount", bankCardList.get(i).getTradeAccount());
            map.put("quota", bankCardList.get(i).getQuota());
            list.add(map);
        }

        bankMangAdapter = new BankMangAdapter(this, list);
        //添加底部按钮<添加银行卡>
        View v = LayoutInflater.from(this).inflate(R.layout.bankmanger_buttonview, null);
        rlAddBank = (RelativeLayout) v.findViewById(R.id.rl_add_bank);
        ivAddType = (ImageView) v.findViewById(R.id.iv_addType);
        tvAddtypeName = (TextView) v.findViewById(R.id.tv_addType_name);
        lvBank.addFooterView(v);
        lvBank.setAdapter(bankMangAdapter);
        //显示是哪个柜台
        if (titleName.equals("数米柜台")) {
            addCadeType = 0;
        } else if (titleName.equals("天风柜台")) {
            addCadeType = 1;
        } else if (titleName.equals("松果柜台")) {
            ivAddType.setImageResource(R.drawable.ic_replace);
            tvAddtypeName.setText("更换新银行卡");
            addCadeType = 2;
        }
        tvBankTitle.setText(titleName);

        lvBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //addCadeType=0表示是数米 数米点击出现解绑银行卡 其他柜台不显示
                if (addCadeType == 0) {
                    bankMangAdapter.changeImageVisable(view, position);
                }
            }
        });

        rlAddBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcard();
            }
        });
    }

    /**
     * 添加银行卡调用对应的接口或者方法
     */
    private void addcard() {
        switch (addCadeType) {
            case 0:
                //数米柜台添加银行卡
                MyShumiSdkTradingHelper.doAddBankCard(BankMangerActivity.this);
                break;
            case 1:
                //天风添加银行卡
                Intent in = new Intent(BankMangerActivity.this, HMDroidGap.class);
                in.putExtra("loadUrl", GetTFMagerUrl.TFMagerUrl(3, BankMangerActivity.this));
                startActivity(in);
                finish();
                break;
            case 2:
                //松果更换银行卡
//                Intent intent=new Intent(BankMangerActivity.this,SGRegister.class);
//                intent.putExtra("type",-1);
//                startActivity(intent);
                G.showToast(this, "敬请期待！");
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //数米银行卡信息界面消失提交数据
        if (addCadeType == 0) {
            new ShumiBankCard(this);
        }
    }

    @OnClick({R.id.ib_back, R.id.ib_add_bank})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_add_bank:
                addcard();
                break;
        }
    }
}
