package com.ecommerce.project.service;

import com.ecommerce.project.payload.CartDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartId);

    CartDTO updateProductQuantityInCart(Long productId, int delete);
}
