package com.impaq.docs.repository.solr;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Highlight;

import com.impaq.docs.domain.solr.Document;

public interface DocumentRepositoryCustom {

	/*@Highlight(query = "content:*?0*")*/
	public Page<Document> findAndApplyHighlightingWithParametrizedHighlightQuery(String name, Pageable page);
}
