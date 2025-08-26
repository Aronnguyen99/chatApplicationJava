/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapp6;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class message  implements Comparable<message>  {
    private String col_name;
    private Date date;
    private String toName;
    private String fromName;
    private String content;

    public String getCol_name(){
        return col_name;
    }

    public Date getDate() {
        return date;
    }

    public void setCol_name(String col_name) {
        this.col_name = col_name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToName() {
        return toName;
    }

    public String getFromName() {
        return fromName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int compareTo(message o) {
        return getDate().compareTo(o.getDate());
    }
    
}
