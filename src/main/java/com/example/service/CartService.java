package com.example.service;

import com.example.document.*;
import com.example.repository.*;
import com.example.DTO.*;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import jakarta.inject.Inject;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.HttpStatus;

import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository; // assume exists if needed else we'll resolve differently
    private final UserRepository userRepository;

    @Inject
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    private Long resolveUserId(Authentication auth) {
        Object uid = auth.getAttributes().get("userId");
        if (uid instanceof Number) return ((Number) uid).longValue();
        String username = auth.getName();
        return userRepository.findByUsername(username).map(User::getId)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private Cart getOrCreateActiveCart(Long userId) {
        return cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart c = new Cart();
                    c.setUserId(userId);
                    c.setStatus(CartStatus.ACTIVE);
                    return cartRepository.save(c);
                });
    }

    public CartDto viewCart(Authentication auth) {
        Long userId = resolveUserId(auth);
        Cart cart = getOrCreateActiveCart(userId);
        return toDto(cart);
    }

    public CartDto addOrUpdateItem(Authentication auth, Long itemId, int quantity) {
        if (quantity <= 0) {
            return removeItem(auth, itemId);
        }
        Long userId = resolveUserId(auth);
        Cart cart = getOrCreateActiveCart(userId);
        // load item
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isEmpty()) throw new HttpStatusException(HttpStatus.NOT_FOUND, "Item not found");
        Item item = itemOpt.get();
        CartItem found = cart.getItems().stream().filter(ci -> ci.getItem().getId().equals(itemId)).findFirst().orElse(null);
        if (found == null) {
            found = new CartItem();
            found.setCart(cart);
            found.setItem(item);
            found.setItemName(item.getName()); // snapshot
            found.setUnitPrice(item.getPriceSell() != null ? item.getPriceSell() : 0d); // snapshot
            found.setQuantity(quantity);
            found.setLineTotal(found.getUnitPrice() * found.getQuantity());
            cart.getItems().add(found);
        } else {
            found.setQuantity(quantity);
            found.setLineTotal(found.getUnitPrice() * found.getQuantity());
        }
        cart.setUpdatedAt(new Date());
        cartRepository.update(cart);
        return toDto(cart);
    }

    public CartDto removeItem(Authentication auth, Long itemId) {
        Long userId = resolveUserId(auth);
        Cart cart = getOrCreateActiveCart(userId);
    cart.getItems().removeIf(ci -> ci.getItem().getId().equals(itemId));
        cart.setUpdatedAt(new Date());
        cartRepository.update(cart);
        return toDto(cart);
    }

    public CartDto clear(Authentication auth) {
        Long userId = resolveUserId(auth);
        Cart cart = getOrCreateActiveCart(userId);
        cart.getItems().clear();
        cart.setUpdatedAt(new Date());
        cartRepository.update(cart);
        return toDto(cart);
    }

    public CartDto checkout(Authentication auth) {
        Long userId = resolveUserId(auth);
        Cart cart = getOrCreateActiveCart(userId);
        if (cart.getItems().isEmpty()) throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Cart empty");
        cart.setStatus(CartStatus.CHECKED_OUT);
        cart.setUpdatedAt(new Date());
        cartRepository.update(cart);
        return toDto(cart);
    }

    private CartDto toDto(Cart cart) {
        double total = cart.getItems().stream().mapToDouble(ci -> ci.getLineTotal() != null ? ci.getLineTotal() : 0d).sum();
    List<CartItemDto> items = cart.getItems().stream()
        .map(ci -> new CartItemDto(ci.getId(), ci.getItem().getId(), ci.getItemName(), ci.getUnitPrice(), ci.getQuantity(), ci.getLineTotal()))
                .collect(Collectors.toList());
        return new CartDto(cart.getId(), cart.getStatus().name(), total, items);
    }
}
