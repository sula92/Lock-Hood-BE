package com.t6.lockhood.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Inventory {

    @Id
    String rowMaterialId;
    String name;
    int avilableQuantity;
    int unitValue;
   /* @ManyToOne
    @JoinColumn(name = "factory_id", referencedColumnName = "id")
    Factory factory;*/


}
