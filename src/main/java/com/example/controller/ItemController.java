package com.example.controller;
import com.example.document.Item;
import com.example.service.ItemService;

import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import javax.validation.Valid;

import java.io.IOException;
import java.util.Optional;

@Controller("/items")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class ItemController {

    @Inject
    private ItemService itemService;

    @Get
    public HttpResponse<?> list(Pageable page) {
        return HttpResponse.ok(itemService.list(page));
    }

    @Post(consumes = MediaType.MULTIPART_FORM_DATA)
    public HttpResponse<?> save(
        @Part("name") String name,
        @Part("price-sell") Double priceSell,
        @Part("price-buy") Double  priceBuy,
        @Part("image") CompletedFileUpload image
    ) {
        Item item = new Item();
        if (image != null) {
            try {
                String imageString = new String();
                item.setImage(image.getBytes());
                item.setName(name);
                item.setPriceBuy(priceBuy);
                item.setPriceSell(priceSell);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Item savedItem = itemService.save(item);
        return HttpResponse.created(savedItem);
    }

    @Get("/{id}")
    public HttpResponse<?> find(@PathVariable Long id) {
        Optional<Item> item = itemService.find(id);
        return item.map(HttpResponse::ok)
                   .orElse(HttpResponse.notFound());
    }

    @Put("/{id}")
    public HttpResponse<?> update(@PathVariable Long id, @Body @Valid Item item) {
        item.setId(id);
        Item updatedItem = itemService.save(item);
        return HttpResponse.ok(updatedItem);
    }

    @Delete("/{id}")
    public HttpResponse<?> delete(@PathVariable Long id) {
        itemService.delete(id);
        return HttpResponse.noContent();
    }
}
