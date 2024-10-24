package com.dearpet.dearpet.service;

import com.dearpet.dearpet.dto.PetDTO;
import com.dearpet.dearpet.entity.Pet;
import com.dearpet.dearpet.entity.User;
import com.dearpet.dearpet.repository.PetRepository;
import com.dearpet.dearpet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
 * Pet Service
 * @Author 위지훈
 * @Since 2024.10.23
 */
@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    // 사용자 Id로 반려동물 목록 조회
    public List<PetDTO> getPetsByUserId(Long userId) {
        List<Pet> pets = petRepository.findByUserUserId(userId);
        return pets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 반려동물 등록
    public PetDTO createPet(PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setName(petDTO.getName());
        pet.setSpecies(petDTO.getSpecies());
        pet.setAge(petDTO.getAge());
        pet.setGender(petDTO.getGender());
        pet.setNeutered(petDTO.isNeutered());
        pet.setWeight(petDTO.getWeight());
        pet.setHealthStatus(petDTO.getHealthStatus());

        User user = userRepository.findById(petDTO.getUserId()).
                orElseThrow(() -> new RuntimeException("User not found"));
        ;
        pet.setUser(user);

        petRepository.save(pet);
        return convertToDTO(pet);
    }

    // 특정 반려동물 정보 조회
    public PetDTO getPetById(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        return convertToDTO(pet);
    }

    // 반려동물 정보 수정
    public PetDTO updatePet(Long petId, PetDTO petDTO) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        pet.setName(petDTO.getName());
        pet.setSpecies(petDTO.getSpecies());
        pet.setAge(petDTO.getAge());
        pet.setNeutered(petDTO.isNeutered());
        pet.setGender(petDTO.getGender());
        pet.setWeight(petDTO.getWeight());
        pet.setHealthStatus(petDTO.getHealthStatus());

        Pet updatedPet = petRepository.save(pet);
        return convertToDTO(updatedPet);
    }

    // 반려동물 삭제
    public void deletePet(Long petId) {
        petRepository.deleteById(petId);
    }

    // pet Entity -> DTO 변환
    private PetDTO convertToDTO(Pet pet) {
        return new PetDTO(pet.getPetId(), pet.getName(), pet.getSpecies(),
                pet.getAge(), pet.isNeutered(), pet.getGender(),
                pet.getWeight(), pet.getHealthStatus(), pet.getUser().getUserId());
    }
}
