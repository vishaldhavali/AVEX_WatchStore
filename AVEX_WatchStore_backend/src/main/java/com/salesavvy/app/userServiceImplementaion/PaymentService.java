package com.salesavvy.app.userServiceImplementaion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.salesavvy.app.entites.CartItem;
import com.salesavvy.app.entites.Order;
import com.salesavvy.app.entites.OrderItem;
import com.salesavvy.app.entites.OrderStatus;
import com.salesavvy.app.userRepositories.CartRepo;
import com.salesavvy.app.userRepositories.OrderItemRepo;
import com.salesavvy.app.userRepositories.OrderRepo;
import com.salesavvy.app.userServices.PaymentServiceContract;

@Service
public class PaymentService implements PaymentServiceContract {

	@Value("${RazorKey.ID}")
	String rozarkeyid;
	@Value("${RazorKey.Secret}")
	String rozarSecretkey;
	
	private final OrderRepo orderrepo;
	private final OrderItemRepo orderiteamrepo;
	private final CartRepo cartrepo; 
	
	
	
	
	public PaymentService(OrderRepo orderrepo, OrderItemRepo orderiteamrepo, CartRepo cartrepo) {
		super();
		this.orderrepo = orderrepo;
		this.orderiteamrepo = orderiteamrepo;
		this.cartrepo = cartrepo;
	}

	@Transactional
	@Override
	public String CreateOrder(int UserId, BigDecimal totalAmount, List<OrderItem> orderitem) throws RazorpayException {
		RazorpayClient razorpayclient =new RazorpayClient(rozarkeyid, rozarSecretkey);
		
		
	var orderrequest	= new JSONObject();
	
	//prepare order request
	orderrequest.put("amount", totalAmount.multiply(BigDecimal.valueOf(100)).intValue());
	orderrequest.put("currency", "INR");
	orderrequest.put("receipt", "txn_" + System.currentTimeMillis());		
	com.razorpay.Order razorpayorder = razorpayclient.orders.create(orderrequest);
	
	// save the details in ordertable
	
	Order order = new Order();
	
	order.setId(razorpayorder.get("id"));
	order.setUserId(UserId);
	order.setStatus(OrderStatus.PENDING);
	order.setCreatedAt(LocalDateTime.now());
	order.setTotalAmount(totalAmount);
	
	orderrepo.save(order);
	
	return razorpayorder.get("id");
	
			}

	@Override
	@Transactional
	public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature,
			int userId) {
		try {
		
			JSONObject attribute = new JSONObject();
			attribute.put("razorpay_order_id",razorpayOrderId);
			attribute.put("razorpay_payment_id",razorpayPaymentId);
			attribute.put("razorpay_signature", razorpaySignature);
			
			boolean isSignatureVaild = com.razorpay.Utils.verifyPaymentSignature(attribute, rozarSecretkey);
		
			if(isSignatureVaild) {
				// order status Sucess
				Order order = orderrepo.findById(razorpayOrderId)
				        .orElseThrow(() -> new IllegalArgumentException("Invalid order"));
				order.setStatus(OrderStatus.SUCCESS);
				order.setUpdatedAt(LocalDateTime.now());
				orderrepo.save(order);
				
				List<CartItem> cartItems = cartrepo.findByUserUserId(userId);
				for(CartItem cartItem : cartItems) {
					OrderItem orderItem = new OrderItem();
					orderItem.setOrder(order);
					orderItem.setProductId(cartItem.getProduct().getProductId());;
					orderItem.setQuantity(cartItem.getQuantity());
					orderItem.setPricePerUnite(cartItem.getProduct().getPrice());
					orderItem.setTotalPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
					orderiteamrepo.save(orderItem);
				}
				cartrepo.deleteByUserUserId(userId);
			return true;
			} else {
				return false;
			}
			
		
	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}
	}
	
	@Transactional
	public void saveOrderItems(String orderId, List<OrderItem> items) {
	    Order order = orderrepo.findById(orderId)
	            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
	    for (OrderItem item : items) {
	        item.setOrder(order);
	        orderiteamrepo.save(item);
	    }
	}


}
