package com.nutech.genggam_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class TransactionRequest {

    @JsonProperty("service_code")
    @NotBlank(message = "Service ataus Layanan tidak ditemukan")
    private String serviceCode;

    public String getServiceCode() { return serviceCode; }
    public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }
}
