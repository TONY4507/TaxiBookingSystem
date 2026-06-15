package com.project.TaxiBookingSystem.dto;

import com.project.TaxiBookingSystem.enums.ApprovalStatus;
import com.project.TaxiBookingSystem.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private Integer userId;
    private String username;
    private String email;
    private Role role;
    private ApprovalStatus approvalStatus;
}
