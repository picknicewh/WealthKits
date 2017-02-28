package com.cfjn.javacf.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 基金申购时候选择柜台支付单选框状态
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class RadioButton  extends LinearLayout {
    private Context context;
    private ImageView imageView;
    private TextView textView;
    private TextView expandable_text;
    private ImageButton expand_collapse;
    private boolean isExpend=false;
//    private ExpandableTextView exp_text;

    private boolean isAttion=true;
    private int index = 0;
    private int id = 0;// 判断是否选中

    private RadioButton tempRadioButton;// 模版用于保存上次点击的对象

    private int state[] = { R.drawable.ic_choose_off, R.drawable.ic_choose_on};

    /***
     * 改变图片
     */
    public void ChageImage() {
//        index++;
//        id = index % 2;// 获取图片id
//        imageView.setImageResource(state[id]);

       isAttion=isAttion?false:true;
       imageView.setImageResource(isAttion?state[0]:state[1]);
    }

    /***
     * 设置文本
     *
     * @param text
     */
    public void setText(String text) {
        textView.setText(text);
    }

    public String getText() {
        return id == 0 ? "" : textView.getText().toString();

    }
    public void setText_(String text_) {
        expandable_text.setText(text_);
    }

    public String getText_() {
        return id == 0 ? "" : expandable_text.getText().toString();

    }
    public RadioButton(Context context) {
        this(context, null);

    }

    public RadioButton(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view=LayoutInflater.from(context).inflate(R.layout.item_radio, this, true);
        imageView = (ImageView)view.findViewById(R.id.image_view);
        textView = (TextView)view.findViewById(R.id.radio_text);
        expandable_text = (TextView)view.findViewById(R.id.expandable_text);
        expand_collapse= (ImageButton)view.findViewById(R.id.expand_collapse);
        expand_collapse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpend) {
                    expand_collapse.setImageResource(R.drawable.ic_arrow_up);
                    expandable_text.setMinLines(0);
                    expandable_text.setMaxLines(Integer.MAX_VALUE);
                    isExpend=true;
                } else {
                    expand_collapse.setImageResource(R.drawable.ic_arrow_down_p);
                    expandable_text.setLines(2);
                    isExpend=false;
                }
            }
        });
//        exp_text= (ExpandableTextView) findViewById(R.id.expand_text_view);
    }
}
