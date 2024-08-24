package com.project.TaxiBookingSystem.entity;


import jakarta.persistence.MappedSuperclass;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@MappedSuperclass
public abstract class User {
	
	
	@NotBlank(message = "{customer.username.required}")
	@Size(min = 3, max = 10, message = "{size.range}")
	private String username;
	
	@NotBlank(message =  "{customer.password.required}")
	@Size(min = 6, message =  "{customer.password.size}")
	private String password;
	
	@NotBlank(message = "{not.blank}")
	private String address;

    @NotBlank(message = "{customer.mobile.number.invalid}")
    @Pattern(regexp = "\\d{10}", message = "{customer.mobile.number.invalid}")
	private String mobileNumber;
	
    @NotBlank(message = "{customer.email.required}")
    @Email(message = "{email.invalid}")
	private String email;

	// Getters and Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
