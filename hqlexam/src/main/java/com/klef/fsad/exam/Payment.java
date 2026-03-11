package com.klef.fsad.exam;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="payment")

public class Payment 
{
@Id
private int id;
private String name;
private String date;
private String status;
private double amount;

public Payment() {}

public int getId() { return id; }
public void setId(int id) { this.id = id; }

public String getName() { return name; }
public void setName(String name) { this.name = name; }

public String getDate() { return date; }
public void setDate(String date) { this.date = date; }

public String getStatus() { return status; }
public void setStatus(String status) { this.status = status; }

public double getAmount() { return amount; }
public void setAmount(double amount) { this.amount = amount; }

}