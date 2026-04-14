package com.salesavvy.app.entites;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "jwt_tokens")
public class JwtToken {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int token_id;
private String token;

private LocalDateTime expires_at ;

@ManyToOne
@JoinColumn(name = "user_id",nullable = false)
private User user;


public JwtToken() {
}

public JwtToken(String token, User user) {
	super();
	this.token = token;
	this.user = user;
}

public JwtToken(int token_id, String token,  LocalDateTime expires_at, User user) {
	super();
	this.token_id = token_id;
	this.token = token;
	
	this.expires_at = expires_at;
	this.user = user;
}

public JwtToken(String token, LocalDateTime expires_at, User user) {
	super();
	this.token = token;
	this.expires_at = expires_at;
	this.user= user;
}

public int getToken_id() {
	return token_id;
}

public void setToken_id(int token_id) {
	this.token_id = token_id;
}

public String getToken() {
	return token;
}

public void setToken(String token) {
	this.token = token;
}

public LocalDateTime getExpires_at() {
	return expires_at;
}

public void setExpires_at(LocalDateTime expires_at) {
	this.expires_at = expires_at;
}

public User getUser() {
	return user;
}

public void setUser(User user) {
    this.user= user;
}


@Override
public int hashCode() {
	return Objects.hash(expires_at, token, token_id, user);
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	JwtToken other = (JwtToken) obj;
	return Objects.equals(expires_at, other.expires_at) && Objects.equals(token, other.token)
			&& token_id == other.token_id && Objects.equals(user, other.user);
}

@Override
public String toString() {
	return "JwtToken [token_id=" + token_id + ", token=" + token + ", expires_at=" + expires_at + ", User=" + user
			+ "]";
}
	

	
}
