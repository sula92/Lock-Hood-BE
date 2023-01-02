package com.t6.lockhood.repository;

import com.t6.lockhood.model.RawMaterialRequest;
import com.t6.lockhood.model.pk.RawMaterialPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawMaterialRequestRepository extends JpaRepository<RawMaterialRequest, Integer> {
}
