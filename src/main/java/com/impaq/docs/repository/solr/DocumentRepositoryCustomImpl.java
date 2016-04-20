package com.impaq.docs.repository.solr;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.stereotype.Repository;

import com.impaq.docs.domain.solr.Document;

@Repository
public class DocumentRepositoryCustomImpl implements DocumentRepositoryCustom {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentRepositoryCustomImpl.class);

    @Resource
    private SolrTemplate solrTemplate;
    
    @Override
    public Page<Document> findAndApplyHighlightingWithParametrizedHighlightQuery(String searchTerm, Pageable page) {
    	
    	Query query = new SimpleQuery(new SimpleStringCriteria("content:"+searchTerm)).setPageRequest(page);
        return solrTemplate.queryForPage(query, Document.class);
    	
    }
    
}
