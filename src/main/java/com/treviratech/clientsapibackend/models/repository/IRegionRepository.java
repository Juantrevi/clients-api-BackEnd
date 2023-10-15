package com.treviratech.clientsapibackend.models.repository;

import com.treviratech.clientsapibackend.models.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRegionRepository extends JpaRepository<Region, Long> {

}
