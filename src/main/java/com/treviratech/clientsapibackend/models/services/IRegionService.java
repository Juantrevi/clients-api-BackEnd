package com.treviratech.clientsapibackend.models.services;

import com.treviratech.clientsapibackend.models.entity.Region;

import java.util.List;

public interface IRegionService {

    public List<Region> findAllRegions();
}
