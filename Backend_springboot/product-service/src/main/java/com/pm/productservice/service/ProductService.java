package com.pm.productservice.service;

import com.pm.productservice.dto.ProductRequestDTO;
import com.pm.productservice.dto.ProductResponseDTO;
import com.pm.productservice.model.Product;
import com.pm.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
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
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found: {}", id);
                    return new RuntimeException("Product not found:" +
                            " " + id);
                });

        return modelMapper.map(product, ProductResponseDTO.class);
    }

    //create product
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
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
    // Get product list (with pagination and search)

    public Page<ProductResponseDTO> getProducts(Pageable pageable, String search, String category) {
        Page<Product> products;

        if (search != null && category != null) {
            products = productRepository.findByNameContainingAndCategory(search, category, pageable);
        } else if (search != null) {
            products = productRepository.findByNameContaining(search, pageable);
        } else if (category != null) {
            products = productRepository.findByCategory(category, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return products.map(product -> modelMapper.map(product, ProductResponseDTO.class));
    }

    // update product
    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO requestDTO) {

        return productRepository.findById(id)
                .map(existingProduct -> {
                    if (!existingProduct.getName().equals(requestDTO.getName()) &&
                            productRepository.existsByName(requestDTO.getName())) {
                        throw new RuntimeException("Product name already exists: " + requestDTO.getName());
                    }

                    existingProduct.setName(requestDTO.getName());
                    existingProduct.setDescription(requestDTO.getDescription());
                    existingProduct.setPrice(requestDTO.getPrice());
                    existingProduct.setCategory(requestDTO.getCategory());
                    existingProduct.setStockQuantity(requestDTO.getStockQuantity());

                    Product updatedProduct = productRepository.save(existingProduct);

                    return modelMapper.map(updatedProduct, ProductResponseDTO.class);
                })
                .orElseThrow(() -> {
                    return new RuntimeException("Product not found: " + id);
                });
    }
    // delete product
    public void deleteProduct(UUID id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found: " + id);
        }
    }



}
