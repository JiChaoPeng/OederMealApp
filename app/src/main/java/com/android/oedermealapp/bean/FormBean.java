package com.android.oedermealapp.bean;


import java.io.Serializable;

public class FormBean implements Serializable {
    private int price;
    private String ownerName;
    private int seat_id;
    private String comment;
    private int commentStar;
    private String foodDetail;
    private int isFinish;
    private int isPay;
    private long time;
    private int id;

    public FormBean() {

    }

    public FormBean(int price, String ownerName, int seat_id, String comment, int commentStar, String foodDetail, int isFinish, int isPay, long time, int id) {
        this.price = price;
        this.ownerName = ownerName;
        this.seat_id = seat_id;
        this.comment = comment;
        this.commentStar = commentStar;
        this.foodDetail = foodDetail;
        this.isFinish = isFinish;
        this.isPay = isPay;
        this.time = time;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getSeat_id() {
        return seat_id;
    }

    public void setSeat_id(int seat_id) {
        this.seat_id = seat_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCommentStar() {
        return commentStar;
    }

    public void setCommentStar(int commentStar) {
        this.commentStar = commentStar;
    }

    public String getFoodDetail() {
        return foodDetail;
    }

    public void setFoodDetail(String foodDetail) {
        this.foodDetail = foodDetail;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
