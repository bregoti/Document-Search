package com.impaq.docs.domain.solr;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(solrCoreName = "impaqdocs")
public class Document {
	
	public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ID = "id";
    public static final String FIELD_TITLE = "title";

	@Id
	@Field("id")
	private String id;
	@Field("title")
	private String title;
	@Field("subject")
	private String subject;
	@Field("description")
	private String description;
	@Field("comments")
	private String comments;
	@Field("author")
	private String author;
	@Field("keywords")
	private String keywords;
	@Field("category")
	private String category;
	@Field("resourcename")
	private String resourcename;
	@Field("url")
	private String url;
	/*@Field("content_type")
	private String contentType;*/
	@Field("last_modified")
	private String lastModified;
	/*@Field("links")
	private String links;*/
	@Field("content")
	private String content;
	
	private String fileName;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getResourcename() {
		return resourcename;
	}
	public void setResourcename(String resourcename) {
		this.resourcename = resourcename;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	/*public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}*/
	public String getLastModified() {
		return lastModified;
	}
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	/*public String getLinks() {
		return links;
	}
	public void setLinks(String links) {
		this.links = links;
	}*/
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getFileName() {
		
		if(null != this.getId()) {
			Path p = Paths.get(this.getId());
			return p.getFileName().toString();
		}else if ( null != this.getResourcename()) {
			Path p = Paths.get(this.getId());
			return p.getFileName().toString();
		}
		
		return "";
	}
}
