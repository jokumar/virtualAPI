//This is a Auto Generated Class 
package com.geeks18.virtualserver.drools.model;
public class OrderRequest extends GenericRuleModel{ 
 private String orderid; 
 private String count; 
 public String getOrderId(){ 
return	this.orderid; 
} 
 public String getCount(){ 
return	this.count; 
} 
 public void setOrderId(String var){ 
	this.orderid=var; 
} 
 public void setCount(String var){ 
	this.count=var; 
} 
 public void doit() { 
   System.out.println("Hello world") ;
 }
}