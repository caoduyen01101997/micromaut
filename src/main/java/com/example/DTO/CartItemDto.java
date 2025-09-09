package com.example.DTO;

public class CartItemDto {
    private Long id;
    private Long itemId;
    private String name;
    private Double unitPrice;
    private Integer quantity;
    private Double lineTotal;

    public CartItemDto() {}
    public CartItemDto(Long id, Long itemId, String name, Double unitPrice, Integer quantity, Double lineTotal) {
        this.id = id; this.itemId = itemId; this.name = name; this.unitPrice = unitPrice; this.quantity = quantity; this.lineTotal = lineTotal;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getLineTotal() { return lineTotal; }
    public void setLineTotal(Double lineTotal) { this.lineTotal = lineTotal; }
}
