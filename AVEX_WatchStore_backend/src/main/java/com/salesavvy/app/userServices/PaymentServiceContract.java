package com.salesavvy.app.userServices;

import java.math.BigDecimal;
import java.util.List;

import com.razorpay.RazorpayException;
import com.salesavvy.app.entites.OrderItem;


public interface PaymentServiceContract {

	public String CreateOrder(int UserId,BigDecimal totalAmount,List<OrderItem> orderitem ) throws RazorpayException ;
public boolean verifyPayment(String razorpayOrderId,String razorpayPaymentId,String razorpaySignature,int userId);

}
