package com.t6.lockhood.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int stockId;
    int rowMaterialId;
    String name;
    int avilableQuantity;
    int unitValue;
   /* @ManyToOne
    @JoinColumn(name = "factory_id", referencedColumnName = "id")
    Factory factory;*/


}
