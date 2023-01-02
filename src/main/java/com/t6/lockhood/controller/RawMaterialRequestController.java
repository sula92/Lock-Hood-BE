package com.t6.lockhood.controller;

import com.t6.lockhood.model.Inventory;
import com.t6.lockhood.model.RawMaterialRequest;
import com.t6.lockhood.repository.InventoryRepository;
import com.t6.lockhood.repository.RawMaterialRequestRepository;
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
public class RawMaterialRequestController {

    @Autowired
    RawMaterialRequestRepository rawMaterialRequestRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @GetMapping("/request")
    List<RawMaterialRequest> getAllRequests() {
        return rawMaterialRequestRepository.findAll();
    }

    @PostMapping("/request")
    RawMaterialRequest doRequest(@RequestBody RawMaterialRequest rawMaterialRequest) {
        Inventory inventory = inventoryRepository.findById(rawMaterialRequest.getId()).get();
        int availableQty = inventory.getAvilableQuantity();
        int requestedQty = rawMaterialRequest.getAmount();
        inventory.setAvilableQuantity(availableQty - requestedQty);
        inventoryRepository.save(inventory);
        return rawMaterialRequestRepository.save(rawMaterialRequest);
    }

    @PutMapping("/request/{id}")
    RawMaterialRequest editRequest(@PathVariable int id, @RequestBody RawMaterialRequest rawMaterialRequest) {
        Inventory inventory = inventoryRepository.findById(rawMaterialRequest.getId()).get();
        RawMaterialRequest rawMaterialRequestOld = rawMaterialRequestRepository.findById(id).get();

        int availableQty = inventory.getAvilableQuantity();
        int requestedQty = rawMaterialRequest.getAmount();
        inventory.setAvilableQuantity(availableQty + rawMaterialRequestOld.getAmount() - requestedQty);

        RawMaterialRequest rawMaterialRequestNew=rawMaterialRequestOld.builder()
                .amount(rawMaterialRequest.getAmount())
                .date(rawMaterialRequest.getDate())
                .taskId(rawMaterialRequest.getTaskId())
                .build();

        inventoryRepository.save(inventory);
        return rawMaterialRequestRepository.save(rawMaterialRequestNew);
    }

    @DeleteMapping("request/{id}")
    public void deleteRequest(@PathVariable int id){
        rawMaterialRequestRepository.deleteById(id);
    }

}