//This is a Auto Generated Class 
package com.geeks18.rule.controller;
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import com.geeks18.rule.controller.AbstractRestController;  
import com.geeks18.virtualserver.drools.model.OrderRequest; 
import com.geeks18.virtualserver.drools.model.OrderResponse; 
@RestController  
@RequestMapping("/order/getPrice") 
public class OrderController extends AbstractRestController<OrderRequest,OrderResponse>{ 
public OrderController(){
super(new OrderResponse()); 
} 
} 
