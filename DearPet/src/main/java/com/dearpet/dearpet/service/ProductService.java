package com.dearpet.dearpet.service;

import com.dearpet.dearpet.dto.ProductDTO;
import com.dearpet.dearpet.entity.Category;
import com.dearpet.dearpet.entity.Product;
import com.dearpet.dearpet.repository.CategoryRepository;
import com.dearpet.dearpet.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setImage(productDTO.getImage());
        product.setQuantity(productDTO.getQuantity());
        product.setStatus(productDTO.getStatus());
        product.setDiscount(productDTO.getDiscount() != null ? productDTO.getDiscount() : BigDecimal.ZERO); // 할인율 설정
        product.setSeller(productDTO.getSeller()); // 판매자 정보 설정

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

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
                orElseThrow(() -> new RuntimeException("Product not found"));

        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getPrice() != null) {
            product.setPrice(productDTO.getPrice());
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getImage() != null) {
            product.setImage(productDTO.getImage());
        }
        if (productDTO.getQuantity() != null) {
            product.setQuantity(productDTO.getQuantity());
        }
        if (productDTO.getStatus() != null) {
            product.setStatus(productDTO.getStatus());
        }
        if (productDTO.getDiscount() != null) {
            product.setDiscount(productDTO.getDiscount());
        }
        if (productDTO.getSeller() != null) {
            product.setSeller(productDTO.getSeller());
        }

        if (productDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

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

    // 상품 Entity -> DTO 변환
    private ProductDTO convertToProductDTO(Product product) {
        return new ProductDTO(product.getProductId(), product.getName(),
                product.getPrice(), product.getDescription(), product.getImage(),
                product.getQuantity(), product.getStatus(), product.getCategory().getCategoryId(),
                product.getDiscount(), product.getSeller());
    }
}