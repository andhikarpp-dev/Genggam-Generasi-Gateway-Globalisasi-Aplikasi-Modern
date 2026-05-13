package com.nutech.genggam_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TopUpRequest {

    @JsonProperty("top_up_amount")
    @NotNull(message = "Parameter amount hanya boleh angka dan tidak boleh lebih kecil dari 0")
    @Min(value = 1, message = "Parameter amount hanya boleh angka dan tidak boleh lebih kecil dari 0")
    private BigDecimal topUpAmount;

    public BigDecimal getTopUpAmount() { return topUpAmount; }
    public void setTopUpAmount(BigDecimal topUpAmount) { this.topUpAmount = topUpAmount; }
}
