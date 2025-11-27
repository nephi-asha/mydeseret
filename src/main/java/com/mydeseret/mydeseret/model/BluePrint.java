package com.mydeseret.mydeseret.model;

import jakarta.persistence.*;
// import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blueprints")
// @Data
public class BluePrint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blueprint_id")
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "output_item_id", nullable = false)
    private Item outputItem;

    private int outputQuantity = 1;

    @OneToMany(mappedBy = "bluePrint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BluePrintItem> components = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Item getOutputItem() {
        return outputItem;
    }

    public void setOutputItem(Item outputItem) {
        this.outputItem = outputItem;
    }

    public int getOutputQuantity() {
        return outputQuantity;
    }

    public void setOutputQuantity(int outputQuantity) {
        this.outputQuantity = outputQuantity;
    }

    public List<BluePrintItem> getComponents() {
        return components;
    }

    public void setComponents(List<BluePrintItem> components) {
        this.components = components;
    }

}