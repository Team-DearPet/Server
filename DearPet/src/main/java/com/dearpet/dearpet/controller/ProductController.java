package com.dearpet.dearpet.controller;

import com.dearpet.dearpet.dto.ProductDTO;
import com.dearpet.dearpet.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * product Controller
 * @Author 위지훈
 * @Since 2024.10.25
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    ProductService productService;

    // 전체 상품 목록 조회
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productList = productService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    // 상품 등록
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(createdProduct);
    }

    // 상품 상세 정보 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Long productId) {
        ProductDTO productDTO = productService.getProductDTO(productId);
        return ResponseEntity.ok(productDTO);
    }

    // 상품 정보 수정
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    // 특정 카테고리의 상품 조회
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable("categoryId") Long categoryId) {
        List<ProductDTO> productList = productService.getProductByCategoryId(categoryId);
        return ResponseEntity.ok(productList);
    }
}
