package com.example.WebShopTP.service;

import com.example.WebShopTP.entities.Product;
import com.example.WebShopTP.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    public Product addProduct(String name, Integer quantity) {
        Product product = new Product();
        product.setName(name);
        product.setQuantity(quantity);
        return productRepo.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public boolean deleteProduct(Long id) {
        if (!productRepo.existsById(id))
            return false;
        productRepo.deleteById(id);
        return true;
    }

    public boolean updateProduct(Long id, String name, Integer quantity) {
        Optional<Product> productOpt = productRepo.findById(id);
        if (!productOpt.isPresent())
            return false;

        Product product = productOpt.get();
        if (name != null && !name.isEmpty()) {
            product.setName(name);
        }
        if (quantity != null && quantity >= 0) {
            product.setQuantity(quantity);
        }
        productRepo.save(product);
        return true;
    }

    public boolean decreaseQuantity(Long productId, Integer quantity) {
        Optional<Product> productOpt = productRepo.findById(productId);
        if (!productOpt.isPresent())
            return false;

        Product product = productOpt.get();
        if (product.getQuantity() < quantity)
            return false; // Недостаточно товара

        product.setQuantity(product.getQuantity() - quantity);
        productRepo.save(product);
        return true;
    }

    public Optional<Product> getProduct(Long id) {
        return productRepo.findById(id);
    }
}