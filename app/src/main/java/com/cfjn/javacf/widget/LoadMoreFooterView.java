package com.cfjn.javacf.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cfjn.javacf.R;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreUIHandler;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 下拉刷新
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class LoadMoreFooterView extends RelativeLayout implements LoadMoreUIHandler {
    private ImageView loadingImage;
    private TextView loadText;
    private ProgressBar loadProBar;

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoadMoreFooterView(Context context) {
        this(context, null);
        setupViews();
    }

    private void setupViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.load_more, this);
//        loadingImage = (ImageView) findViewById(R.id.loading_progress_icon);
        loadText = (TextView) findViewById(R.id.loadstate_tv);
        loadProBar= (ProgressBar) findViewById(R.id.foot_progressbar);
    }

    @Override
    public void onLoading(LoadMoreContainer container) {
        setVisibility(VISIBLE);
//        loadingImage.setVisibility(VISIBLE);
        loadText.setText("加载中...");
//        final RotateAnimation anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        anim.setInterpolator(new LinearInterpolator());
//        anim.setRepeatCount(-1);
//        anim.setDuration(1000);
//        loadingImage.setImageResource(R.drawable.progress_loading2);
//        loadingImage.startAnimation(anim);
        loadProBar.setVisibility(VISIBLE);
    }

    @Override
    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore) {
        loadProBar.setVisibility(GONE);
        if (!hasMore) {
            if (empty) {
                loadText.setText("加载成功");
            } else {
                setVisibility(GONE);
                loadText.setText("加载失败");
            }
//            loadingImage.setVisibility(INVISIBLE)
        } else {
            loadText.setText("下拉加载更多");
            setVisibility(VISIBLE);
        }
    }

    @Override
    public void onWaitToLoadMore(LoadMoreContainer container) {
        setVisibility(VISIBLE);
        loadText.setText("下拉加载更多");
//        loadingImage.setVisibility(INVISIBLE);
    }

}
