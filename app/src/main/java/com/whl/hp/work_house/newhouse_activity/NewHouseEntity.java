package com.whl.hp.work_house.newhouse_activity;

import java.util.List;

/**
 * Created by hp-whl on 2015/9/28.
 */
public class NewHouseEntity {
    private String fid;
    private String fcover;
    private String fname;
    private String faddress;
    private String fregion;
    private String fpricedisplaystr;
    private String faroundhighprice;
    private String lng;
    private String lat;
    private List<BookMark> bookmark;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFcover() {
        return fcover;
    }

    public void setFcover(String fcover) {
        this.fcover = fcover;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFaddress() {
        return faddress;
    }

    public void setFaddress(String faddress) {
        this.faddress = faddress;
    }

    public String getFregion() {
        return fregion;
    }

    public void setFregion(String fregion) {
        this.fregion = fregion;
    }

    public String getFpricedisplaystr() {
        return fpricedisplaystr;
    }

    public void setFpricedisplaystr(String fpricedisplaystr) {
        this.fpricedisplaystr = fpricedisplaystr;
    }

    public String getFaroundhighprice() {
        return faroundhighprice;
    }

    public void setFaroundhighprice(String faroundhighprice) {
        this.faroundhighprice = faroundhighprice;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public List<BookMark> getBookmark() {
        return bookmark;
    }

    public void setBookmark(List<BookMark> bookmark) {
        this.bookmark = bookmark;
    }
}
