package com.example.form;

/**
 * 記事投稿に使用するフォーム.
 * 
 * @author yoshiki.morimoto
 *
 */
public class ArticleForm {
	/** 名前 */
	private String name;
	/** コメント */
	private String comment;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "ArticleForm [name=" + name + ", comment=" + comment + "]";
	}
	
}
