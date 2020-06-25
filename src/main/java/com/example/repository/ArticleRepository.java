package com.example.repository;

import java.util.ArrayList;
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
import com.example.domain.Comment;

/**
 * articleテーブルを操作するリポジトリ.
 * 
 * @author yoshiki.morimoto
 *
 */
@Repository
@Transactional
public class ArticleRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Article> ARTICLE_ROW_MAPPER = (rs, i) -> {
		Article article = new Article();
		article.setId(rs.getInt("id"));
		article.setName(rs.getString("name"));
		article.setContent(rs.getString("content"));
		return article;
	};
	
	
	
	/**
	 * 投稿記事一覧を取得する.
	 * 
	 * @return 投稿記事一覧
	 */
	public List<Article> findAll() {
		String sql = "SELECT id, name, content FROM articles ORDER BY id DESC;";
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
	 * @param id 投稿ID
	 */
	public void deleteById(int id) {
		String sql = "DELETE FROM articles WHERE id=:id;";
		SqlParameterSource source = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, source);
	}
	
//	public List<Article> findAll2() {
//		String sql = "SELECT a.id AS id, a.name AS name, a.content AS content, c.id AS com_id, "
//				+ "c.name AS com_name, c.content AS com_content, c.article_id AS article_id "
//				+ "FROM articles AS a JOIN comments AS c ON a.id=c.article_id "
//				+ "WHERE a.id=c.article_id;";
//		return template.query(sql, ARTICLE_ROW_MAPPER2);
//	}
	

}
