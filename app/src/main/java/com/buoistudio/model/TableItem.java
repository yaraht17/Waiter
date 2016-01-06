package com.buoistudio.model;

import java.io.Serializable;

public class TableItem implements Serializable {
    private String id;
    private String name;
    private int slotNumber;

    private long timeCall;

    public TableItem(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public TableItem(String id, String name, long timeCall) {
        this.timeCall = timeCall;
        this.name = name;
        this.id = id;
    }

    public TableItem(String id, String name, int slotNumber) {
        this.id = id;
        this.name = name;
        this.slotNumber = slotNumber;
    }

    public long getTimeCall() {
        return timeCall;
    }

    public void setTimeCall(long timeCall) {
        this.timeCall = timeCall;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }
}
