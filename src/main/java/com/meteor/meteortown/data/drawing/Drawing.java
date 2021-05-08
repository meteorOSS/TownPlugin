package com.meteor.meteortown.data.drawing;

import java.util.List;

public class Drawing {
    String key;
    String name;
    String sc;
    int r;
    List<String> cmds;

    public Drawing(String key, String name, String sc,int r,List<String> cmds) {
        this.key = key;
        this.name = name;
        this.sc = sc;
        this.r = r;
        this.cmds = cmds;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public List<String> getCmds() {
        return cmds;
    }

    public void setCmds(List<String> cmds) {
        this.cmds = cmds;
    }
}
