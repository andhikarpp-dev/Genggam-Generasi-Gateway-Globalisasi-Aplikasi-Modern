package com.nutech.genggam_api.repository;

import com.nutech.genggam_api.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbc;

    public TransactionRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Transaction> rowMapper = (rs, n) -> {
        Transaction t = new Transaction();
        t.setId(rs.getLong("id"));
        t.setInvoiceNumber(rs.getString("invoice_number"));
        t.setUserId(rs.getLong("user_id"));
        t.setTransactionType(rs.getString("transaction_type"));
        t.setServiceCode(rs.getString("service_code"));
        t.setDescription(rs.getString("description"));
        t.setTotalAmount(rs.getBigDecimal("total_amount"));
        java.sql.Timestamp ts = rs.getTimestamp("created_on");
        if (ts != null) t.setCreatedOn(ts.toInstant().atOffset(ZoneOffset.UTC));
        return t;
    };

    /**
     * Generate invoice number "INVddMMyyyy-NNN" with daily sequence
     * berbasis count transaksi pada hari yang sama.
     */
    public String generateInvoiceNumber() {
        String datePart = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        String sql = """
                SELECT COUNT(*) + 1
                FROM transactions
                WHERE created_on::date = CURRENT_DATE
                """;
        Integer next = jdbc.queryForObject(sql, Integer.class);
        return String.format("INV%s-%03d", datePart, next == null ? 1 : next);
    }

    public Transaction insert(Transaction tx) {
        String sql = """
                INSERT INTO transactions
                    (invoice_number, user_id, transaction_type, service_code,
                     description, total_amount, created_on)
                VALUES (?, ?, ?, ?, ?, ?, NOW())
                """;
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tx.getInvoiceNumber());
            ps.setLong(2, tx.getUserId());
            ps.setString(3, tx.getTransactionType());
            if (tx.getServiceCode() == null) {
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(4, tx.getServiceCode());
            }
            ps.setString(5, tx.getDescription());
            ps.setBigDecimal(6, tx.getTotalAmount());
            return ps;
        }, kh);
        Number key = (Number) kh.getKeys().get("id");
        tx.setId(key.longValue());
        // ambil created_on persis
        String selSql = "SELECT created_on FROM transactions WHERE id = ?";
        java.sql.Timestamp ts = jdbc.queryForObject(selSql, java.sql.Timestamp.class, tx.getId());
        if (ts != null) tx.setCreatedOn(ts.toInstant().atOffset(ZoneOffset.UTC));
        return tx;
    }

    public List<Transaction> findHistory(Long userId, Integer limit, int offset) {
        if (limit == null) {
            String sql = """
                    SELECT id, invoice_number, user_id, transaction_type,
                           service_code, description, total_amount, created_on
                    FROM transactions
                    WHERE user_id = ?
                    ORDER BY created_on DESC
                    OFFSET ?
                    """;
            return jdbc.query(sql, rowMapper, userId, offset);
        }
        String sql = """
                SELECT id, invoice_number, user_id, transaction_type,
                       service_code, description, total_amount, created_on
                FROM transactions
                WHERE user_id = ?
                ORDER BY created_on DESC
                LIMIT ? OFFSET ?
                """;
        return jdbc.query(sql, rowMapper, userId, limit, offset);
    }
}
