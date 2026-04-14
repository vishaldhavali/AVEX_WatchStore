package com.salesavvy.app.adminserviceImplemention;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.salesavvy.app.adminservice.AdminBusinessContract;
import com.salesavvy.app.entites.Order;
import com.salesavvy.app.entites.OrderItem;
import com.salesavvy.app.entites.OrderStatus;
import com.salesavvy.app.userRepositories.OrderItemRepo;
import com.salesavvy.app.userRepositories.OrderRepo;
import com.salesavvy.app.userRepositories.Products_repo;

@Service
public class AdminBusinessService implements AdminBusinessContract {
	
	private final OrderRepo orderepo;
	private final OrderItemRepo orderitemrepo;
	private final Products_repo productrepo;
	
	

	

	public AdminBusinessService(OrderRepo orderepo, OrderItemRepo orderitemrepo, Products_repo productrepo) {
		super();
		this.orderepo = orderepo;
		this.orderitemrepo = orderitemrepo;
		this.productrepo = productrepo;
	}
	 @Override
	    public Map<String, Object> calculateMonthlyBusiness(int month, int year) {
	        List<Order> successOrders = orderepo.findSuccessfulOrdersByMonthAndYear(month, year);
	        return calculateBusinessMetrics(successOrders);
	    }

	    @Override
	    public Map<String, Object> calculateDailyBusiness(LocalDate date) {
	        List<Order> successOrders = orderepo.findSuccessfulOrdersByDate(date);
	        return calculateBusinessMetrics(successOrders);
	    }

	    @Override
	    public Map<String, Object> calculateYearlyBusiness(int year) {
	        List<Order> successOrders = orderepo.findSuccessfulOrdersByYear(year);
	        return calculateBusinessMetrics(successOrders);
	    }

	    @Override
	    public Map<String, Object> calculateOverallBusiness() {
	        List<Order> successOrders = orderepo.findAllByStatus(OrderStatus.SUCCESS);

	        Map<String, Object> response = calculateBusinessMetrics(successOrders);
	        
	        return response;
	    }
	
	private Map<String, Object> calculateBusinessMetrics(List<Order> orders) {
		double totalrevenue = 0.0;
		Map<String, Integer> categorySales = new HashMap<String, Integer>();
	for(Order order : orders) {
		totalrevenue += order.getTotalAmount().doubleValue();
		
		List<OrderItem> items = orderitemrepo.findByOrder_Id(order.getId());
		for(OrderItem item : items) {
			String catagoryName = productrepo.findCategoryNameByProductId(item.getProductId());
			categorySales.put(catagoryName, categorySales.getOrDefault(catagoryName,0) + item.getQuantity());
		}
	}
	Map<String, Object> metrics = new HashMap<>();
	  metrics.put("totalRevenue", totalrevenue);
      metrics.put("categorySales", categorySales);

      return metrics;
	
	}

}
