package com.example.WebShopTP.service;

import com.example.WebShopTP.entities.Order;
import com.example.WebShopTP.entities.Product;
import com.example.WebShopTP.repository.OrderRepository;
import com.example.WebShopTP.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ProductService productService;

    public Order addOrder(Long productId, Integer quantity, String customer, String status) throws Exception {
        // Проверяем наличие товара
        Optional<Product> productOpt = productRepo.findById(productId);
        if (!productOpt.isPresent()) {
            throw new Exception("Товар не найден");
        }

        Product product = productOpt.get();
        if (product.getQuantity() < quantity) {
            throw new Exception("Недостаточно товара. Доступно: " + product.getQuantity());
        }

        // Уменьшаем количество товара
        if (!productService.decreaseQuantity(productId, quantity)) {
            throw new Exception("Ошибка при обновлении товара");
        }

        // Создаём заказ
        Order order = new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setCustomer(customer);
        order.setStatus(status);
        return orderRepo.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public boolean deleteOrder(Long id) {
        if (!orderRepo.existsById(id))
            return false;

        // Если удаляем заказ, нужно вернуть товар
        Optional<Order> orderOpt = orderRepo.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            Optional<Product> productOpt = productRepo.findById(order.getProductId());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                product.setQuantity(product.getQuantity() + order.getQuantity());
                productRepo.save(product);
            }
        }

        orderRepo.deleteById(id);
        return true;
    }

    public boolean updateOrder(Long id, String status, String courier) {
        Optional<Order> orderOpt = orderRepo.findById(id);
        if (!orderOpt.isPresent())
            return false;

        Order order = orderOpt.get();
        if (status != null)
            order.setStatus(status);
        if (courier != null)
            order.setCourier(courier);

        orderRepo.save(order);
        return true;
    }

    public List<Order> getOrdersByCustomer(String customer) {
        return orderRepo.findAll().stream()
                .filter(o -> customer.equals(o.getCustomer()))
                .toList();
    }

    public List<Order> getOrdersByCourier(String courier) {
        return orderRepo.findAll().stream()
                .filter(o -> o.getCourier() != null && courier.equals(o.getCourier()))
                .toList();
    }
}