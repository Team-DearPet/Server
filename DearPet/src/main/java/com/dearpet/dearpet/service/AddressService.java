package com.dearpet.dearpet.service;

import com.dearpet.dearpet.dto.AddressDTO;
import com.dearpet.dearpet.entity.Address;
import com.dearpet.dearpet.entity.User;
import com.dearpet.dearpet.repository.AddressRepository;
import com.dearpet.dearpet.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                .defaultAddress(false)  // 기본값을 false로 설정
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

        // 기본 주소지로 설정하려는 경우, 기존 모든 주소지의 defaultAddress를 false로 설정
        if (Boolean.TRUE.equals(addressDTO.getDefaultAddress())) {
            addressRepository.findByUser_Username(username)
                    .forEach(existingAddress -> {
                        existingAddress.setDefaultAddress(false);
                        addressRepository.save(existingAddress);
                    });
            address.setDefaultAddress(true);  // 현재 주소를 기본 주소지로 설정
        } else {
            address.setDefaultAddress(false);
        }

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
                .addressId(address.getAddressId())
                .address(address.getAddress())
                .defaultAddress(address.getDefaultAddress())  // 기본 주소지 여부 포함
                .build();
    }
}