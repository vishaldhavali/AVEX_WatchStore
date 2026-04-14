package com.salesavvy.app.adminservice;

import java.time.LocalDate;
import java.util.Map;

public interface AdminBusinessContract {

	 // Calculate business stats for a specific month and year
   public  Map<String, Object> calculateMonthlyBusiness(int month, int year);

    // Calculate business stats for a specific date
   public Map<String, Object> calculateDailyBusiness(LocalDate date);

    // Calculate business stats for a full year
    public Map<String, Object> calculateYearlyBusiness(int year);

    // Calculate overall all-time business stats
  public  Map<String, Object> calculateOverallBusiness();
}
