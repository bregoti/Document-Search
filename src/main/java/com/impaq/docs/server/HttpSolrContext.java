package com.impaq.docs.server;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.support.HttpSolrServerFactoryBean;

@Configuration
@EnableSolrRepositories(basePackages = { "com.impaq.docs.repository.solr" }, multicoreSupport = true)
public class HttpSolrContext {

	private @Resource Environment env;
	HttpSolrServer server = null;

	private static final String PROPERTY_NAME_SOLR_SERVER_URL = "solr.server.url";

	@Resource
	private Environment environment;

	@Bean
	public HttpSolrServerFactoryBean solrServerFactoryBean() {
		HttpSolrServerFactoryBean factory = new HttpSolrServerFactoryBean();

		factory.setUrl(environment
				.getRequiredProperty(PROPERTY_NAME_SOLR_SERVER_URL));

		return factory;
	}

	@Bean
	public SolrTemplate solrTemplate() throws Exception {
		return new SolrTemplate(solrServerFactoryBean().getObject());
	}

}
