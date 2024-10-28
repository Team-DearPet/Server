package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.dto.PetDTO;
import com.dearpet.dearpet.security.JwtTokenProvider;
import com.dearpet.dearpet.service.PetService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Pet Controller
 * @Author 위지훈
 * @Since 2024.10.23
 */
@RestController
@RequestMapping("/api/pets")
@CrossOrigin("*")
public class PetController {

    @Autowired
    private PetService petService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 사용자가 보유한 반려동물 목록 조회
    @GetMapping
    public ResponseEntity<List<PetDTO>> getPetsByUsername(@RequestHeader("Authorization") String token){
        String username = jwtTokenProvider.getUsername(token);

        List<PetDTO> petList = petService.getPetsByUsername(username);
        return ResponseEntity.ok(petList);
    }


    // 반려동물 등록
    @PostMapping
    public ResponseEntity<PetDTO> createPet(@RequestBody PetDTO petDTO){
        PetDTO createdPet = petService.createPet(petDTO);
        return ResponseEntity.ok(createdPet);
    }

    // 특정 반려동물 정보 조회
    @GetMapping("/{petId}")
    public ResponseEntity<PetDTO> getPet(@PathVariable("pet_id") Long petId){
        PetDTO petDto = petService.getPetById(petId);
        return ResponseEntity.ok(petDto);
    }

    // 특정 반려동물 정보 수정
    @PatchMapping("/{petId}")
    public ResponseEntity<PetDTO> updatePet(@PathVariable("pet_id") Long petId, @RequestBody PetDTO petDTO){
        PetDTO petDto = petService.updatePet(petId, petDTO);
        return ResponseEntity.ok(petDto);
    }

    // 반려동물 삭제
    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable("pet_id") Long petId){
        petService.deletePet(petId);
        return ResponseEntity.noContent().build();
    }
}
