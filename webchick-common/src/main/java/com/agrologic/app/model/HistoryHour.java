package com.agrologic.app.model;


import java.io.Serializable;

public class HistoryHour implements Serializable{

    private Long flock_id;
    private Long grow_day;
    private String d_num;
    private String values;
    private String label;
    private Integer format;

    public HistoryHour(){

    }

    public HistoryHour(Long flock_id, Long grow_day, String d_num, String values, String label, Integer format){
        this.flock_id = flock_id;
        this.grow_day = grow_day;
        this.d_num = d_num;
        this.values = values;
        this.label = label;
        this.format = format;
    }

    public Long get_flock_id(){
        return flock_id;
    }

    public void set_flock_id(Long flock_id){
        this.flock_id = flock_id;
    }

    public Long get_grow_day(){
        return grow_day;
    }

    public void set_grow_day(Long grow_day){
        this.grow_day = grow_day;
    }

    public String get_d_num(){
        return d_num;
    }

    public void set_d_num(String d_num){
        this.d_num = d_num;
    }

    public String get_values(){
        return values;
    }

    public void set_values(String values){
        this.values = values;
    }

    public String get_label(){
        return label;
    }

    public void set_label(String label){
        this.label = label;
    }

    public Integer get_format(){
        return format;
    }

    public void set_format(Integer format){
        this.format = format;
    }
}
