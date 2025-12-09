package app.demo.workly.datasource.repository;

import app.demo.workly.domain.model.task.Task;
import app.demo.workly.domain.repository.TaskRepo;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TaskDao implements TaskRepo {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Constructor.
     */
    public TaskDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Optional<Task>> ROW_MAPPER = (rs, rowNum) -> extractRow(rs);

    private static final ResultSetExtractor<Optional<Task>> RESULT_EXTRACTOR= (rs) -> {
        if (!rs.next()) return Optional.empty();
        return extractRow(rs);
    };

    private static Optional<Task> extractRow(ResultSet rs) throws SQLException {
        return Optional.of(Task.builder()
                .id(rs.getInt("id"))
                .projectId(rs.getInt("project_id"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .status(rs.getString("status"))
                .build());
    }

    @Override
    public List<Task> findAll() {
        final String sql = "SELECT id, project_id, title, description, status FROM tasks WHERE deleted_at IS NULL";
        return this.jdbcTemplate.query(sql, ROW_MAPPER)
                .stream()
                .filter(Optional::isPresent).
                map(Optional::get)
                .toList();
    }

    @Override
    public List<Task> findAllByProject(int projectId) {
        final String sql = """
                SELECT id, project_id, title, description, status FROM tasks 
                WHERE project_id = :projectId AND deleted_at IS NULL
            """;
        return this.jdbcTemplate.query(sql, new MapSqlParameterSource("projectId", projectId), ROW_MAPPER)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public Optional<Task> findById(int id) {
        final String sql = """
                SELECT id, project_id, title, description, status FROM tasks 
                WHERE id = :id AND deleted_at IS NULL
            """;
        return this.jdbcTemplate.query(sql, new MapSqlParameterSource("id", id), RESULT_EXTRACTOR);
    }

    @Override
    public Task create(int projectId, String title, String description, String status) {
        final String sql = """
                INSERT INTO tasks (project_id, title, description, status)
                VALUES (:projectId, :title, :description, :status)
            """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("projectId", projectId)
                .addValue("title", title)
                .addValue("description", description)
                .addValue("status", status)
                .addValue("now", new Timestamp(System.currentTimeMillis()));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
        Number key = keyHolder.getKey();
        int id = key != null ? key.intValue() : -1;

        return Task.builder()
                .id(id)
                .projectId(projectId)
                .title(title)
                .description(description)
                .status(status)
                .build();
    }

    @Override
    public Optional<Task> update(int id, String title, String description, String status) {
        final String sql = """
                UPDATE tasks SET
                    title = COALESCE(:title, title),
                    description = COALESCE(:description, description),
                    status = COALESCE(:status, status),
                    updated_at = :now
                WHERE id = :id AND deleted_at IS NULL;
            """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("title", title)
                .addValue("description", description)
                .addValue("status", status != null ? status.toUpperCase() : null)
                .addValue("now", new Timestamp(System.currentTimeMillis()));

        int updated = this.jdbcTemplate.update(sql, params);
        if (updated == 0) return Optional.empty();

        return findById(id);
    }

    @Override
    public boolean softDelete(int id) {
        final String sql = "UPDATE tasks SET deleted_at = :now WHERE id = :id AND deleted_at IS NULL";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("now", new Timestamp(System.currentTimeMillis()));

        return this.jdbcTemplate.update(sql, params) > 0;
    }
}
