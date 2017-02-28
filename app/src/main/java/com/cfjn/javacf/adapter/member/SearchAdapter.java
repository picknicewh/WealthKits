package com.cfjn.javacf.adapter.member;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfjn.javacf.R;
import com.cfjn.javacf.base.UserInfo;
import com.cfjn.javacf.modle.ImgType;
import com.cfjn.javacf.modle.AttentionVo;
import com.cfjn.javacf.util.ApiUri;
import com.cfjn.javacf.util.G;
import com.cfjn.javacf.util.OKHttpListener;
import com.cfjn.javacf.util.OkHttpUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 基金搜索适配器
 * 版本说明：代码规范整改
 * 附加注释： 每个关注状态当一个对象储存在 showType 中
 * 主要接口： 提交关注状态
 */
public class SearchAdapter extends BaseAdapter implements OKHttpListener {
    private Context context;
    private List<Map<String, String>> mlist;
    private ViewHolder holder;
    private List<ImgType> showType;
    private int index = -1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x11) {
                Bundle bunld = msg.getData();
                G.showToast(context, bunld.getString("ResultDesc"));
                notifyDataSetChanged();
            } else if (msg.what == 0x12) {
                G.showToast(context, "数据提交失败，请再次重试！");
            } else if (msg.what == 0x13) {
                G.showToast(context, "网络不稳定，亲请稍后再试！");
            }
        }
    };

    public SearchAdapter(List<Map<String, String>> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
        showType = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search, null);
            new ViewHolder(convertView);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tvFundName.setText(mlist.get(position).get("fundNameAbbr"));
        holder.tvFundCode.setText(mlist.get(position).get("fundCode"));
        //由于每个关注的值是一个map对像传过来的，不好控制值，因此 每个关注的值 把他当作一个对象储存起来，每个对象储存一次。
//        if (index < position) {
//            index = position;
//            String show = mlist.get(position).get("attention");
//            ImgType imgType = new ImgType();
//            imgType.setShowType(show);
//            showType.add(imgType);
//        }
//        G.log(showType.get(position).getShowType() + "--------------showType---------------" + position);
        //根据储存的值进行判断关注状态
//        if (!TextUtils.isEmpty(showType.get(position).getShowType()) && showType.get(position).getShowType().equals("true")) {
//            holder.ivFundType.setImageResource(R.drawable.ic_attention_on);
//        } else {
//            holder.ivFundType.setImageResource(R.drawable.ic_attention_off);
//        }
        //设置关注的监听事件
//        holder.ivFundType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(G.isEmteny(showType.get(position).getShowType())) return;
//
//                if (showType.get(position).getShowType().equals("true")) {
//                    setAttenType(position, "2");
//                    showType.get(position).setShowType("false");
//                } else {
//                    setAttenType(position, "1");
//                    showType.get(position).setShowType("true");
//                }
//            }
//        });
        if (mlist.get(position).get("attention").equals("true")) {
            Collections.emptyList();
            holder.ivFundType.setImageResource(R.drawable.ic_attention_on);
        } else {
            holder.ivFundType.setImageResource(R.drawable.ic_attention_off);
        }

        holder.ivFundType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlist.get(position).get("attention").equals("true")) {
                    setAttenType(position, "2");
                    mlist.get(position).put("attention","false");
                } else {
                    setAttenType(position, "1");
                    mlist.get(position).put("attention","true");
                }
            }
        });

        return convertView;
    }

    /**
     * 设置关注状态
     *
     * @param position 基金位置
     * @param type     是关注还是取消
     */
    private void setAttenType(final int position, final String type) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("secretKey", UserInfo.getInstance(context).getSecretKey());
        map.put("token", UserInfo.getInstance(context).getToken());
        map.put("code", mlist.get(position).get("fundCode"));
        map.put("yesNo", type);
        map.put("fundType", mlist.get(position).get("fundType"));
        map.put("fundName", mlist.get(position).get("fundName"));
        OkHttpUtil.sendPost(ApiUri.ATTENTION, map, this);
    }

    @Override
    public void onSuccess(String uri, String result) {
        if (uri.equals(ApiUri.ATTENTION)) {
            Gson gson = new Gson();
            AttentionVo attentionVo = gson.fromJson(result, AttentionVo.class);
            if (attentionVo.getResultCode().equals("0")) {
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("ResultDesc", attentionVo.getResultDesc());
                msg.setData(bundle);
                msg.what = 0x11;
                //将网络数据传给handle
                handler.sendMessage(msg);
            } else {
                handler.sendEmptyMessage(0x12);
            }
        }
    }

    @Override
    public void onError(String uri, String error) {
        if (uri.equals(ApiUri.ATTENTION)) {
            handler.sendEmptyMessage(0x13);
        }
    }

    /**
     * 当搜索值被重置时  需要把储存的值进行初始化更新
     */
    public void upadater() {
        index = -1;
        showType.clear();
    }

    class ViewHolder {
        /**
         * 基金名称
         */
        @Bind(R.id.tv_fund_name)
        TextView tvFundName;
        /**
         * 基金代码
         */
        @Bind(R.id.tv_fund_code)
        TextView tvFundCode;
        /**
         * 关注状态
         */
        @Bind(R.id.iv_fund_type)
        ImageView ivFundType;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            if (G.isEmteny(new UserInfo(context).getLoginName())) {
                ivFundType.setVisibility(View.GONE);
            }
            view.setTag(this);
        }
    }
}
