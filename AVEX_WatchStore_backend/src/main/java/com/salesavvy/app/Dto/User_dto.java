package com.salesavvy.app.Dto;

public class User_dto {
	        
	    private String username;
	    private String email;
	    private String password;
	    private String role;

	    // Default constructor
	    public User_dto() {
	    }

	    // Parameterized constructor
	    public User_dto(String username, String email, String role) {
	        this.username = username;
	        this.email = email;
	        this.role = role;
	    }
	    // Getters & Setters
	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public String getRole() {
	        return role;
	    }

	    public void setRole(String role) {
	        this.role = role;
	    }
	
}
