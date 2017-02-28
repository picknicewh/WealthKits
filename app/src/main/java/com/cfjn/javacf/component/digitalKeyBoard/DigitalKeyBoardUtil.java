package com.cfjn.javacf.component.digitalKeyBoard;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.cfjn.javacf.R;
import com.cfjn.javacf.plugin.SoftKeyBoardPlugin;


/**
 * <p>数字键盘上的触发和数字键盘的随机算法/p>
 *
 * @author: 罗成毅
 * @corporation: 天风
 * @date: 15-11-25
 * @version: 1.1
 */
public class DigitalKeyBoardUtil implements View.OnClickListener
{
    
    private StringBuilder mInputBuider;  //输入缓冲区
                                          
    private int           mMaxLength = 6; //最大输入长度
                                          
    private WindowManager mWManager;
    
    private View          mKeyBoard;

    private int [] keyword = new int[]{0,1,2,3,4,5,6,7,8,9};
    
    public DigitalKeyBoardUtil(WindowManager windowManager, View keyboard, int maxLength, String oldPwd)
    {
        this.mWManager = windowManager;
        this.mMaxLength = maxLength;
        this.mKeyBoard = keyboard;
        this.mInputBuider = new StringBuilder(maxLength);
        this.mInputBuider.append(oldPwd);
        init();
    }
    
    public void setPwd(int maxLength, String oldPwd){
    	this.mMaxLength = maxLength;
    	this.mInputBuider = new StringBuilder(maxLength);
        this.mInputBuider.append(oldPwd);
    }
    
    public void init()
    {
        randomKeyword();
        setListener(mKeyBoard);
    }

    /**
     * 随机键位值
     */
    public void randomKeyword(){
        for (int i=0;i<5;i++){
            int keywords = keyword[i];
            int randomInt = (int)(Math.random()*10);
            randomInt = randomInt>=10?9:randomInt;
            int randomkeywords = keyword[randomInt];
            /** 开始替换 **/
            keyword[i] = randomkeywords;
            keyword[randomInt]=keywords;
        }
        ((Button)(mKeyBoard.findViewById(R.id.btn_digital_0))).setText(""+keyword[0]);
        ((Button)(mKeyBoard.findViewById(R.id.btn_digital_1))).setText(""+keyword[1]);
        ((Button)(mKeyBoard.findViewById(R.id.btn_digital_2))).setText(""+keyword[2]);
        ((Button)(mKeyBoard.findViewById(R.id.btn_digital_3))).setText(""+keyword[3]);
        ((Button)(mKeyBoard.findViewById(R.id.btn_digital_4))).setText(""+keyword[4]);
        ((Button)(mKeyBoard.findViewById(R.id.btn_digital_5))).setText(""+keyword[5]);
        ((Button)(mKeyBoard.findViewById(R.id.btn_digital_6))).setText(""+keyword[6]);
        ((Button)(mKeyBoard.findViewById(R.id.btn_digital_7))).setText(""+keyword[7]);
        ((Button)(mKeyBoard.findViewById(R.id.btn_digital_8))).setText(""+keyword[8]);
        ((Button)(mKeyBoard.findViewById(R.id.btn_digital_9))).setText(""+keyword[9]);
    }

