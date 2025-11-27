package com.mydeseret.mydeseret.model;

import jakarta.persistence.*;
// import lombok.Data;

@Entity
@Table(name = "blueprint_items")
// @Data
public class BluePrintItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blueprint_item_id")    
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blueprint_id")
    private BluePrint bluePrint;

    @ManyToOne
    @JoinColumn(name = "input_item_id")
    private Item inputItem;

    @Column(name = "quantity_needed")
    private int quantityNeeded;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BluePrint getBluePrint() {
        return bluePrint;
    }

    public void setBluePrint(BluePrint bluePrint) {
        this.bluePrint = bluePrint;
    }

    public Item getInputItem() {
        return inputItem;
    }

    public void setInputItem(Item inputItem) {
        this.inputItem = inputItem;
    }

    public int getQuantityNeeded() {
        return quantityNeeded;
    }

    public void setQuantityNeeded(int quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }

    
}