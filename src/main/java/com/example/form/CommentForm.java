package com.example.form;

/**
 * コメント投稿に使用するフォーム.
 * 
 * @author yoshiki.morimoto
 *
 */
public class CommentForm {
	/** 投稿ID */
	private String srticleId;
	/** 名前 */
	private String name;
	/** コメント */
	private String comment;
	public String getSrticleId() {
		return srticleId;
	}
	public void setSrticleId(String srticleId) {
		this.srticleId = srticleId;
	}
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
		return "CommentForm [srticleId=" + srticleId + ", name=" + name + ", comment=" + comment + "]";
	}
	
}
