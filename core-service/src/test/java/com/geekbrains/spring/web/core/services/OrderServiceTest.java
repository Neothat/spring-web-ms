package com.geekbrains.spring.web.core.services;

import com.geekbrains.spring.web.core.dto.OrderDetailsDto;
import com.geekbrains.spring.web.core.entities.OrderItem;
import com.geekbrains.spring.web.core.entities.Product;
import com.geekbrains.spring.web.core.repositories.OrdersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class OrderServiceTest {

    private static final String USERNAME = "username";
    private static final String CART = "SPRING_WEB_" + USERNAME;

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @MockBean
    private ProductsService productsService;

    @Test
    public void createOrderTest() {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
        orderDetailsDto.setAddress("address");
        orderDetailsDto.setPhone("phone");

        Product product = new Product();
        product.setId(20L);
        product.setTitle("Cookies");
        product.setPrice(120);

        Product anotherProduct = new Product();
        anotherProduct.setId(21L);
        anotherProduct.setTitle("Another Cookies");
        anotherProduct.setPrice(121);

        cartService.clearCart(CART);
//        Mockito.doReturn(CART).when(cartService).getCartUuidFromSuffix(Mockito.anyString());
        Mockito.doReturn(Optional.of(product)).when(productsService).findById(20L);
        Mockito.doReturn(Optional.of(anotherProduct)).when(productsService).findById(21L);
        cartService.addToCart(CART, 20L);
        cartService.addToCart(CART, 21L);

        cartService.getCurrentCart(CART).setTotalPrice(241);

        orderService.createOrder(USERNAME, orderDetailsDto);

        Assertions.assertEquals(1, ordersRepository.findAllByUsername(USERNAME).size());
        Assertions.assertEquals(0, cartService.getCurrentCart(CART).getItems().size());
    }
}
