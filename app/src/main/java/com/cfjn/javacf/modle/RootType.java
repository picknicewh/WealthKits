package com.cfjn.javacf.modle;

import java.util.List;

/**
 * Created by Administrator on 2015/12/21.
 */
public class RootType extends Type {
    private List<ChildType> childList;

    public List<ChildType> getChildList() {
        return childList;
    }

    public void setChildList(List<ChildType> childList) {
        this.childList = childList;
    }

}
