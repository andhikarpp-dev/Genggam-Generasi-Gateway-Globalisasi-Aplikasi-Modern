package com.nutech.genggam_api.service;

import com.nutech.genggam_api.dto.response.BalanceResponse;
import com.nutech.genggam_api.dto.response.HistoryItemResponse;
import com.nutech.genggam_api.dto.response.HistoryResponse;
import com.nutech.genggam_api.dto.response.TransactionResponse;
import com.nutech.genggam_api.exception.ApiException;
import com.nutech.genggam_api.model.Service;
import com.nutech.genggam_api.model.Transaction;
import com.nutech.genggam_api.model.User;
import com.nutech.genggam_api.repository.ServiceRepository;
import com.nutech.genggam_api.repository.TransactionRepository;
import com.nutech.genggam_api.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class TransactionService {

    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(UserRepository userRepository,
                              ServiceRepository serviceRepository,
                              TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.transactionRepository = transactionRepository;
    }

    public BalanceResponse getBalance(User current) {
        BigDecimal balance = userRepository.getBalance(current.getId());
        return new BalanceResponse(balance);
    }

    @Transactional
    public BalanceResponse topUp(User current, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw ApiException.badRequest(
                    "Parameter amount hanya boleh angka dan tidak boleh lebih kecil dari 0");
        }
        userRepository.addBalance(current.getId(), amount);

        Transaction tx = new Transaction();
        tx.setInvoiceNumber(transactionRepository.generateInvoiceNumber());
        tx.setUserId(current.getId());
        tx.setTransactionType("TOPUP");
        tx.setServiceCode(null);
        tx.setDescription("Top Up balance");
        tx.setTotalAmount(amount);
        transactionRepository.insert(tx);

        return new BalanceResponse(userRepository.getBalance(current.getId()));
    }

    @Transactional
    public TransactionResponse pay(User current, String serviceCode) {
        if (serviceCode == null || serviceCode.trim().isEmpty()) {
            throw ApiException.badRequest("Service ataus Layanan tidak ditemukan");
        }
        Service svc = serviceRepository.findByCode(serviceCode)
                .orElseThrow(() -> ApiException.badRequest("Service ataus Layanan tidak ditemukan"));

        BigDecimal balance = userRepository.getBalance(current.getId());
        if (balance == null || balance.compareTo(svc.getServiceTariff()) < 0) {
            throw ApiException.badRequest("Saldo tidak mencukupi untuk transaksi");
        }

        int updated = userRepository.subtractBalance(current.getId(), svc.getServiceTariff());
        if (updated == 0) {
            throw ApiException.badRequest("Saldo tidak mencukupi untuk transaksi");
        }

        Transaction tx = new Transaction();
        tx.setInvoiceNumber(transactionRepository.generateInvoiceNumber());
        tx.setUserId(current.getId());
        tx.setTransactionType("PAYMENT");
        tx.setServiceCode(svc.getServiceCode());
        tx.setDescription(svc.getServiceName());
        tx.setTotalAmount(svc.getServiceTariff());
        Transaction saved = transactionRepository.insert(tx);

        TransactionResponse res = new TransactionResponse();
        res.setInvoiceNumber(saved.getInvoiceNumber());
        res.setServiceCode(svc.getServiceCode());
        res.setServiceName(svc.getServiceName());
        res.setTransactionType("PAYMENT");
        res.setTotalAmount(svc.getServiceTariff());
        res.setCreatedOn(saved.getCreatedOn());
        return res;
    }

    public HistoryResponse history(User current, Integer offset, Integer limit) {
        int off = (offset == null || offset < 0) ? 0 : offset;
        Integer lim = (limit != null && limit <= 0) ? null : limit;

        List<Transaction> rows = transactionRepository.findHistory(current.getId(), lim, off);
        List<HistoryItemResponse> items = rows.stream().map(t -> {
            HistoryItemResponse h = new HistoryItemResponse();
            h.setInvoiceNumber(t.getInvoiceNumber());
            h.setTransactionType(t.getTransactionType());
            h.setDescription(t.getDescription());
            h.setTotalAmount(t.getTotalAmount());
            h.setCreatedOn(t.getCreatedOn());
            return h;
        }).collect(Collectors.toList());

        return new HistoryResponse(off, lim, items);
    }
}
