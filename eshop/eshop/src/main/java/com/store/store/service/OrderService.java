package com.store.store.service;

import java.util.List;

import com.store.store.domain.Order;
import com.store.store.domain.Payment;
import com.store.store.domain.Shipping;
import com.store.store.domain.ShoppingCart;
import com.store.store.domain.User;

public interface OrderService {

	Order createOrder(ShoppingCart shoppingCart, Shipping shippingAddress, Payment payment, User user);
	
	List<Order> findByUser(User user);
	
	Order findOrderWithDetails(Long id);
}
