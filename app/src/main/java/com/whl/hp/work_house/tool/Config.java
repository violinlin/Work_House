package com.whl.hp.work_house.tool;

/**
 * Created by hp-whl on 2015/9/23.
 */
public class Config {
    //城市列表信息
    public static final String CHOICE_CITY = "http://ikft.house.qq.com/index.php?guid=866500021200250&devua=appkft_1080_1920_XiaomiMI4LTE_1.8.3_" +
            "Android19&act=kftcitylistnew&channel=71&devid=866500021200250&appname=QQHouse&mod=appkft";
    //    viewpagerde显示的图片信息
    public static final String FIRST_PAGE_WEBVIEW = "http://ikft.house.qq.com/index.php?guid=866500021200250&devua=appkft_1080_1920" +
            "_XiaomiMI4LTE_1.8.3_Android19&devid=866500021200250&appname=QQHou" +
            "se&mod=appkft&act=homepage&channel=71&cityid=%s";
    //listView 的详情url
    public static final String FIRST_PAGE_LISTVIEW = "http://ikft.house.qq.com/index.php?gu" +
            "id=866500021200250&devua=appkft_1080_1920_XiaomiMI4LTE_1.8.3_Android19&devid=8665" +
            "00021200250&appname=QQHouse&mod=appkft&reqnum=%d&pageflag=%d&act=newslist&channel=71&buttonmore=%d&cityid=%s";
    //资讯详情
    public static final String NEWS_DETAIL = "http://ikft.house.qq.com/index.php?guid=866500021200250&devua=appkft_1080_1920_XiaomiMI4LTE_1.8.3_Android19&" +
            "devid=866500021200250&appname=QQHouse&mod=appkft&act=newsdetail&channel=71&newsid=%s";
    // 资讯评论详情
    public static final String NEWS_COMMENT = "http://ikft.house.qq.com/index.php?guid=866500021200250&devua=appkft_1080_1920_XiaomiMI4LTE_1.8.3_Android19&devid=866500021200250&appname=QQHouse&mod=app" +
            "kft&reqnum=20&pageflag=0&act=newscomment&channel=71&targetid=%s";
    //找房
    public static final String LOOKING_NEWHOUSE = "http://ikft.house.qq.com/index.php?guid=866500021200250&devua=appkft_1080_1920_XiaomiMI4LTE_1.8.3_Android19&rn=10&order=0&searchtype=normal&devid=8665000" +
            "21200250&page=%d&appname=QQHouse&mod=appkft&act=searchhouse&channel=71&cityid=%s";
    //新房详情
    public static final String NEW_HOUSE_INFO = "http://ikft.house.qq.com/index.php?guid=866500021200250&devua=appkft_1080_1920_XiaomiMI4LTE_1.8.3" +
            "_Android19&hid=%s&devid=866500021200250&appname=QQHouse&mod=appkft&act=houseinfo&channel=71";

    /**
     * 找新房 评论
     */
    public static final String NEW_HOUSE_COMMENT = "http://ikft.house.qq.com/index.php?guid=866500021200250&devua=appkft_1080_1920_XiaomiMI4LTE_1.8.3_A" +
            "ndroid19&rn=0&hid=%s&devid=866500021200250&page=1&appname=QQHouse&mod=appkft&type=all&act=housecomment&channel=71";
    //打折优惠 楼盘信息
    public static final String DISCOUNT_SEAL = "http://ikft.house.qq.com/index.php?&page=%d&appname=QQHouse&mo" +
            "d=appkft&act=discountslist&channel=71&cityid=1";
    /**
     * 打折优惠 楼盘评论
     */
    public static final String DISCOUNT_HOUSE_COMMENT = "http://ikft.house.qq.com/index.php?&rn=0&hid=%s&page=1&appname=QQHouse&mod=appkft&typ" +
            "e=all&act=housecomment&channel=%s";


    /**
     * 最新开盘
     */
    public static final String NEWEST_HOUSE = "http://ikft.house.qq.com/index.php?guid=866500021200250&devua=appkft_1080_1920_XiaomiMI4LTE_1.8.3_Android19&rn=10&order=0&searchtype=normal&devid=866500021200250" +
            "&page=%d&appname=QQHouse&mod=appkft&feature=996&act=searchhouse&channel=71&cityid=%s";


    /**
     * 看房团
     */
    public static final String TEAM_HOUSE = "http://ikft.house.qq.com/index.php?guid=000000000000000&devua=appkft_1080_1776_GenymotionSamsungGalaxyS5-4.4.4-API19-1080x1920_1.8.3_Android19&devid=000000000000000&" +
            "appname=QQHouse&mod=appkft&act=kftlist&channel=65&cityid=%s";
//    二手房：
    public static String ERSHOU_URL="http://esf.db.house.qq.com/bj/search/sale/?rf=kanfang";
//             租房：
    public static String ZUFANG_URL="http://esf.db.house.qq.com/bj/search/lease/?rf=kanfang";


//    资讯:
 public static String ZIXUN_URL=" http://ikft.house.qq.com/index.php?devua=appkft_720_1280_XiaomiMI2A_2.3_Android19&devid=860954025171452&mod=appkft&buttonmore=1&cityid=%s&guid=860954025171452&appname=" +
        "QQHouse&huid=H489647634&reqnum=20&pageflag=0&majorversion=v2&act=newslist&channel=27";
}
