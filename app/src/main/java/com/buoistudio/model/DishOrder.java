package com.buoistudio.model;

public class DishOrder {
    private String id;
    private DishItem dish;
    private int count;
    private int status;

    private ExtraFeeItem extraFee;
    private boolean isExtraFee;

    public DishOrder(String id, DishItem dish, int count, int status) {
        this.id = id;
        this.dish = dish;
        this.count = count;
        this.status = status;
        isExtraFee = false;
        extraFee = new ExtraFeeItem();
    }

    public DishOrder(ExtraFeeItem extraFee, boolean isExtraFee) {
        this.extraFee = extraFee;
        this.isExtraFee = isExtraFee;
    }

    public boolean isExtraFee() {
        return isExtraFee;
    }

    public void setIsExtraFee(boolean isExtraFee) {
        this.isExtraFee = isExtraFee;
    }

    public ExtraFeeItem getExtraFee() {
        return extraFee;
    }

    public void setExtraFee(ExtraFeeItem extraFee) {
        this.extraFee = extraFee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DishItem getDish() {
        return dish;
    }

    public void setDish(DishItem dish) {
        this.dish = dish;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
