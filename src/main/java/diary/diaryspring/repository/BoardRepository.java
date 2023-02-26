package diary.diaryspring.repository;

import diary.diaryspring.domain.Board;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BoardRepository {
    private final JdbcTemplate jt;

    public BoardRepository(DataSource ds) {
        this.jt = new JdbcTemplate(ds);
    }

    public void save(Board board) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jt);
        jdbcInsert.withTableName("board").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("title", board.getTitle());
        params.put("writer", board.getWriter());
        params.put("content", board.getContent());
        params.put("date", board.getDate());

        Number executeKey = jdbcInsert.executeAndReturnKey(params);
        System.out.println(executeKey);
    }

    public void update(Board oldBoard, Board newBoard) {
        jt.query("UPDATE board SET title=?, content=? WHERE id = ?", boardRowMapper(),
                newBoard.getTitle(), newBoard.getContent(), oldBoard.getId());
    }

    public void delete(Board board) {
        jt.query("DELETE FROM board WHERE id = ?", boardRowMapper(), board.getId());
    }

    public Optional<Board> findByWriter(String writer) {
        List<Board> result = jt
                .query("SELECT * FROM board WHERE writer = ?", boardRowMapper(), writer);
        return result.stream().findAny();
    }

    public Optional<Board> findByTitle(String title) {
        List<Board> result = jt
                .query("SELECT * FROM board WHERE title = ?", boardRowMapper(), title);
        return result.stream().findAny();
    }

    public List<Board> findAll() {
        return jt.query("SELECT * FROM board", boardRowMapper());
    }

    private RowMapper<Board> boardRowMapper() {
        return (rs, rowNum) -> {
            Board board = new Board();

            board.setTitle(rs.getString("title"));
            board.setWriter(rs.getString("writer"));
            board.setContent(rs.getString("content"));
            board.setDate();

            return board;
        };
    }
}

