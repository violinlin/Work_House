package com.whl.hp.work_house.LookHouse;

import java.util.List;

/**
 * Created by hp-whl on 2015/10/5.
 */
public class LookHouseEntitys {
    private String rid;
    private String name;
    private String alias;
    private String deadtime;
    List<Houses>houses;


    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDeadtime() {
        return deadtime;
    }

    public void setDeadtime(String deadtime) {
        this.deadtime = deadtime;
    }

    public List<Houses> getHouses() {
        return houses;
    }

    public void setHouses(List<Houses> houses) {
        this.houses = houses;
    }
}
