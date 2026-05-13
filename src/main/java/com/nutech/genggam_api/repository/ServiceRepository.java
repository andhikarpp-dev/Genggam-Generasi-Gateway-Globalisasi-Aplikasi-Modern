package com.nutech.genggam_api.repository;

import com.nutech.genggam_api.model.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ServiceRepository {

    private final JdbcTemplate jdbc;

    public ServiceRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Service> rowMapper = (rs, n) -> {
        Service s = new Service();
        s.setServiceCode(rs.getString("service_code"));
        s.setServiceName(rs.getString("service_name"));
        s.setServiceIcon(rs.getString("service_icon"));
        s.setServiceTariff(rs.getBigDecimal("service_tariff"));
        return s;
    };

    public List<Service> findAll() {
        String sql = """
                SELECT service_code, service_name, service_icon, service_tariff
                FROM services
                ORDER BY id ASC
                """;
        return jdbc.query(sql, rowMapper);
    }

    public Optional<Service> findByCode(String serviceCode) {
        String sql = """
                SELECT service_code, service_name, service_icon, service_tariff
                FROM services
                WHERE service_code = ?
                """;
        return jdbc.query(sql, rowMapper, serviceCode).stream().findFirst();
    }
}
