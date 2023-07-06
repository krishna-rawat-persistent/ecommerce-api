package com.workflow2.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileDTO {
    private UUID id;
    private String name;
    private String address;
    private String number;
    private String email;
}
