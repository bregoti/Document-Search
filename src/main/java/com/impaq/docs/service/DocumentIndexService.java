package com.impaq.docs.service;

import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;

import com.impaq.docs.domain.solr.Document;

public interface DocumentIndexService {

	int DEFAULT_PAGE_SIZE = 5;
	
	public List<Document> search(String searchTerm);

	public List<Document> queryDocuments(String searchTerm) throws SolrServerException;
	
	Page<Document> findByName(String searchTerm, Pageable pageable);
	
	FacetPage<Document> autocompleteNameFragment(String fragment, Pageable pageable);
}
