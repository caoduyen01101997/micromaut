
package com.example;

import com.example.document.Item;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;


@MicronautTest
public class ItemTest {

    @Test
    public void testItemGettersAndSetters() {
        Item item = new Item();
        
        Long id = 1L;
        String name = "Sample Item";
        Double priceSell = 50.0;
        Double priceBuy = 30.0;

        item.setId(id);
        item.setName(name);
        item.setPriceSell(priceSell);
        item.setPriceBuy(priceBuy);

        assertEquals(id, item.getId());
        assertEquals(name, item.getName());
        assertEquals(priceSell, item.getPriceSell());
        assertEquals(priceBuy, item.getPriceBuy());
    }
}