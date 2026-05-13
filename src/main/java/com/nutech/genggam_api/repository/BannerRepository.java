package com.nutech.genggam_api.repository;

import com.nutech.genggam_api.model.Banner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BannerRepository {

    private final JdbcTemplate jdbc;

    public BannerRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Banner> rowMapper = (rs, n) -> {
        Banner b = new Banner();
        b.setBannerName(rs.getString("banner_name"));
        b.setBannerImage(rs.getString("banner_image"));
        b.setDescription(rs.getString("description"));
        return b;
    };

    public List<Banner> findAll() {
        String sql = """
                SELECT banner_name, banner_image, description
                FROM banners
                ORDER BY id ASC
                """;
        return jdbc.query(sql, rowMapper);
    }
}
