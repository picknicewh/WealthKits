package com.cfjn.javacf.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Toast;

import com.cfjn.javacf.R;
import com.cfjn.javacf.modle.ChildType;
import com.cfjn.javacf.modle.RootType;
import com.cfjn.javacf.modle.TypeEntity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.util.LogUtils;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局变量
 * Created by DELL on 2015/12/1.
 */
public class G {
    /**
     * 调试信息
     */
    public static boolean DEBUG = true;
    /**
     * toast提示
     */
    private static Toast toast;
    /**
     * 是否需要登录
     */
    public static boolean isTFlogin = false;
    /**
     * 是否撤单
     */
    public static boolean ISOUTORDEER = false;
    /**
     * 是否申购
     */
    public static boolean ISPURCHASE = false;
    /**
     * 省
     */
    public static String PROVINCE = "";
    /**
     * 市
     */
    public static String CITY = "";
    /**
     * 区
     */
    public static String DISTRICT = "";
    /**
     * 当前网络时间
     */
    public static String NetWorkTime=null;
    /**
     * 尺寸
     *
     * @author Shurrik
     */
    public static final class size {
        /**
         * 屏幕宽
         */
        public static int W = 480;
        /**
         * 屏幕高
         */
        public static int H = 800;
    }

    /**
     * 初始化屏幕尺寸
     */
    public static void initDisplaySize(Activity activity) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        G.size.W = mDisplayMetrics.widthPixels;
        G.size.H = mDisplayMetrics.heightPixels;
    }

    /**
     * 提示
     *
     * @param msg 提示信息
     */
    public static void showToast(Context context, String msg) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, size.H/4);
        toast.show();

    }

    /**
     * 记录调试信息
     *
     * @param msg 调试信息
     */
    public static void log(Object msg) {
        if (DEBUG) {
            LogUtils.i(String.valueOf(msg));
        }
    }

    /**
     * 判断网络是否连接
     *
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    public static final class user {
        public static String USER_ID = "";
        public static String MOBILE = "";
        public static String USER_PASSWORD = "";
        public static String USER_TOKEN = "";
        public static String TOKEN_KEY = "";
        public static String TOKEN_SECRET = "";
        public static String USER_CARD_ID = "";
        public static String EMAIL = "";
        public static String BANK_CARD = "";
        public static String REAL_NAME = "";

        public static void set(String userId, String userPwd, String userToken, String tokenKey, String tokenSecret, String userCardId) {
            USER_ID = userId;
            USER_PASSWORD = userPwd;
            USER_TOKEN = userToken;
            TOKEN_KEY = tokenKey;
            TOKEN_SECRET = tokenSecret;
            USER_CARD_ID = userCardId;
        }

        public static void reset() {
            USER_ID = "";
            USER_PASSWORD = "";
            USER_TOKEN = "";
            TOKEN_KEY = "";
            TOKEN_SECRET = "";
            USER_CARD_ID = "";
        }

//        /**
//         * 是否登录
//         */
//        public static boolean isLogin() {
//            if (USER_ID.equals("") && TOKEN_KEY.equals("")) {
//                return false;
//            }
//            return true;
//        }
    }

    public static class type {
        public static final List<RootType> ROOT_TYPES = new ArrayList<RootType>();//一级分类
        public static final List<ChildType> CHILD_TYPES = new ArrayList<ChildType>();//二级分类

        static {
            int[][] codes = {{1010000, 1011000, 1012000, 1013000},
                    {1020000, 1021000, 1022000, 1023000},
                    {1030000, 1031000, 1032000, 1033000},
                    {1040000, 1041000, 1042000, 1043000, 1044000},
                    {1050000, 1051000, 1052000, 1053000, 1054000, 1055000},
                    {1060000, 1061000, 1062000, 1063000, 1064000, 1065000},
                    {1070000, 1071000, 1072000, 1073000, 1074000},
                    {1080000, 1081000, 1082000, 1083000},
                    {1090000, 1091000, 1092000, 1093000, 1094000, 1095000, 1096000},
                    {1100000, 1101000, 1102000, 1103000, 1104000},
                    {1110000, 1111000, 1112000, 1113000},
                    {6010000, 6011000, 6012000, 6013000, 6014000, 6015000, 6016000},
                    {6020000, 6021000, 6022000, 6023000, 6024000}};
            String[][] names = {{"餐饮", "早午晚餐", "水果零食", "烟酒茶"}, {"交通", "公共交通", "打车租车", "私家车费用"}, {"服饰", "化妆饰品", "衣服裤子", "鞋帽包包"},
                    {"通讯", "上网费", "座机费", "手机费", "邮寄费"}, {"居家", "日常用品", "水电煤气", "物业管理", "维修保养", "房租"}, {"娱乐", "休闲玩乐", "宠物宝贝", "旅游度假", "腐败聚会", "运动健身"},
                    {"医疗", "保健费", "治疗费", "美容费", "药品费"}, {"教育", "数码装备", "书报杂志", "培训进修"}, {"投资", "利息支出", "投资亏损", "按揭还款", "消费税收", "赔偿罚款", "银行手续"},
                    {"人情", "孝敬家长", "慈善捐助", "还人钱物", "送礼请客"}, {"其他", "其他支出", "意外丢失", "烂账损失"}, {"职业收入", "工资收入", "利息收入", "加班收入", "奖金收入", "投资收入", "兼职收入"},
                    {"其他收入", "礼金收入", "中奖收入", "意外来钱", "经营所得"}};
            int[][] colors = {{R.color.c_spjs, R.color.c_spjs, R.color.c_spjs, R.color.c_spjs},
                    {R.color.c_xcjt, R.color.c_xcjt, R.color.c_xcjt, R.color.c_xcjt},
                    {R.color.c_yfsp, R.color.c_yfsp, R.color.c_yfsp, R.color.c_yfsp},
                    {R.color.c_jltx, R.color.c_jltx, R.color.c_jltx, R.color.c_jltx, R.color.c_jltx},
                    {R.color.c_jjwy, R.color.c_jjwy, R.color.c_jjwy, R.color.c_jjwy, R.color.c_jjwy, R.color.c_jjwy},
                    {R.color.c_xxyl, R.color.c_xxyl, R.color.c_xxyl, R.color.c_xxyl, R.color.c_xxyl, R.color.c_xxyl},
                    {R.color.c_ylbj, R.color.c_ylbj, R.color.c_ylbj, R.color.c_ylbj, R.color.c_ylbj},
                    {R.color.c_xxjx, R.color.c_xxjx, R.color.c_xxjx, R.color.c_xxjx},
                    {R.color.c_jrbx, R.color.c_jrbx, R.color.c_jrbx, R.color.c_jrbx, R.color.c_jrbx, R.color.c_jrbx, R.color.c_jrbx},
                    {R.color.c_rqwl, R.color.c_rqwl, R.color.c_rqwl, R.color.c_rqwl, R.color.c_rqwl},
                    {R.color.c_qtzx, R.color.c_qtzx, R.color.c_qtzx, R.color.c_qtzx},
                    {R.color.c_zysr, R.color.c_zysr, R.color.c_zysr, R.color.c_zysr, R.color.c_zysr, R.color.c_zysr, R.color.c_zysr},
                    {R.color.c_qtsr, R.color.c_qtsr, R.color.c_qtsr, R.color.c_qtsr}};
            int[][] icons = {{R.drawable.ic_food, R.drawable.ic_meals, R.drawable.ic_fruits, R.drawable.ic_tea},
                    {R.drawable.ic_traffic, R.drawable.ic_bus_p, R.drawable.ic_car_p, R.drawable.ic_taxi_p},
                    {R.drawable.ic_dress, R.drawable.ic_cosmetics, R.drawable.ic_clothes, R.drawable.ic_bags_p},
                    {R.drawable.ic_communication, R.drawable.ic_internet, R.drawable.ic_landline, R.drawable.ic_mobile, R.drawable.ic_mail},
                    {R.drawable.ic_home, R.drawable.ic_items, R.drawable.ic_water, R.drawable.ic_property, R.drawable.ic_maintenance, R.drawable.ic_rent},
                    {R.drawable.ic_entertainment, R.drawable.ic_leisure, R.drawable.ic_pet, R.drawable.ic_travel, R.drawable.ic_party, R.drawable.ic_movement},
                    {R.drawable.ic_medical, R.drawable.ic_care, R.drawable.ic_treatment, R.drawable.ic_beauty_p, R.drawable.ic_expended},
                    {R.drawable.ic_education, R.drawable.ic_digital, R.drawable.ic_magazine, R.drawable.ic_training},
                    {R.drawable.ic_investment, R.drawable.ic_taxi, R.drawable.ic_loss, R.drawable.ic_interest_pay, R.drawable.ic_eating_c_p, R.drawable.ic_reimbursement, R.drawable.ic_formalities},
                    {R.drawable.ic_human, R.drawable.ic_parents, R.drawable.ic_charity, R.drawable.ic_repay, R.drawable.ic_gift},
                    {R.drawable.ic_other_spending, R.drawable.ic_other_spending_p, R.drawable.ic_accidentally_lost, R.drawable.ic_damage},
                    {R.drawable.ic_professional_earnings, R.drawable.ic_wage, R.drawable.ic_interest, R.drawable.ic_overtime, R.drawable.ic_onus_p, R.drawable.ic_investment_p, R.drawable.ic_part_time},
                    {R.drawable.ic_other_income, R.drawable.ic_amounts, R.drawable.ic_the_winning, R.drawable.ic_accident, R.drawable.ic_business_p}};
            for (int x = 0; x < codes.length; x++) {
                RootType rootType = new RootType();
                rootType.setCode(codes[x][0]);
                rootType.setName(names[x][0]);
                int type = codes[x][0] < 6000000 ? 0 : 1;    // Code小于6000000是支出 0 支出 1 收入
                rootType.setType(type);
                rootType.setIcon(icons[x][0]);
                rootType.setColor(colors[x][0]);
                rootType.setPosition(x);
                List<ChildType> childList = new ArrayList<ChildType>();
                for (int y = 1; y < codes[x].length; y++) {
                    ChildType childType = new ChildType();
                    childType.setRootCode(codes[x][0]);
                    childType.setIcon(icons[x][y]);
                    childType.setCode(codes[x][y]);
                    childType.setName(names[x][y]);
                    childType.setType(type);
                    childType.setColor(colors[x][0]);
                    childType.setPosition(y - 1);
                    childList.add(childType);
                }
                CHILD_TYPES.addAll(childList);
                rootType.setChildList(childList);
                ROOT_TYPES.add(rootType);
                List<ChildType> childList_p = new ArrayList<ChildType>();
                for (int y = 1; y < codes[x].length; y++) {
                    ChildType childType = new ChildType();
                    childType.setRootCode(codes[x][0]);
                    childType.setCode(codes[x][y]);
                    childType.setName(names[x][y]);
                    childType.setType(type);
                    childType.setColor(colors[x][0]);
                    childType.setPosition(y - 1);
                    childList_p.add(childType);
                }

                rootType.setChildList(childList_p);

            }
        }

        public static void initTypeCodeDB(Context context) throws Exception {
            DbUtils db = DbUtils.create(context);
            if (db.tableIsExist(TypeEntity.class)) {// 表已经存在
                return;
            }
            List<TypeEntity> types = new ArrayList<TypeEntity>();
            for (ChildType childType : CHILD_TYPES) {
                TypeEntity type = new TypeEntity();
                type.setTypeCode(childType.getCode());
                type.setCount(0);
                types.add(type);
            }
            db.saveAll(types);
        }

        /**
         * 根据分类编号查询一级分类对象
         *
         * @param rootCode 一级分类编号
         * @return RootType 一级分类对象
         */
        public static RootType getRootTypeByCode(int rootCode) {
            for (RootType rootType : ROOT_TYPES) {
                if (rootType.getCode() == rootCode) {
                    return rootType;
                }
            }
            return null;
        }

        /**
         * 根据分类编号查询二级分类对象
         *
         * @param childCode 二级分类编号
         * @return ChildType 二级分类对象
         */
        public static ChildType getChildTypeByCode(int childCode) {
            for (ChildType childType : CHILD_TYPES) {
                if (childType.getCode() == childCode) {
                    return childType;
                }
            }
            return null;
        }

        /**
         * 根据分类编号查询二级分类对象
         *
         * @param rootCode  一级分类编号
         * @param childCode 二级分类编号
         * @return ChildType 二级分类对象
         */
        public static ChildType getChildTypeByCode(int rootCode, int childCode) {
            RootType rootType = getRootTypeByCode(rootCode);
            for (ChildType childType : rootType.getChildList()) {
                if (childType.getCode() == childCode) {
                    return childType;
                }
            }
            return null;
        }
    }

    public static final class PieColor {

//        public static final String color[] = {"#76EEC6", "#458B74", "#000000", "#FF0000", "#191970", "#7B68EE", "#0000CD", "#1E90FF",
//                "#006400", "#FFF68F", "#8B0000", "#90EE90", "#8B008B", "#9C9C9C", "#BFEFFF", "#CDB5CD", "#4A708B", "#AB82FF", "#9A32CD",
//                "#00BFFF", "#D15FEE", "#27408B", "#EE3A8C", "#FF00FF", "#836FFF", "#EED5D2", "#8B1C62", "#FF82AB", "#EEA2AD", "#8B5F65",
//                "#EE30A7", "#8B3A62", "#FF1493", "#CDB38B", "#2F4F4F", "#FF4500", "#FF6347", "#FF7F00", "#CD8500", "#A020F0",
//                "#EE8262", "#FF4040", "#FF4500", "#FF69B4", "#CD853F", "#FF8C00", "#CD6839", "#CD5555", "#B8860B", "#FFFF00",};

        public static final int color2[] = {Color.parseColor("#76EEC6"), Color.parseColor("#458B74"),
                Color.parseColor("#D15FEE"), Color.parseColor("#27408B"), Color.parseColor("#EE3A8C"),
                Color.parseColor("#FF00FF"), Color.parseColor("#836FFF"), Color.parseColor("#EED5D2"),
                Color.parseColor("#8B1C62"), Color.parseColor("#FF82AB"), Color.parseColor("#EEA2AD"),
                Color.parseColor("#8B5F65"), Color.parseColor("#EE30A7"), Color.parseColor("#8B3A62"),
                Color.parseColor("#FF1493"), Color.parseColor("#CDB38B"), Color.parseColor("#2F4F4F"),
                Color.parseColor("#FF4500"), Color.parseColor("#FF6347"), Color.parseColor("#FF7F00"),
                Color.parseColor("#CD8500"), Color.parseColor("#A020F0"), Color.parseColor("#EE8262"),
                Color.parseColor("#FF4040"), Color.parseColor("#FF4500"), Color.parseColor("#FF69B4"),
                Color.parseColor("#CD853F"), Color.parseColor("#FF8C00"), Color.parseColor("#CD6839"),
                Color.parseColor("#CD5555"), Color.parseColor("#B8860B"), Color.parseColor("#FFFF00"),
                Color.parseColor("#000000"), Color.parseColor("#FF0000"), Color.parseColor("#191970"),
                Color.parseColor("#7B68EE"), Color.parseColor("#0000CD"), Color.parseColor("#1E90FF"),
                Color.parseColor("#006400"), Color.parseColor("#FFF68F"), Color.parseColor("#8B0000"),
                Color.parseColor("#90EE90"), Color.parseColor("#8B008B"), Color.parseColor("#9C9C9C"),
                Color.parseColor("#BFEFFF"), Color.parseColor("#CDB5CD"), Color.parseColor("#4A708B"),
                Color.parseColor("#AB82FF"), Color.parseColor("#9A32CD"), Color.parseColor("#00BFFF")
        };
    }


    /* 根据手机的分辨率从 dp 的单位 转成为 px(像素)
    */
    public static int dp2px(Context context, double dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 判断字符串是否为空
     */
    public static boolean isEmteny(String values){
        if(null==values||"".equals(values)||"null".equals(values)){
            return true;
        }
        return  false;
    }

    public static String momeyFormat(double momey){
        String s = "0.00";
        DecimalFormat format = new DecimalFormat("########0.00");
        double n;
        if (momey<100000){
            n = momey;
            s = format.format(n);
        }else if (10000 <= momey && momey<100000000){
             n = momey/10000;
             s = format.format(n)+"万";
        }else if (momey>=100000000){
             n = momey/100000000;
             s = format.format(n)+"亿";
        }else if (momey==0){
             s="0.00";
        }else if (momey>-100000000 && momey<-100000){
             n = momey/10000;
             s = format.format(n)+"万";
        }else if (momey<-1000000000){
             n = momey/100000000;
             s = format.format(n)+"亿";
        }
        return s;

    }
}
