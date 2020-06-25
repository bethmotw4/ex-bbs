package com.example.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Article;
import com.example.domain.Comment;
import com.example.form.ArticleForm;
import com.example.form.CommentForm;
import com.example.repository.ArticleRepository;
import com.example.repository.ArticleRepository2;
import com.example.repository.CommentRepository;

/**
 * 記事投稿とコメント投稿に関する制御を行うコントローラ.
 * 
 * @author yoshiki.morimoto
 *
 */
@Controller
@RequestMapping("/article")
public class ArticleController {
	@Autowired
//	private ArticleRepository articleRepository;
	
	private ArticleRepository2 articleRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	/**
	 * 記事投稿に使用するフォームのインスタンス化.
	 * 
	 * @return 記事投稿フォーム
	 */
	@ModelAttribute
	public ArticleForm setUpArticleForm() {
		return new ArticleForm();
	}
	
	/**
	 * コメント投稿時に使用するフォームのインスタンス化.
	 * 
	 * @return コメント投稿フォーム
	 */
	@ModelAttribute
	public CommentForm setUpCommentForm() {
		return new CommentForm();
	}
	
	/**
	 * 掲示板画面を表示.
	 * 
	 * @param model リクエストスコープ
	 * @return 掲示板画面
	 */
	@RequestMapping("")
	public String index(Model model) {
		List<Article> articleList = articleRepository.findAll();
//		for (Article article : articleList) {
//			List<Comment> commentList = commentRepository.findByArticledId(article.getId());
//			article.setCommentList(commentList);
//		}
		model.addAttribute("articleList", articleList);
		return "article";
	}
	
	/**
	 * 記事を投稿する.
	 * 
	 * @param form 記事投稿フォーム
	 * @return 掲示板画面
	 */
	@RequestMapping("/insert")
	public String insertArticle(@Validated ArticleForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return index(model);
		}
		Article article = new Article();
		BeanUtils.copyProperties(form, article);
		System.out.println(article);
		articleRepository.insert(article);
		
		return "redirect:/article";
	}
	
	/**
	 * コメントを投稿する.
	 * 
	 * @param form コメント投稿フォーム
	 * @return 掲示板画面
	 */
	@RequestMapping("/comment")
	public String insertComment(@Validated CommentForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return index(model);
		}
		Comment comment = new Comment();
//		articleIDがintとstring
		BeanUtils.copyProperties(form, comment);
		comment.setArticleId(Integer.parseInt(form.getArticleId()));
		commentRepository.insert(comment);
		return "redirect:/article";
	}
	
	/**
	 * 投稿記事を削除する.
	 * 
	 * @param id 投稿ID
	 * @return 掲示板画面
	 */
	@RequestMapping("/delete")
	public String deleteArticle(Integer articleId) {
//		commentRepository.deleteByArticleId(articleId);
		articleRepository.deleteById(articleId);
		return "redirect:/article";
	}
}
