package com.nutech.genggam_api.controller;

import com.nutech.genggam_api.dto.response.ApiResponse;
import com.nutech.genggam_api.model.Banner;
import com.nutech.genggam_api.model.Service;
import com.nutech.genggam_api.service.InformationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InformationController {

    private final InformationService informationService;

    public InformationController(InformationService informationService) {
        this.informationService = informationService;
    }

    @GetMapping("/banner")
    public ApiResponse<List<Banner>> banner() {
        return ApiResponse.success("Sukses", informationService.listBanners());
    }

    @GetMapping("/services")
    public ApiResponse<List<Service>> services() {
        return ApiResponse.success("Sukses", informationService.listServices());
    }
}
