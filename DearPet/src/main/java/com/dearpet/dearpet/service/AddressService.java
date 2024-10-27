package com.dearpet.dearpet.service;

import com.dearpet.dearpet.dto.AddressDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dearpet.dearpet.entity.Address;
import com.dearpet.dearpet.entity.User;
import com.dearpet.dearpet.repository.AddressRepository;
import com.dearpet.dearpet.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/*
 * Address Service
 * @Author ghpark
 * @Since 2024.10.28
 */

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    // 특정 사용자의 배송지 목록 조회
    public List<AddressDTO> getUserAddresses(String username) {
        return addressRepository.findByUser_Username(username)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 배송지 추가
    @Transactional
    public AddressDTO addUserAddress(String username, AddressDTO addressDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = Address.builder()
                .user(user)
                .address(addressDTO.getAddress())
                .name(addressDTO.getName())
                .build();

        Address savedAddress = addressRepository.save(address);
        return convertToDto(savedAddress);
    }

    // 배송지 정보 수정
    @Transactional
    public AddressDTO updateUserAddress(String username, Long addressId, AddressDTO addressDTO) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // 사용자 권한 확인
        if (!address.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to update address");
        }

        address.setAddress(addressDTO.getAddress());
        address.setName(addressDTO.getName());

        Address updatedAddress = addressRepository.save(address);
        return convertToDto(updatedAddress);
    }

    // 배송지 삭제
    @Transactional
    public void deleteUserAddress(String username, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // 사용자 권한 확인
        if (!address.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access to delete address");
        }

        addressRepository.delete(address);
    }

    // 엔티티를 DTO로 변환
    private AddressDTO convertToDto(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .address(address.getAddress())
                .name(address.getName())
                .build();
    }
}
