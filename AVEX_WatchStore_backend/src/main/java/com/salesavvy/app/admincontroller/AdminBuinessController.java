package com.salesavvy.app.admincontroller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salesavvy.app.adminservice.AdminBusinessContract;

@RestController
@RequestMapping("/admin/business")
public class AdminBuinessController {

	private final AdminBusinessContract adminBusinessService;

	public AdminBuinessController(AdminBusinessContract adminBusinessService) {
		this.adminBusinessService = adminBusinessService;
	}

	// GET /admin/business/monthly?month=4&year=2026
	@GetMapping("/monthly")
	public ResponseEntity<?> getMonthlyBusiness(@RequestParam int month, @RequestParam int year) {
		try {
			Map<String, Object> result = adminBusinessService.calculateMonthlyBusiness(month, year);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
		}
	}

	// GET /admin/business/daily?date=2026-04-07
	@GetMapping("/daily")
	public ResponseEntity<?> getDailyBusiness(@RequestParam LocalDate date) {
		try {
			Map<String, Object> result = adminBusinessService.calculateDailyBusiness(date);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
		}
	}

	// GET /admin/business/yearly?year=2026
	@GetMapping("/yearly")
	public ResponseEntity<?> getYearlyBusiness(@RequestParam int year) {
		try {
			Map<String, Object> result = adminBusinessService.calculateYearlyBusiness(year);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
		}
	}

	// GET /admin/business/overall
	@GetMapping("/overall")
	public ResponseEntity<?> getOverallBusiness() {
		try {
			Map<String, Object> result = adminBusinessService.calculateOverallBusiness();
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Something went wrong while calculating overall business");
		}
	}
}