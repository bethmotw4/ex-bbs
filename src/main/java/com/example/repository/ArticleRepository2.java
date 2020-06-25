package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
//		articleに足すコメントリスト
		List<Comment> commentList = null;
//		articles.idの数値
		Integer key = null;
//		articleオブジェクト
		Article article = null;
//		articleが何件あるか?
		int articleIdx = 0;
//		commentが何件あるか?
		int commentIdx = 0;
		while (rs.next()) {
//			articleのデータがないまたは、articles.idが変わったらarticleのオブジェクト作成
			if (article == null || !key.equals(rs.getInt("id"))) {
//				現在のarticles.idを保持
				key = rs.getInt("id");
				article = ARTICLE_ROW_MAPPER.mapRow(rs, articleIdx++);
//				System.out.println(article);
//				System.out.println("articleIdx" + articleIdx);
				commentList = new ArrayList<>();
				article.setCommentList(commentList);
				commentIdx = 0;
				articleList.add(article);
			}
//			article_idがnullでなければコメントリストに追加
			if (rs.getObject("article_id") != null) {
				commentList.add(COMMENT_ROW_MAPPER.mapRow(rs, commentIdx++));
//				System.out.println("commentIdx" + commentIdx);
//				System.out.println(article);
			}
//			System.out.println("=============================================");
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
				+ "ORDER BY id DESC, com_id DESC;";
		return template.query(sql, ARTICLE_LIST);
	}
	
	
//	1回のSQLで複数テーブルの消し方がわからん
//	commentsのtableのforeign keyにon delete cascadeではダメ？
	public void deleteById2(int id) {
		String sql = "DELETE FROM articles AS a USING comments AS c WHERE a.id=c.article_id AND a.id=4;";
		SqlParameterSource source = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, source);
	}
}
