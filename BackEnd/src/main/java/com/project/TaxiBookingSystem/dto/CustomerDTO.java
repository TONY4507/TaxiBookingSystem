package com.project.TaxiBookingSystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
   


	private int CustomerId;

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


    
}

