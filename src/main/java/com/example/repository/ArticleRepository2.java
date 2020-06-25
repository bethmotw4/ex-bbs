package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Article;
import com.example.domain.Comment;

@Repository
@Transactional
public class ArticleRepository2 {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private final static RowMapper<Article> ARTICLE_ROW_MAPPER = new BeanPropertyRowMapper<>(Article.class);
	private static final RowMapper<Comment> COMMENT_ROW_MAPPER = (rs, i) -> {
		Comment comment = new Comment();
		comment.setId(rs.getInt("com_id"));
		comment.setName(rs.getString("com_name"));
		comment.setContent(rs.getString("com_content"));
		comment.setArticleId(rs.getInt("article_id"));
		return comment;
	};
	
	
	private static final ResultSetExtractor<List<Article>> ARTICLE_LIST = (rs) -> {
//		returnする記事一覧
		List<Article> articleList = new ArrayList<>();
		List<Comment> commentList = null;
		Integer key = null;
		Article article = null;
		int articleIdx = 0;
		int commentIdx = 0;
		while (rs.next()) {
//			親テーブルのデータがないまたは、JOINするカラムの値が変わったらarticleのオブジェクト作成
			if (article == null || !key.equals(rs.getInt("id"))) {
				key = rs.getInt("id");
				article = ARTICLE_ROW_MAPPER.mapRow(rs, articleIdx++);
				commentList = new ArrayList<>();
				article.setCommentList(commentList);
				commentIdx = 0;
				articleList.add(article);
			}
//			毎行commentテーブルの要素をarticleに追加する
			if (rs.getObject("article_id") != null) {
				commentList.add(COMMENT_ROW_MAPPER.mapRow(rs, commentIdx++));
			}
		}
		return articleList;
	};
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
	
	/**
	 * 投稿一覧を取得する.
	 * 
	 * @return 投稿一覧
	 */
	public List<Article> findAll() {
		String sql = "SELECT a.id AS id, a.name AS name, a.content AS content, c.id AS com_id, "
				+ "c.name AS com_name, c.content AS com_content, c.article_id AS article_id "
				+ "FROM articles AS a LEFT OUTER JOIN comments AS c ON a.id=c.article_id "
				+ "ORDER BY id DESC;";
		return jdbcTemplate.query(sql, ARTICLE_LIST);
	}
}
