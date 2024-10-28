package com.dearpet.dearpet.repository;

import com.dearpet.dearpet.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/*
 * Address Repository
 * @Author ghpark
 * @Since 2024.10.28
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser_Username(String username); // 특정 사용자의 주소 목록 조회
}