    /**
     * 设定所有监听
     * @param keyboard
     */
    private void setListener(View keyboard)
    {
        (keyboard.findViewById(R.id.btn_digital_0)).setOnClickListener(this);
        (keyboard.findViewById(R.id.btn_digital_1)).setOnClickListener(this);
        (keyboard.findViewById(R.id.btn_digital_2)).setOnClickListener(this);
        (keyboard.findViewById(R.id.btn_digital_3)).setOnClickListener(this);
        (keyboard.findViewById(R.id.btn_digital_4)).setOnClickListener(this);
        (keyboard.findViewById(R.id.btn_digital_5)).setOnClickListener(this);
        (keyboard.findViewById(R.id.btn_digital_6)).setOnClickListener(this);
        (keyboard.findViewById(R.id.btn_digital_7)).setOnClickListener(this);
        (keyboard.findViewById(R.id.btn_digital_8)).setOnClickListener(this);
        (keyboard.findViewById(R.id.btn_digital_9)).setOnClickListener(this);
        (keyboard.findViewById(R.id.ibtn_digital_del)).setOnClickListener(this);
        (keyboard.findViewById(R.id.ibtn_digital_done)).setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        if ( id == R.id.btn_digital_0 )
        {
            if ( mInputBuider.length() < mMaxLength )
            {
                mInputBuider.append(keyword[0]);
            }
        }
        else if ( id == R.id.btn_digital_1 )
        {
            if ( mInputBuider.length() < mMaxLength )
            {
                mInputBuider.append(keyword[1]);
            }
        }
        else if ( id == R.id.btn_digital_2 )
        {
            if ( mInputBuider.length() < mMaxLength )
            {
                mInputBuider.append(keyword[2]);
            }
        }
        
        else if ( id == R.id.btn_digital_3 )
        {
            if ( mInputBuider.length() < mMaxLength )
            {
                mInputBuider.append(keyword[3]);
            }
        }
        else if ( id == R.id.btn_digital_4 )
        {
            if ( mInputBuider.length() < mMaxLength )
            {
                mInputBuider.append(keyword[4]);
            }
        }
        
        else if ( id == R.id.btn_digital_5 )
        {
            if ( mInputBuider.length() < mMaxLength )
            {
                mInputBuider.append(keyword[5]);
            }
        }
        
        else if ( id == R.id.btn_digital_6 )
        {
            if ( mInputBuider.length() < mMaxLength )
            {
                mInputBuider.append(keyword[6]);
            }
        }
        
        else if ( id == R.id.btn_digital_7 )
        {
            if ( mInputBuider.length() < mMaxLength )
            {
                mInputBuider.append(keyword[7]);
            }
        }
        else if ( id == R.id.btn_digital_8 )
        {
            if ( mInputBuider.length() < mMaxLength )
            {
                mInputBuider.append(keyword[8]);
            }
        }
        else if ( id == R.id.btn_digital_9 )
        {
            if ( mInputBuider.length() < mMaxLength )
            {
                mInputBuider.append(keyword[9]);
            }
        }
        else if ( id == R.id.ibtn_digital_del )
        {
            if ( mInputBuider.length() > 0 )
            {
                mInputBuider.deleteCharAt(mInputBuider.length() - 1);
            }
        }
        
        else if ( id == R.id.ibtn_digital_done )
        {
            try
            {
                mWManager.removeView(mKeyBoard);
                SoftKeyBoardPlugin.isViewAdded = false;

                if ( null != SoftKeyBoardPlugin.sWebview )
                {
//                    SoftKeyBoardPlugin.sWebview.sendJavascript("onFinish()");
                    gl.onFinish();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        updateInput();
    }
    
    /**
     * -发送输入值给H5
     */
    private void updateInput()
    {
        if ( null != SoftKeyBoardPlugin.sWebview )
        {
            String strInput = mInputBuider.toString().trim();
            gl.onKeyDown(strInput);
        }
//        if ( null != SoftKeyBoardPlugin.sWebview )
//        {
//            String strInput = mInputBuider.toString().trim();
//            SoftKeyBoardPlugin.sWebview.sendJavascript("getInput('" + strInput + "')");
//        }
    }
    //////////////////////////////////////////////////////////////////////////
    // 监听
    //////////////////////////////////////////////////////////////////////////
    private keyboardLinstener gl;
    public void setOnKeyDown(keyboardLinstener GLinput){gl =GLinput;}
    /**
     * 监听的listener
     *
     * onEnd 当滑动位置超过最大图片下标的时候执行
     */
    public interface keyboardLinstener{
        void onKeyDown(String words);
        void onFinish();
    }
}
