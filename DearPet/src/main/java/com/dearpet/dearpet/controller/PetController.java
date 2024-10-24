package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.dto.PetDTO;
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
public class PetController {

    @Autowired
    private PetService petService;

    // 사용자 Id로 사용자가 보유한 반려동물 정보 조회
    /*@GetMapping
    public ResponseEntity<List<PetDTO>> getPetsByUserId(HttpServletRequest request){
        String token = extractToken(request);
        Long userId = jwUtil.extractUserId(token); // Jspn web token정보에서 userId추출하기

        List<PetDTO> petList = petService.getPetsByUserId(userId);
        return ResponseEntity.ok(petList);
    }
*/

    // 반려동물 등록
    @PostMapping
    public ResponseEntity<PetDTO> createPet(@RequestBody PetDTO petDTO){
        PetDTO createdPet = petService.createPet(petDTO);
        return ResponseEntity.ok(createdPet);
    }

    // 특정 반려동물 정보 조회
    @GetMapping("/{petId}")
    public ResponseEntity<PetDTO> getPet(@PathVariable Long petId){
        PetDTO petDto = petService.getPetById(petId);
        return ResponseEntity.ok(petDto);
    }

    // 특정 반려동물 정보 수정
    @PatchMapping("/{petId}")
    public ResponseEntity<PetDTO> updatePet(@PathVariable Long petId, @RequestBody PetDTO petDTO){
        PetDTO petDto = petService.updatePet(petId, petDTO);
        return ResponseEntity.ok(petDto);
    }

    // 반려동물 삭제
    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable Long petId){
        petService.deletePet(petId);
        return ResponseEntity.noContent().build();
    }
}
