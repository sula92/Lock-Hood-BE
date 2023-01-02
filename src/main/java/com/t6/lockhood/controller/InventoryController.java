package com.t6.lockhood.controller;

import com.t6.lockhood.model.Inventory;
import com.t6.lockhood.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(
        value = "/api",
        produces = "application/json")

@CrossOrigin(origins = {
        "*"

},
        allowedHeaders = "*",

        maxAge = 15 * 60,
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.DELETE,
                RequestMethod.PUT
        })
@Transactional
public class InventoryController {

        @Autowired
        InventoryRepository inventoryRepository;

        @GetMapping("/inventories")
        List<Inventory> getAllInventories(){
                return inventoryRepository.findAll();
        }

        @PostMapping("/inventories")
        Inventory saveInventory(@RequestBody Inventory inventory){
                return inventoryRepository.save(inventory);
        }

        @PutMapping("/inventories/{id}")
        Inventory editInventory(@PathVariable int id, @RequestBody Inventory inventory){
                Inventory inventorUpdated=inventoryRepository.findById(id).get();
                inventorUpdated.setAvilableQuantity(inventory.getAvilableQuantity());
                inventorUpdated.setName(inventory.getName());
                return inventoryRepository.save(inventory);
        }

        @DeleteMapping("/inventories/{id}")
        void deleteInventory(@PathVariable int id){
                inventoryRepository.deleteById(id);
        }
}
