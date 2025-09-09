package com.example.controller;

import com.example.DTO.CartDto;
import com.example.service.CartService;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Inject;

@Controller("/cart")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class CartController {

    @Inject
    private CartService cartService;

    @Get
    public CartDto view(Authentication authentication) {
        return cartService.viewCart(authentication);
    }

    public static class ModifyItemRequest { public Long itemId; public Integer quantity; }

    @Post("/items")
    public CartDto add(Authentication authentication, @Body ModifyItemRequest req) {
        return cartService.addOrUpdateItem(authentication, req.itemId, req.quantity);
    }

    @Put("/items/{itemId}")
    public CartDto update(Authentication authentication, Long itemId, @Body ModifyItemRequest req) {
        return cartService.addOrUpdateItem(authentication, itemId, req.quantity);
    }

    @Delete("/items/{itemId}")
    public CartDto remove(Authentication authentication, Long itemId) {
        return cartService.removeItem(authentication, itemId);
    }

    @Delete
    public CartDto clear(Authentication authentication) {
        return cartService.clear(authentication);
    }

    @Post("/checkout")
    public CartDto checkout(Authentication authentication) {
        return cartService.checkout(authentication);
    }
}
