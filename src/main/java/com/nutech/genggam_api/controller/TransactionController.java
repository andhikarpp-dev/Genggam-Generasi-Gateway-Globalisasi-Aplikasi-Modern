package com.nutech.genggam_api.controller;

import com.nutech.genggam_api.dto.request.TopUpRequest;
import com.nutech.genggam_api.dto.request.TransactionRequest;
import com.nutech.genggam_api.dto.response.ApiResponse;
import com.nutech.genggam_api.dto.response.BalanceResponse;
import com.nutech.genggam_api.dto.response.HistoryResponse;
import com.nutech.genggam_api.dto.response.TransactionResponse;
import com.nutech.genggam_api.security.AuthUtil;
import com.nutech.genggam_api.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/balance")
    public ApiResponse<BalanceResponse> balance() {
        return ApiResponse.success(
                "Get Balance Berhasil",
                transactionService.getBalance(AuthUtil.currentUser()));
    }

    @PostMapping("/topup")
    public ApiResponse<BalanceResponse> topUp(@Valid @RequestBody TopUpRequest req) {
        return ApiResponse.success(
                "Top Up Balance berhasil",
                transactionService.topUp(AuthUtil.currentUser(), req.getTopUpAmount()));
    }

    @PostMapping("/transaction")
    public ApiResponse<TransactionResponse> transaction(@Valid @RequestBody TransactionRequest req) {
        return ApiResponse.success(
                "Transaksi berhasil",
                transactionService.pay(AuthUtil.currentUser(), req.getServiceCode()));
    }

    @GetMapping("/transaction/history")
    public ApiResponse<HistoryResponse> history(
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit) {
        return ApiResponse.success(
                "Get History Berhasil",
                transactionService.history(AuthUtil.currentUser(), offset, limit));
    }
}
