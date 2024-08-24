package com.project.TaxiBookingSystem.dto;

import com.project.TaxiBookingSystem.entity.Cab;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class DriverDTO {
	
	 	@NotBlank(message = "Name is required")
	 	private int Driverid;
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
	    
	    @NotBlank(message = "{driver.license.required}")
	    @Size(min = 5, max = 15, message = "{driver.license.size}")
	    private String licenseNumber;
	    
	    @NotNull(message = "{driver.cab.required}")
	    @OneToOne(cascade = CascadeType.ALL)
	    private Cab cab;
	  


    // Constructors, Getters, and Setters
}
