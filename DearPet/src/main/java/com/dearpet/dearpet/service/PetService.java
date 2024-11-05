package com.dearpet.dearpet.service;

import com.dearpet.dearpet.dto.PetDTO;
import com.dearpet.dearpet.dto.UserDTO;
import com.dearpet.dearpet.entity.Pet;
import com.dearpet.dearpet.entity.User;
import com.dearpet.dearpet.repository.PetRepository;
import com.dearpet.dearpet.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
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

    @Autowired
    private WebClient webClient;

    // 사용자가 보유한 반려동물 목록 조회
    public List<PetDTO> getPetsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Pet> pets = petRepository.findByUserUserId(user.getUserId());
        return pets.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 반려동물 등록
    public PetDTO createPet(PetDTO petDTO, String username) {
        Pet pet = new Pet();
        pet.setName(petDTO.getName());
        pet.setSpecies(petDTO.getSpecies());
        pet.setAge(petDTO.getAge());
        pet.setGender(petDTO.getGender());
        pet.setNeutered(petDTO.getNeutered());
        pet.setWeight(petDTO.getWeight());
        pet.setHealthStatus(petDTO.getHealthStatus());

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

        if (petDTO.getName() != null) {
            pet.setName(petDTO.getName());
        }
        if (petDTO.getSpecies() != null) {
            pet.setSpecies(petDTO.getSpecies());
        }
        if (petDTO.getAge() != null) {
            pet.setAge(petDTO.getAge());
        }
        if (petDTO.getNeutered() != null) {
            pet.setNeutered(petDTO.getNeutered());
        }
        if (petDTO.getGender() != null) {
            pet.setGender(petDTO.getGender());
        }
        if (petDTO.getWeight() != null) {
            pet.setWeight(petDTO.getWeight());
        }
        if (petDTO.getHealthStatus() != null) {
            pet.setHealthStatus(petDTO.getHealthStatus());
        }

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
                pet.getAge(), pet.getNeutered(), pet.getGender(),
                pet.getWeight(), pet.getHealthStatus(), pet.getUser().getUserId());
    }

    // 자동으로 반려동물 상태 정보를 가져와 ChatGPT에 전달
    public String getHealthAdvice(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 사용자의 모든 반려동물 상태 정보 생성
        List<Pet> pets = petRepository.findByUserUserId(user.getUserId());
        String petStatusSummary = createPetStatusSummary(pets);

        // ChatGPT로 요청 전송
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4-turbo",
                "messages", List.of(Map.of("role", "user", "content", petStatusSummary))
        );

        try {
            return webClient.post()
                    .uri("/v1/chat/completions")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .map(response -> response.get("choices").get(0).get("message").get("content").asText())
                    .onErrorReturn("건강 관리 조언을 가져오는 데 실패했습니다. 잠시 후 다시 시도해주세요.")
                    .block();
        } catch (Exception e) {
            return "OpenAI API 호출 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    // 반려동물 상태 요약 문자열 생성
    private String createPetStatusSummary(List<Pet> pets) {
        StringBuilder status = new StringBuilder();
        for (Pet pet : pets) {
            status.append("이름: ").append(pet.getName()).append(", ")
                    .append("나이: ").append(pet.getAge()).append(", ")
                    .append("체중: ").append(pet.getWeight()).append(", ")
                    .append("종류:").append(pet.getSpecies()).append(", ")
                    .append("성별: ").append(pet.getGender()).append(", ")
                    .append("중성화여부: ").append(pet.getNeutered()).append(", ")
                    .append("건강상태: ").append(pet.getHealthStatus()).append("\n")
                    .append("각 동물마다 ㅇㅇ의 경우에는 으로 시작해서 나이, 체중, 종별 주의사항, 건강상태, 성별" +
                            " 모두 고려해서 건강관리 할 수 있게끔 조언해줘");
        }
        return status.toString();
    }
}
