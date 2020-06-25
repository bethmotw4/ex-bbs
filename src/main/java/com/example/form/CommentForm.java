package com.example.form;

import javax.validation.constraints.NotBlank;

/**
 * コメント投稿に使用するフォーム.
 * 
 * @author yoshiki.morimoto
 *
 */
public class CommentForm {
	/** 投稿ID */
	private String articleId;
	/** 名前 */
	@NotBlank(message = "名前が未入力です")
	private String name;
	/** コメント */
	@NotBlank(message = "コメントが未入力です")
	private String content;
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "CommentForm [articleId=" + articleId + ", name=" + name + ", content=" + content + "]";
	}
	
}
