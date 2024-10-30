package com.dearpet.dearpet.dto;

import lombok.*;

/*
 * Address DTO
 * @Author ghpark
 * @Since 2024.10.28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    private Long addressId;
    private String address;
    private String name;
    private Boolean defaultAddress;
}
