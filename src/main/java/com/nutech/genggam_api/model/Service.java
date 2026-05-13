package com.nutech.genggam_api.model;

import java.math.BigDecimal;

public class Service {
    private String serviceCode;
    private String serviceName;
    private String serviceIcon;
    private BigDecimal serviceTariff;

    public String getServiceCode() { return serviceCode; }
    public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getServiceIcon() { return serviceIcon; }
    public void setServiceIcon(String serviceIcon) { this.serviceIcon = serviceIcon; }
    public BigDecimal getServiceTariff() { return serviceTariff; }
    public void setServiceTariff(BigDecimal serviceTariff) { this.serviceTariff = serviceTariff; }
}
