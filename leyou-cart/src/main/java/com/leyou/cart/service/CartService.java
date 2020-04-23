package com.leyou.cart.service;

import com.leyou.cart.pojo.Cart;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/4/12.
 */
public interface CartService {
    void addCart(Cart cart);

    List<Cart> queryCart();

    void updateCartNum(Cart cart);

    void delCart(String id);
}
