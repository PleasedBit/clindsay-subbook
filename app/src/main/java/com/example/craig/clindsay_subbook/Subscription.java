package com.example.craig.clindsay_subbook;

import java.io.Serializable;

/**
 * Created by Craig on 2018-02-04.
 */

public class Subscription implements Serializable {

    private String Name;
    private String date;
    private String Cost;
    private String Desc;

    Subscription(String Name, String date, String Cost, String Desc) {
        this.Name = Name;
        this.date = date;
        this.Cost = Cost;
        this.Desc = Desc;
    }

    public String getName() { return this.Name; }
    public String getDate() { return this.date; }
    public String getCost() { return this.Cost; }
    public String getDesc() { return this.Desc; }

    public void setName(String Name) { this.Name = Name; }
    public void setDate(String Date) { this.date = Date; }
    public void setCost(String Cost) { this.Cost = Cost; }
    public void setDesc(String Desc) { this.Desc = Desc; }

    public String toString() {
        return this.Name + "    " + this.date + "    $" + this.Cost + "\n      " + this.Desc;
    }


}
