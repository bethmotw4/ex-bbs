package com.example.form;

import javax.validation.constraints.NotBlank;

/**
 * 記事投稿に使用するフォーム.
 * 
 * @author yoshiki.morimoto
 *
 */
public class ArticleForm {
	/** 名前 */
	@NotBlank(message = "名前が未入力です")
	private String name;
	/** コンテント */
	@NotBlank(message = "投稿内容が未入力です")
	private String content;
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
		return "ArticleForm [name=" + name + ", content=" + content + "]";
	}
	
}
