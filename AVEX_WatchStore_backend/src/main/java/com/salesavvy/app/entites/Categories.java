package com.salesavvy.app.entites;


import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Categories {
@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "category_id")	
int categoryId;
@Column(name = "category_name", nullable = false, unique = true)
	String categoryName;


public Categories() {
}


public Categories(int categoryId, String category_name) {
	super();
	this.categoryId = categoryId;
	this.categoryName = category_name;
}


public Categories(String category_name) {
	super();
	this.categoryName = category_name;
}


public int getcategoryId() {
	return categoryId;
}


public void setcategoryId(int categoryId) {
	this.categoryId = categoryId;
}


public String getCategory_name() {
	return categoryName;
}


public void setCategory_name(String category_name) {
	this.categoryName = category_name;
}


@Override
public int hashCode() {
	return Objects.hash(categoryId, categoryName);
}


@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Categories other = (Categories) obj;
	return categoryId == other.categoryId && Objects.equals(categoryName, other.categoryName);
}


@Override
public String toString() {
	return "Categories [categoryId=" + categoryId + ", category_name=" + categoryName + "]";
}



}
