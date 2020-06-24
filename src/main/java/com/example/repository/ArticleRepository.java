package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Article;

@Repository
@Transactional
public class ArticleRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	private static CommentRepository repository;
	
	private static final RowMapper<Article> ARTICLE_ROW_MAPPER = (rs, i) -> {
		Article article = new Article();
		article.setId(rs.getInt("id"));
		article.setName(rs.getString("name"));
		article.setContent(rs.getString("content"));
		article.setCommentList(repository.findByArticledId(article.getId()));
		return article;
	};
	
	/**
	 * 投稿記事一覧を取得する.
	 * 
	 * @return 投稿記事一覧
	 */
	public List<Article> findAll() {
		String sql = "SELECT id, name, content FROM articles;";
		return template.query(sql, ARTICLE_ROW_MAPPER);
	}
	
	/**
	 * 投稿記事を登録する.
	 * 
	 * @param article 投稿記事
	 */
	public void insert(Article article) {
		String sql = "INSERT INTO articles(name, content) VALUES(:name, :content);";
		SqlParameterSource source = new BeanPropertySqlParameterSource(article);
		template.update(sql, source);
	}
	
	/**
	 * 投稿記事を削除する.
	 * 
	 * @param id ID
	 */
	public void deleteById(int id) {
		String sql = "DELETE FROM articles WHERE id=:id;";
		SqlParameterSource source = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, source);
	}
}
