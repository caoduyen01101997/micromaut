package com.example.service;
import com.example.document.Item;
import com.example.repository.ItemRepository;
import com.example.util.IdUtil;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

@Singleton
public class ItemService {

    @Inject
    EntityManager entityManager;

    @Inject
    private ItemRepository itemRepository;

    @Transactional
    public Page<Item> list(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    @Transactional
    public Item save(Item item) {
        Long id = item.getId();
        if(id == null){
            id = IdUtil.generateId();
        }
        if(item != null){
            if(item.getId() == null){
                Item managedItem = entityManager.find(Item.class, id);
                if (managedItem == null) {
                    // item.setId(IdUtil.generateId());
                     entityManager.persist(item);
                     entityManager.flush();
                } 
            }else{
                entityManager.merge(item);
            }
        }

        return item;
        
    }

    @Transactional
    public Optional<Item> find(@NonNull Long id) {
        return itemRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }
}
