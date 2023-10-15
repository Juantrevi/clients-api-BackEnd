package com.treviratech.clientsapibackend.models.services;

import com.treviratech.clientsapibackend.models.entity.Region;
import com.treviratech.clientsapibackend.models.repository.IRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegionServiceImplements implements IRegionService{

    @Autowired
    private IRegionRepository regionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Region> findAllRegions() {
        return regionRepository.findAll();
    }
}
