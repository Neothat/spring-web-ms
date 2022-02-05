package com.geekbrains.spring.web.core.services;

import com.geekbrains.spring.web.core.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class CartServiceTest {
    @Autowired
    private CartService cartService;

    @MockBean
    private ProductsService productsService;

    @Test
    public void cartActionsTest() {
        cartService.clearCart("test_cart");
        cartService.clearCart("another_test_cart");

        Product product = new Product();
        product.setId(20L);
        product.setTitle("Cookies");
        product.setPrice(120);

        Product anotherProduct = new Product();
        anotherProduct.setId(21L);
        anotherProduct.setTitle("Another Cookies");
        anotherProduct.setPrice(121);

        Mockito.doReturn(Optional.of(product)).when(productsService).findById(20L);
        Mockito.doReturn(Optional.of(anotherProduct)).when(productsService).findById(21L);

        cartService.addToCart("test_cart", 20L);

        cartService.clearCart("test_cart");

        cartService.addToCart("test_cart", 20L);
        cartService.addToCart("test_cart", 20L);
        cartService.addToCart("test_cart", 20L);
        cartService.addToCart("test_cart", 20L);
        cartService.addToCart("test_cart", 20L);

        Mockito.verify(productsService, Mockito.times(6)).findById(ArgumentMatchers.eq(20L));
        Assertions.assertEquals(1, cartService.getCurrentCart("test_cart").getItems().size());

        cartService.decrementItem("test_cart", 20L);

        Assertions.assertEquals(1, cartService.getCurrentCart("test_cart").getItems().size());
        Assertions.assertEquals(4, cartService.getCurrentCart("test_cart").getItems().get(0).getQuantity());

        cartService.removeItemFromCart("test_cart", 20L);

        Assertions.assertEquals(0, cartService.getCurrentCart("test_cart").getItems().size());

        cartService.addToCart("test_cart", 20L);
        cartService.addToCart("test_cart", 20L);
        cartService.addToCart("another_test_cart", 21L);
        cartService.addToCart("another_test_cart", 21L);

        cartService.merge("test_cart", "another_test_cart");
        Assertions.assertEquals(2, cartService.getCurrentCart("test_cart").getItems().size());
        Assertions.assertEquals(0, cartService.getCurrentCart("another_test_cart").getItems().size());

    }

}
