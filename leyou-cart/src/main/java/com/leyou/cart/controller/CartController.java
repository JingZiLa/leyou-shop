package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Mirror
 * @CreateDate 2020/4/12.
 *购物车控制层
 */
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     * @param cart
     * @return
     */
    @PostMapping("/addCart")
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        this.cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询购物车商品
     * @return
     */
    @GetMapping("/queryCart")
    public ResponseEntity<List<Cart>> queryCart(){
        List<Cart> carts = this.cartService.queryCart();

        if (CollectionUtils.isEmpty(carts)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carts);
    }

    /**
     * 更新购物车商品数量
     * @param cart
     * @return
     */
    @PostMapping("/updateCartNum")
    public ResponseEntity<Void> updateCartNum(@RequestBody Cart cart) {
        this.cartService.updateCartNum(cart);
        return ResponseEntity.noContent().build();
    }

    /**
     * 删除购物车商品
     * @param id
     * @return
     */
    @GetMapping("/delCart")
    public ResponseEntity<Void> delCart(String id) {
        this.cartService.delCart(id);
        return ResponseEntity.noContent().build();
    }
}
