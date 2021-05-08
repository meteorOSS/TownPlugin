package com.meteor.meteortown.data.town;

public enum Dir {
    //
    S("南",360.0D,180.0D),
    N("北",159.0D,180.0D),
    W("西",89.0D,270.0D),
    E("东",247.0D,90.0D),
    X("偏航",0.0D,0.0D);
    private String key;
    private double vaulue;
    private double rotate;
    private Dir(String key,Double vaulue,Double rotate){
        this.key = key;
        this.vaulue = vaulue;
        this.rotate = rotate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getVaulue() {
        return vaulue;
    }

    public void setVaulue(double vaulue) {
        this.vaulue = vaulue;
    }

    public double getRotate() {
        return rotate;
    }

    public void setRotate(double rotate) {
        this.rotate = rotate;
    }
}
