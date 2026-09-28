package com.t6.lockhood.model;

import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence")
    @SequenceGenerator(name = "hibernate_sequence", sequenceName = "hibernate_sequence", allocationSize = 1)
    int stockId;
    int rowMaterialId;
    String name;
    int avilableQuantity;
    int unitValue;
   /* @ManyToOne
    @JoinColumn(name = "factory_id", referencedColumnName = "id")
    Factory factory;*/


}
