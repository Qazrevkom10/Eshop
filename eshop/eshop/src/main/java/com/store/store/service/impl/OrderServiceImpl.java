package com.store.store.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.store.store.repository.ArticleRepository;
import com.store.store.repository.CartItemRepository;
import com.store.store.repository.OrderRepository;
import com.store.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.store.store.domain.Article;
import com.store.store.domain.CartItem;
import com.store.store.domain.Order;
import com.store.store.domain.Payment;
import com.store.store.domain.Shipping;
import com.store.store.domain.ShoppingCart;
import com.store.store.domain.User;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
    OrderRepository orderRepository;
	
	@Autowired
    CartItemRepository cartItemRepository;
	
	@Autowired
    ArticleRepository articleRepository;
			
	@Override
	@Transactional
	@CacheEvict(value = "itemcount", allEntries = true)
	public synchronized Order createOrder(ShoppingCart shoppingCart, Shipping shipping, Payment payment, User user) {
		Order order = new Order();
		order.setUser(user);
		order.setPayment(payment);
		order.setShipping(shipping);
		order.setOrderTotal(shoppingCart.getGrandTotal());
		shipping.setOrder(order);
		payment.setOrder(order);			
		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate = today.plusDays(5);				
		order.setOrderDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		order.setShippingDate(Date.from(estimatedDeliveryDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		order.setOrderStatus("In Progress");
		
		order = orderRepository.save(order);
		
		List<CartItem> cartItems = shoppingCart.getCartItems();
		for (CartItem item : cartItems) {
			Article article = item.getArticle();
			article.decreaseStock(item.getQty());
			articleRepository.save(article);
			item.setOrder(order);
			cartItemRepository.save(item);
		}		
		return order;	
	}
	
	@Override
	public Order findOrderWithDetails(Long id) {
		return orderRepository.findEagerById(id);
	}	

	public List<Order> findByUser(User user) {
		return orderRepository.findByUser(user);
	}

}
