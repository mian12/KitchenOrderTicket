package com.solution.alnahar.kitchenorderticket.model;

public class PendingOrders {


   private String orderNO;
    private String vrnoa;

    private String kOTNo;
   private String tableNO;
    private String amount;
    private String time;
    private  String waiter;
    private  String type;

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public String getkOTNo() {
        return kOTNo;
    }

    public void setkOTNo(String kOTNo) {
        this.kOTNo = kOTNo;
    }

    public String getTableNO() {
        return tableNO;
    }

    public void setTableNO(String tableNO) {
        this.tableNO = tableNO;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVrnoa() {
        return vrnoa;
    }

    public void setVrnoa(String vrnoa) {
        this.vrnoa = vrnoa;
    }
}
