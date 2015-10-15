package com.whl.hp.work_house.been;

/**
 * Created by hp-whl on 2015/9/23.
 */
public class CityEntitys {
    public static final int TYPE_LABEL=0;
    public static final  int TYPE_NAME=1;
    private String category;

    public static int getTypeCount(){
      return 2;
    }
    public int getType(){
        if (category.equals("lable")){
            return TYPE_LABEL;
        }else {
            return TYPE_NAME;
        }
    }
    private String cityid;
    private String cityalias;
    private String cityname;
    private String citypinyin;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCityalias() {
        return cityalias;
    }

    public void setCityalias(String cityalias) {
        this.cityalias = cityalias;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCitypinyin() {
        return citypinyin;
    }

    public void setCitypinyin(String citypinyin) {
        this.citypinyin = citypinyin;
    }
}
