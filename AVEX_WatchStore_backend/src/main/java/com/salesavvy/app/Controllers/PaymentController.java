package com.salesavvy.app.Controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;
import com.salesavvy.app.entites.OrderItem;
import com.salesavvy.app.entites.User;
import com.salesavvy.app.userServices.PaymentServiceContract;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	private final PaymentServiceContract pservice;

	@Value("${RazorKey.ID}")
	private String razorKeyId;

	public PaymentController(PaymentServiceContract pservice) {
		super();
		this.pservice = pservice;
	}

	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> Paymentcreation(@RequestBody Map<String, Object> requestbody,
			HttpServletRequest request) {
		try {
			User user = (User) request.getAttribute("Authenticated_User");
			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not authicated"));
			}

			int userid = user.getUserId();

			BigDecimal totalamt = new BigDecimal(requestbody.get("totalamount").toString());
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> rawcartitem = (List<Map<String, Object>>) requestbody.get("cartitems");

			// convert the rawcartiteam to List

			List<OrderItem> cartItems = rawcartitem.stream().map(items -> {
				OrderItem orderitem = new OrderItem();
				orderitem.setProductId(toInt(items.get("productId")));
				orderitem.setQuantity(toInt(items.get("quantity")));
				BigDecimal priceperunit = new BigDecimal(items.get("price").toString());
				orderitem.setPricePerUnite(priceperunit);
				orderitem.setTotalPrice(priceperunit.multiply(BigDecimal.valueOf(toInt(items.get("quantity")))));
				return orderitem;
			}).collect(Collectors.toList());

			String razorpayorderId = pservice.CreateOrder(userid, totalamt, cartItems);

			return ResponseEntity.ok(Map.of("razorpayOrderId", razorpayorderId, "razorpayKeyId", razorKeyId));
		} catch (RazorpayException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "Invaild request data " + e.getMessage()));
		}
	}
	@PostMapping("/verify")
	public ResponseEntity<String> verifypayment(@RequestBody Map<String, Object> requestbody,
			HttpServletRequest request) {

		try {
			User user = (User) request.getAttribute("Authenticated_User");
			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authicated");
			}
			int userid = user.getUserId();
			String rozarpayorderid = (String) requestbody.get("razorpayOrderId");
			String rozarpaypaymentid = (String) requestbody.get("rozarpayPaymentId");
			String rozarpaySignature = (String) requestbody.get("rozarpaySignature");

			boolean isverifed = pservice.verifyPayment(rozarpayorderid, rozarpaypaymentid, rozarpaySignature, userid);
			if (isverifed) {
				return ResponseEntity.ok("Payment Verified Successfully");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Verification failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erorr in Verifcation payment : " + e.getMessage());

		}

	}

	private int toInt(Object value) {
		if (value instanceof Number number) {
			return number.intValue();
		}
		if (value instanceof String str) {
			return Integer.parseInt(str.trim());
		}
		throw new IllegalArgumentException("Invalid numeric value: " + value);
	}
}
