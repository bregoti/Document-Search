package com.impaq.docs.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.stereotype.Service;

import com.impaq.docs.domain.solr.Document;
import com.impaq.docs.repository.solr.DocumentRepository;

@Service
public class DocumentIndexServiceImpl implements DocumentIndexService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DocumentIndexServiceImpl.class);

	private static final Pattern IGNORED_CHARS_PATTERN = Pattern.compile("\\p{Punct}");

	protected static final String QUERY_METHOD_METHOD_NAME = "methodName";
	protected static final String QUERY_METHOD_NAMED_QUERY = "namedQuery";
	protected static final String QUERY_METHOD_QUERY_ANNOTATION = "queryAnnotation";

	@Resource
	private DocumentRepository repository;

	@Resource
	private SolrTemplate solrTemplate;
	
	@Value("${solr.repository.query.method.type}")
	private String queryMethodType;

	
	@Override
	public List<Document> search(String searchTerm) {
		LOGGER.debug("Searching products with given search term: {}",
				searchTerm);
		return findProducts(searchTerm);
	}

	@Override
	public Page<Document> findByName(String searchTerm, Pageable pageable) {
		
		if (StringUtils.isBlank(searchTerm)) {
			return repository.findAll(pageable);
		}
		
		return repository.findByNameIn(splitSearchTermAndRemoveIgnoredCharacters(searchTerm), pageable);
	}
	
	private Collection<String> splitSearchTermAndRemoveIgnoredCharacters(String searchTerm) {
		String[] searchTerms = StringUtils.split(searchTerm, " ");
		List<String> result = new ArrayList<String>(searchTerms.length);
		for (String term : searchTerms) {
			if (StringUtils.isNotEmpty(term)) {
				result.add(IGNORED_CHARS_PATTERN.matcher(term).replaceAll(" "));
			}
		}
		return result;
	}
	
	@Override
	public FacetPage<Document> autocompleteNameFragment(String fragment, Pageable pageable) {
		LOGGER.debug("autocomplete: Searching products with given search term: {}", fragment);
		if (StringUtils.isBlank(fragment)) {
			return new SolrResultPage<Document>(Collections.<Document> emptyList());
		}
		return repository.findByNameStartingWith(splitSearchTermAndRemoveIgnoredCharacters(fragment), pageable);
	}
	
	@Override
	public List<Document> queryDocuments(String searchTerm) throws SolrServerException{
		LOGGER.debug("Searching documents with given search term: {}",
				searchTerm);
		return findDocuments(searchTerm);
	}
	private List<Document> findProducts(String searchTerm) {

		if (queryMethodType != null) {

			LOGGER.debug("Finding products by using the named query");
			return repository.findByQueryAnnotation(searchTerm);
			
			
			
			//return repository.findByContent(searchTerm);
			
			
			/*if (queryMethodType.equals(QUERY_METHOD_METHOD_NAME)) {
				LOGGER.debug("Finding products by using the query method name");
				return repository.findByTitleContainsOrDescriptionContains(
						searchTerm, searchTerm, sortByIdDesc());
			} else if (queryMethodType.equals(QUERY_METHOD_NAMED_QUERY)) {
				LOGGER.debug("Finding products by using the named query");
				return repository.findByNamedQuery(searchTerm);
			} else if (queryMethodType.equals(QUERY_METHOD_QUERY_ANNOTATION)) {
				LOGGER.debug("Finding products by using the query annotation");
				return repository.findByQueryAnnotation(searchTerm,
						sortByIdDesc());
			}*/

		}

		return new ArrayList<Document>();
	}
	
	private List<Document> findDocuments(String searchTerm) throws SolrServerException{
	

		if (queryMethodType != null) {

			LOGGER.debug("Finding documents by using the named query");
			
			/*SimpleHighlightQuery query = new SimpleHighlightQuery();
			// ie solr fl, fields to include in results
			//query.addProjectionOnFields("id url content author title subject");
			Criteria conditions = new Criteria("title").contains(searchTerm)
					.or("description").contains(searchTerm)
					.or("content").contains(searchTerm)
					.or("id").contains(searchTerm)
					.or("author").contains(searchTerm);
			Criteria conditions = new Criteria("content").contains(searchTerm);
			query.addCriteria(conditions);
			HighlightOptions hlOptions = new HighlightOptions();
			// ie solr hl.fl, fields to apply highlighting 
			hlOptions.addField("content");
			hlOptions.addHighlightParameter("content",searchTerm);
			hlOptions.setSimplePrefix("<b>");
			hlOptions.setSimplePostfix("</b>");
			query.setHighlightOptions(hlOptions);
			LOGGER.debug("Query String:"+query.hasHighlightOptions()+ "Query :"+ query.getHighlightOptions().getQuery());
			HighlightPage<Document> results = solrTemplate.queryForHighlightPage(query, Document.class);
			*/
			
			Pageable page = new PageRequest(0,10);
			SolrQuery query = new SolrQuery();
			
			query.setQuery(searchTerm);
			query.setFields("content");
			query.setHighlight(true);
			query.setRows(10);
			query.addHighlightField("content");
			query.setHighlightSnippets(1000);
			query.setHighlightSimplePost("</span>");
			query.setHighlightSimplePre("<span class='label label-important'>");
			QueryResponse resp = solrTemplate.getSolrServer().query(query);
			
			List<Document> documents = solrTemplate.convertQueryResponseToBeans(resp, Document.class);
			
			//return new SolrResultsPage<Document>(documents, page, resp.getResults().getNumFound());
			
			return documents;
			//			return results.getContent();
			
			/*String[] words = searchTerm.split(" ");
			
			Criteria conditions = createSearchConditions(words);
			SimpleQuery query = new SimpleQuery(conditions);*/
			
			/*Query query = new SolrQuery();
			query.setQuery(searchTerm);
			query.setFields("content", "author", "url", "title", "id");
			query.setHighlight(true);
			query.setRows(10);
			query.addHighlightField("content");
			query.setHighlightSnippets(1000);
			query.setHighlightSimplePost("</span>");
			query.setHighlightSimplePre("<span class='label label-important'>");*/
			
			//Page results = solrTemplate.queryForPage(query, Document.class);
			
			//return results.getContent();
			
			//return repository.findByQueryAnnotation(searchTerm);
			
			//return repository.findByContent(searchTerm);
			
			
			/*if (queryMethodType.equals(QUERY_METHOD_METHOD_NAME)) {
				LOGGER.debug("Finding products by using the query method name");
				return repository.findByTitleContainsOrDescriptionContains(
						searchTerm, searchTerm, sortByIdDesc());
			} else if (queryMethodType.equals(QUERY_METHOD_NAMED_QUERY)) {
				LOGGER.debug("Finding products by using the named query");
				return repository.findByNamedQuery(searchTerm);
			} else if (queryMethodType.equals(QUERY_METHOD_QUERY_ANNOTATION)) {
				LOGGER.debug("Finding products by using the query annotation");
				return repository.findByQueryAnnotation(searchTerm,
						sortByIdDesc());
			}*/

		}

		return new ArrayList<Document>();
	}
		

	private Criteria createSearchConditions(String[] words) {
		
		Criteria conditions = null;
		for (String word: words) {
            if (conditions == null) {
                conditions = new Criteria("title").contains(word)
                        .or(new Criteria("description").contains(word) 
                        .or(new Criteria("content").contains(word)));
            }
            else {
                conditions = conditions.or(new Criteria("title").contains(word))
                        .or(new Criteria("description").contains(word)
                        .or(new Criteria("content").contains(word)));
            }
        }
 
        return conditions;
	}
	private Sort sortByIdDesc() {
		return new Sort(Sort.Direction.DESC, Document.FIELD_ID);
	}
}
