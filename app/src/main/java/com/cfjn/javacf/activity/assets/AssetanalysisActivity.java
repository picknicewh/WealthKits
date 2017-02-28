package com.cfjn.javacf.activity.assets;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.adapter.CommonAdapter;
import com.cfjn.javacf.adapter.ViewHolder;
import com.cfjn.javacf.modle.MyAnalysisListVo;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.NetWorkUitls;
import com.cfjn.javacf.util.TitleValueColorEntity;
import com.cfjn.javacf.widget.PieChartView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  作者： zll
 *  时间： 2016-5-31
 *  名称： 资产分析
 *  版本说明：代码规范整改
 *  附加注释：PieChartView实现饼图
 *  主要接口：暂无
 */
public class AssetanalysisActivity extends Activity {
    /**
     * 饼图
     */
    @Bind(R.id.piechar_assets)
    PieChartView piecharAssets;
    /***
     * 总资产
     */
    @Bind(R.id.tv_total_assets)
    TextView tvTotalAssets;
    /**
     * 资产分布
     */
    @Bind(R.id.lv_pie)
    ListView lvPie;
    /**
     * 资产页
     */
    @Bind(R.id.ll_pie)
    LinearLayout llPie;
    /**
     * 无网络或者没有资产页面
     */
    private LinearLayout notValue;
    /**
     * 负责储存基金名称个对应的颜色
     */
    public static Map<String, String> MAP;
    /**
     * 饼图数据
     */
    public static List<MyAnalysisListVo> listVos;
    public  String totalAssets;
    private DecimalFormat format;
    private CommonAdapter commonAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_analysis);
        ButterKnife.bind(this);
        initializeView();
        initializeMode();
        initializeControl();
    }

    public void initializeView() {
        notValue = (LinearLayout) findViewById(R.id.notValue);
    }

    private void initializeMode(){
        format = new DecimalFormat("#0.00");
        listVos = (List<MyAnalysisListVo>) getIntent().getSerializableExtra("analysis");
        MAP = new HashMap<>();
        totalAssets = MyAssetsActivity.totalAssets;
    }

    private void initializeControl(){
        if (null == listVos || listVos.size() == 0||G.isEmteny(totalAssets)|| totalAssets.equals("0.0")) {
            notValueShow(true);
            piecharAssets.setRun(false);
            return;
        }
        notValueShow(false);
        setPiePage();
    }

    /**
     * 绘制饼图和下面财产颜色提示
     */
    private void setPiePage() {
        final List<Map<String,String>>assetsValue=new ArrayList<>();
        if (listVos.size() == 0) {
            llPie.setVisibility(View.GONE);
        } else {
//            float f[]={12,10,30,56,12};
            List<TitleValueColorEntity> data = new ArrayList<>();//饼图所需的数据集合
            TitleValueColorEntity entity;    //饼图所需的数据实体
            int i=0;
            for (int j = 0; j < listVos.size(); j++) {
                MyAnalysisListVo analysisListVo = listVos.get(j);
                //金额
                double value = Double.parseDouble(analysisListVo.getPercentage()) / Double.parseDouble(totalAssets);
                //去掉小于0的资产  因为在华为的手机上如果出现小于0的支持饼图会变形  所以将小于0的支持去掉
                if(value>0){
                    //名字
                    String title = analysisListVo.getFundName();
                    //把资产和资产名称保存起来
                    Map<String,String>map=new HashMap<>();
                    map.put("value",value+"");
                    map.put("title",title);
                    assetsValue.add(map);
                    //创建entity
                    entity = new TitleValueColorEntity(title, Float.parseFloat(analysisListVo.getPercentage()), G.PieColor.color2[i]);
                    i++;
                    G.log((float)value+"----------------"+analysisListVo.getPercentage()+"-------------"+totalAssets);
                    data.add(entity);
                }

            }
            piecharAssets.setData(data);
            tvTotalAssets.setText(format.format(Double.parseDouble(totalAssets)) + "元");
            piecharAssets.setRun(false);

            commonAdapter=new CommonAdapter(this,assetsValue,R.layout.assets_analysis_explain) {
                @Override
                public void convert(ViewHolder helper, Object item, int position) {
                    helper.setBackgroundColor(R.id.v_color,G.PieColor.color2[position]);
                    helper.setText(R.id.tv_fund_name,
                            assetsValue.get(position).get("title") +
                                    "(" + format.format(Double.parseDouble(assetsValue.get(position).get("value")) * 100) + "%)");
                }
            };
            lvPie.setAdapter(commonAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        piecharAssets.setRun(false);
    }

    private void notValueShow(boolean isNotValue) {
        TextView tview;
        ImageView imageView;
        if (isNotValue) {
            notValue.setVisibility(View.VISIBLE);
            if(NetWorkUitls.isNetworkAvailable(this)){
                imageView = (ImageView) notValue.findViewById(R.id.img_notNetwork);
                tview = (TextView) notValue.findViewById(R.id.tv_notNetwork);
                imageView.setImageResource(R.drawable.ic_mammon);
                tview.setText("您目前没有任何资产，快去基金超市申购吧！");
            }
        } else {
            notValue.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.ib_back)
    public void onClick() {
        finish();
    }
}
