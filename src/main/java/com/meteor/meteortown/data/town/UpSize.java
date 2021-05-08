package com.meteor.meteortown.data.town;

import java.util.List;

public class UpSize {
    int x;
    int y;
    int z;
    PayType payType;
    int points;
    List<TakeItem> takeItemList;
    public UpSize(int x, int y, int z, PayType payType, int points, List<TakeItem> takeItemList) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.payType = payType;
        this.points = points;
        this.takeItemList = takeItemList;
    }
    public UpSize(int x,int y,int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<TakeItem> getTakeItemList() {
        return takeItemList;
    }

    public void setTakeItemList(List<TakeItem> takeItemList) {
        this.takeItemList = takeItemList;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
