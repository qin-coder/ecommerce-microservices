package com.pm.productservice.service;

import com.pm.productservice.dto.ProductRequestDTO;
import com.pm.productservice.dto.ProductResponseDTO;
import com.pm.productservice.model.Product;
import com.pm.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    // get all products
    public List<ProductResponseDTO> getAllProducts() {
        log.info("Fetching all products");

        return productRepository.findAll()
                .stream()
                .map(product -> modelMapper.map(product,
                        ProductResponseDTO.class))
                .collect(Collectors.toList());
    }
    //get single product
    @Transactional
    public ProductResponseDTO getProductById(UUID id) {
        log.info("Fetching product by ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found: {}", id);
                    return new RuntimeException("Product not found: " + id);
                });

        return modelMapper.map(product, ProductResponseDTO.class);
    }

    //create product
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        log.info("Creating new product: {}",
                productRequestDTO.getName());
        // check if the product is existing
        if (productRepository.existsByName(productRequestDTO.getName())) {
            throw new RuntimeException("Product name already " +
                    "exists: " + productRequestDTO.getName());
        }
        Product product = modelMapper.map(productRequestDTO,
                Product.class);
        //save the product
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully: {}",
                savedProduct.getId());
        return modelMapper.map(savedProduct,
                ProductResponseDTO.class);
    }


}
