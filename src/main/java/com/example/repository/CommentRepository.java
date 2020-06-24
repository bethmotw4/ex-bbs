package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Comment;

/**
 * commentsテーブルを操作するリポジトリ.
 * 
 * @author yoshiki.morimoto
 *
 */
@Repository
@Transactional
public class CommentRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Comment> COMMENT_ROW_MAPPER = (rs, i) -> {
		Comment comment = new Comment();
		comment.setId(rs.getInt("id"));
		comment.setName(rs.getString("name"));
		comment.setContent(rs.getString("content"));
		comment.setArticleId(rs.getInt("article_id"));
		return comment;
	};
	
	/**
	 * 投稿IDでコメントを検索する.
	 * 
	 * @param articled 投稿ID
	 * @return コメント
	 */
	public List<Comment> findByArticledId(int articled) {
		String sql = "SELECT id, name, content, article_id FROM comments "
				+ "WHERE article_id=:articleId ORDER BY id;";
		SqlParameterSource source = new MapSqlParameterSource().addValue("articledId", articled);
		return template.query(sql, source, COMMENT_ROW_MAPPER);
	}
}
