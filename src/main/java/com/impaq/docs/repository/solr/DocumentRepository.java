package com.impaq.docs.repository.solr;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import com.impaq.docs.domain.solr.Document;

public interface DocumentRepository extends SolrCrudRepository<Document, String> {

/*	public List<Document> findByTitleContainsOrDescriptionContains(
			String title, String description, Sort sort);*/

	/*@Query(name = "Document.findByNamedQuery")
	public List<Document> findByNamedQuery(String searchTerm, Sort sort);*/

	//@Query("resourcename:*?0* OR title:*?0* OR id:*?0* OR content:*?0*")
	@Highlight(fields = { "name", "description" }, fragsize = 10, snipplets = 20, prefix = "<b>", postfix = "</b>",
			 formatter = "simple")
	@Query("content:*?0*")
	public List<Document> findByQueryAnnotation(String searchTerm);
	
	@Highlight(fields = { "title", "id", "subject", "content" }, fragsize = 10, snipplets = 10, prefix = "<b>", postfix = "</b>")
	@Query("content:*?0* OR id:*?0*")
	HighlightPage<Document> findByNameIn(Collection<String> searchTerms, Pageable page);
	
	@Facet(fields = {"title", "id", "subject"})
	@Query("title:*?0*")
	FacetPage<Document> findByNameStartingWith(Collection<String> searchTerms, Pageable page);
	
	//@Highlight
	//Page<Document> findAndApplyHighlighting(Pageable page);
	
	/*@Highlight(fields = { "name", "description" }, fragsize = 10, snipplets = 20, prefix = "<b>", postfix = "</b>",
			query = "name:with", formatter = "simple")
	Page<Document> findAndApplyHighlightingAllParameters(Pageable page);*/
	
	/*@Highlight(query = "content:*?0*")
	public Page<Document> findAndApplyHighlightingWithParametrizedHighlightQuery(String name, Pageable page);
*/
	//List<Document> findByContent(String searchTerm);
	
	//public List<Document> findAndApplyHighlightingWithParametrizedHighlightQuery(String name);
}
