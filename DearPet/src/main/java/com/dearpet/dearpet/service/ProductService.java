package com.dearpet.dearpet.service;

import com.dearpet.dearpet.dto.ProductDTO;
import com.dearpet.dearpet.entity.Category;
import com.dearpet.dearpet.entity.Product;
import com.dearpet.dearpet.repository.CategoryRepository;
import com.dearpet.dearpet.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
 * product Service
 * @Author 위지훈
 * @Since 2024.10.25
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // 전체 상품 목록 조회
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::convertToProductDTO).collect(Collectors.toList());
    }

    // 상품 등록
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();

        updateProductFields(product, productDTO);

        productRepository.save(product);
        return convertToProductDTO(product);
    }

    // 상품 상세 정보 조회
    public ProductDTO getProductDTO(Long productId) {
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToProductDTO(product);
    }

    // 상품 정보 수정
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new RuntimeException("Pet not found"));

        updateProductFields(product, productDTO);

        Product updateProduct = productRepository.save(product);
        return convertToProductDTO(updateProduct);
    }

    // 상품 삭제
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    // 특정 카테고리의 상품 조회
    public List<ProductDTO> getProductByCategoryId(Long categoryId) {
        List<Product> product = productRepository.findByCategoryCategoryId(categoryId);
        return product.stream().map(this::convertToProductDTO).collect(Collectors.toList());
    }

    // 상품 수정/등록에서 중복되는 부분을 따로 추출 해서 따로 메서드로 만듬
    private void updateProductFields(Product product, ProductDTO productDTO) {
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setQuantity(productDTO.getQuantity());
        product.setStatus(productDTO.getStatus());

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);
    }

    // 상품 Entity -> DTO 변환
    private ProductDTO convertToProductDTO(Product product) {
        return new ProductDTO(product.getProductId(), product.getName(),
                product.getPrice(), product.getDescription(), product.getImage(),
                product.getQuantity(), product.getStatus(), product.getCategory().getCategoryId());

    }
}
