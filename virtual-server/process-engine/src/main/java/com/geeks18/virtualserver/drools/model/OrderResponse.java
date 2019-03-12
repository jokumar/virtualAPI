//This is a Auto Generated Class 
package com.geeks18.virtualserver.drools.model;
public class OrderResponse extends GenericRuleModel{ 
 private String orderid; 
 private String price; 
 private String status; 
 private String test; 
 public String getOrderId(){ 
return	this.orderid; 
} 
 public String getPrice(){ 
return	this.price; 
} 
 public String getStatus(){ 
return	this.status; 
} 
 public String getTest(){ 
return	this.test; 
} 
 public void setOrderId(String var){ 
	this.orderid=var; 
} 
 public void setPrice(String var){ 
	this.price=var; 
} 
 public void setStatus(String var){ 
	this.status=var; 
} 
 public void setTest(String var){ 
	this.test=var; 
} 
 public void doit() { 
   System.out.println("Hello world") ;
 }
}