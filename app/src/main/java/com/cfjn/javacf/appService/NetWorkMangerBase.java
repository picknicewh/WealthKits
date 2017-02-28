package com.cfjn.javacf.appService;

import android.content.Context;

import com.cfjn.javacf.shumi.ShumiAsstesDate;
import com.cfjn.javacf.shumi.ShuMiBankDate;
import com.cfjn.javacf.shumi.ShuMiTradingDate;
import com.cfjn.javacf.util.G;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 数米提交数据广播提交完数据后将之前存储的信息清空
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class NetWorkMangerBase {

    private ShuMiTradingDate shuMiTradingDate;
    private ShuMiBankDate shuMiBankDate;
    private ShumiAsstesDate asstesDate;

    private String SMTeadingdate;
    private String SMBankDate;
    private String SMAsstes;

    public NetWorkMangerBase(Context context,int type) {
        shuMiTradingDate=new ShuMiTradingDate(context);
        shuMiBankDate=new ShuMiBankDate(context);
        asstesDate=new ShumiAsstesDate(context);

        SMTeadingdate=shuMiTradingDate.getSmTradingDate();
        SMBankDate=shuMiTradingDate.getSmTradingDate();
        SMAsstes=asstesDate.getShumiAsstesDate();

        switch (type) {
            case 0:
                if (!G.isEmteny(SMAsstes)) {
                    asstesDate.clean();
                }
                break;
            case 1:
                if (!G.isEmteny(SMTeadingdate)) {
                    shuMiTradingDate.clean();
                }
                break;
            case 2:
                if (!G.isEmteny(SMBankDate)) {
                    shuMiBankDate.clean();
                }
                break;
        }
    }
}
