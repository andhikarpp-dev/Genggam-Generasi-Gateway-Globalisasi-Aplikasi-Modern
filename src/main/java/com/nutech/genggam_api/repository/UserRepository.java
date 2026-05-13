package com.nutech.genggam_api.repository;

import com.nutech.genggam_api.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<User> rowMapper = (rs, n) -> {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setEmail(rs.getString("email"));
        u.setFirstName(rs.getString("first_name"));
        u.setLastName(rs.getString("last_name"));
        u.setPassword(rs.getString("password"));
        u.setProfileImage(rs.getString("profile_image"));
        u.setBalance(rs.getBigDecimal("balance"));
        return u;
    };

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer c = jdbc.queryForObject(sql, Integer.class, email);
        return c != null && c > 0;
    }

    public Optional<User> findByEmail(String email) {
        String sql = """
                SELECT id, email, first_name, last_name, password,
                       profile_image, balance
                FROM users
                WHERE email = ?
                """;
        return jdbc.query(sql, rowMapper, email).stream().findFirst();
    }

    public Optional<User> findById(Long id) {
        String sql = """
                SELECT id, email, first_name, last_name, password,
                       profile_image, balance
                FROM users
                WHERE id = ?
                """;
        return jdbc.query(sql, rowMapper, id).stream().findFirst();
    }

    public Long insert(User u) {
        String sql = """
                INSERT INTO users (email, first_name, last_name, password, balance)
                VALUES (?, ?, ?, ?, 0)
                """;
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getFirstName());
            ps.setString(3, u.getLastName());
            ps.setString(4, u.getPassword());
            return ps;
        }, kh);
        Number key = (Number) kh.getKeys().get("id");
        return key.longValue();
    }

    public int updateName(Long id, String firstName, String lastName) {
        String sql = """
                UPDATE users
                SET first_name = ?, last_name = ?, updated_at = NOW()
                WHERE id = ?
                """;
        return jdbc.update(sql, firstName, lastName, id);
    }

    public int updateProfileImage(Long id, String imageUrl) {
        String sql = """
                UPDATE users
                SET profile_image = ?, updated_at = NOW()
                WHERE id = ?
                """;
        return jdbc.update(sql, imageUrl, id);
    }

    public int addBalance(Long id, BigDecimal amount) {
        String sql = """
                UPDATE users
                SET balance = balance + ?, updated_at = NOW()
                WHERE id = ?
                """;
        return jdbc.update(sql, amount, id);
    }

    public int subtractBalance(Long id, BigDecimal amount) {
        String sql = """
                UPDATE users
                SET balance = balance - ?, updated_at = NOW()
                WHERE id = ? AND balance >= ?
                """;
        return jdbc.update(sql, amount, id, amount);
    }

    public BigDecimal getBalance(Long id) {
        String sql = "SELECT balance FROM users WHERE id = ?";
        return jdbc.queryForObject(sql, BigDecimal.class, id);
    }
}
