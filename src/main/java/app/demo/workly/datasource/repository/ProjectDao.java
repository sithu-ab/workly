package app.demo.workly.datasource.repository;

import app.demo.workly.domain.model.project.Project;
import app.demo.workly.domain.repository.ProjectRepo;
import lombok.Locked;
import org.springframework.jdbc.core.RowMapper;
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
    public List<Project> getProjects() {
        final String sql = "SELECT * FROM projects WHERE deleted_at IS NULL";
        return this.jdbcTemplate.query(sql, ROW_MAPPER).stream().filter(Optional::isPresent).map(Optional::get).toList();
    }
}
