package com.cfjn.javacf.modle;

/**
 * Created by Administrator on 2015/12/21.
 */

/**
 * 记账类型
 *
 * @author wh
 */
public class Type {
    /**
     * 类别编号
     */
    private int code;
    /**
     * 类别名称
     */
    private String name;
    /**
     * 类型：0 支出 1 收入
     */
    private int type;
    /**
     * 图片
     */
    private int icon;
    /**
     * 文字颜色
     */
    private int color;
    /**
     * 类别位置
     */
    private int position;

    /**
     * 是否被选中
     */
    private boolean isChecked;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
    /**
     * 收藏
     */
    private  int flag;

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

