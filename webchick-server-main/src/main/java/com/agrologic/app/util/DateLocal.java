
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.util;



import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * Title: DateLocal <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: Agro Logic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class DateLocal extends java.util.Date {
    public DateLocal(int date, int month, int year) {
        super(year - 1900, month - 1, date);
    }

    public DateLocal(int hour, int minute, int date, int month, int year) {
        super(year - 1900, month - 1, date, hour, 0);
    }

    @Override
    public int getMonth() {
        return super.getMonth() + 1;
    }

    @Override
    public void setMonth(int month) {
        super.setMonth(month - 1);
    }

    @Override
    public int getYear() {
        return super.getYear() + 1900;
    }

    @Override
    public void setYear(int year) {
        super.setMonth(year - 1900);
    }

    public DateLocal addDays(int days) {
        return new DateLocal(super.getHours(), super.getMinutes(), super.getDate() + days, super.getMonth() + 1,
                             super.getYear() + 1900);
    }

    public static DateLocal now() {
        java.util.Date now = new java.util.Date();

        return new DateLocal(now.getHours(), now.getMinutes(), now.getDate(), now.getMonth() + 1, now.getYear() + 1900);
    }

    public static String currentDate() {
        Timestamp        time        = new Timestamp(System.currentTimeMillis());
        Date             now         = new Date(time.getTime());
        String           DATE_FORMAT = "hh:mm dd/M/yyyy";
        SimpleDateFormat sdf         = new SimpleDateFormat(DATE_FORMAT);

        return sdf.format(now);
    }
}



