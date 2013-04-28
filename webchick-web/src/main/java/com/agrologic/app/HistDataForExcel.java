
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app;


public class HistDataForExcel {
    private String title;
    private String value;

    public HistDataForExcel() {}

    public HistDataForExcel(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(value).toString();
    }
}



