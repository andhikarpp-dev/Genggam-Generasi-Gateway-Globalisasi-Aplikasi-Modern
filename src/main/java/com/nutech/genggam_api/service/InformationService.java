package com.nutech.genggam_api.service;

import com.nutech.genggam_api.model.Banner;
import com.nutech.genggam_api.model.Service;
import com.nutech.genggam_api.repository.BannerRepository;
import com.nutech.genggam_api.repository.ServiceRepository;

import java.util.List;

@org.springframework.stereotype.Service
public class InformationService {

    private final BannerRepository bannerRepository;
    private final ServiceRepository serviceRepository;

    public InformationService(BannerRepository bannerRepository, ServiceRepository serviceRepository) {
        this.bannerRepository = bannerRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<Banner> listBanners() {
        return bannerRepository.findAll();
    }

    public List<Service> listServices() {
        return serviceRepository.findAll();
    }
}
