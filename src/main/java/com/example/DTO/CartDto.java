package com.example.DTO;

import java.util.List;

public class CartDto {
    private Long id;
    private String status;
    private Double total;
    private List<CartItemDto> items;

    public CartDto() {}
    public CartDto(Long id, String status, Double total, List<CartItemDto> items) {
        this.id = id; this.status = status; this.total = total; this.items = items;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public List<CartItemDto> getItems() { return items; }
    public void setItems(List<CartItemDto> items) { this.items = items; }
}
