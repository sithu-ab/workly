package app.demo.workly.datasource.repository;

import app.demo.workly.domain.model.project.Project;
import app.demo.workly.domain.repository.ProjectRepo;
import lombok.Locked;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProjectDao implements ProjectRepo {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final RowMapper<Optional<Project>> ROW_MAPPER = (rs, rowNum) -> extractRow(rs);

    private static final ResultSetExtractor<Optional<Project>> RESULT_EXTRACTOR= (rs) -> {
        if (!rs.next()) return Optional.empty();
        return extractRow(rs);
    };

    public ProjectDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Optional<Project> extractRow(ResultSet rs) throws SQLException {
        return Optional.of(Project.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .build());
    }

    @Override
    @Locked.Read
    public List<Project> findAll() {
        final String sql = "SELECT * FROM projects WHERE deleted_at IS NULL";
        return this.jdbcTemplate.query(sql, ROW_MAPPER).stream().filter(Optional::isPresent).map(Optional::get).toList();
    }

    @Override
    @Locked.Read
    public Optional<Project> findById(int id) {
        final String sql = "SELECT * FROM projects WHERE id = :id AND deleted_at IS NULL";
        return this.jdbcTemplate.query(sql, new MapSqlParameterSource("id", id), RESULT_EXTRACTOR);
    }
}
