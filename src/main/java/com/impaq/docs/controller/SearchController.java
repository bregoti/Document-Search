package com.impaq.docs.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.impaq.docs.domain.solr.Document;
import com.impaq.docs.exception.DocumentNotFoundException;
import com.impaq.docs.service.DocumentIndexService;
import com.impaq.docs.util.MimeTypeConstants;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * @author bregoti
 *
 */
@Controller
@PropertySource("classpath:spring/application.properties")
public class SearchController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);
	
	@Resource
	private DocumentIndexService service;
	
	@Resource
	private Environment env;
	
	private static final String SFTP_HOST = "sftp.host";
	private static final String SFTP_PORT = "sftp.port";
	private static final String SFTP_USER = "sftp.user";
	private static final String SFTP_PWD = "sftp.pwd";
	private static final String SFTP_WORKING_DIR = "sftp.working.dir";
	private static final String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
	private static final String NO = "no";
	
	

	/**
	 * Search the documents from the file system.
	 * @param model
	 * @param searchTerm
	 * @param pageable
	 * @param request
	 * @return
	 * @throws SolrServerException
	 */
	@RequestMapping(value = "/search", method=RequestMethod.GET)
	public String search(Model model, @RequestParam(required = false, value= "q") String searchTerm, @PageableDefault(page = 0, 
	size = DocumentIndexService.DEFAULT_PAGE_SIZE) Pageable pageable, HttpServletRequest request) throws SolrServerException{
		
		LOGGER.debug("searching the products with search term :"+ searchTerm);
		model.addAttribute("page",service.findByName(searchTerm, pageable));
		model.addAttribute("pageable", pageable);
		model.addAttribute("query", searchTerm);
		
		return "list";
		
	}
	
	/**
	 * Retrieves the document from the remote server thru 'SFTP' protocol 
	 * and Download the document or show the document in the browser. 
	 * @param id
	 * @param request
	 * @param response
	 * @throws DocumentNotFoundException 
	 */
	@RequestMapping(value = "/downloadFile", method=RequestMethod.GET)
	public @ResponseBody void downloadDoc(@RequestParam(required = false, value= "filePath") String filePath, HttpServletRequest request, HttpServletResponse response) throws DocumentNotFoundException {
		
	
		Session session = null;
		Channel channel = null;
		
		ChannelSftp channelSftp = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream outStream = null;
		
		try{
			JSch jsch = new JSch();
			session = jsch.getSession(env.getRequiredProperty(SFTP_USER), env.getRequiredProperty(SFTP_HOST), Integer.parseInt(env.getRequiredProperty(SFTP_PORT)));
			session.setPassword(env.getRequiredProperty(SFTP_PWD));
			java.util.Properties config = new java.util.Properties();
		
			String SFTP_WORKING_DIR = FilenameUtils.getPath(filePath);
			
			config.put(STRICT_HOST_KEY_CHECKING, NO);
			session.setConfig(config);
			session.connect();
		
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp)channel;
			//channelSftp.cd(env.getRequiredProperty(SFTP_WORKING_DIR));
			channelSftp.cd("/"+SFTP_WORKING_DIR);
			
			String fileName = FilenameUtils.getName(filePath);
			String fileNameExtension = FilenameUtils.getExtension(filePath);
			String mimeType = MimeTypeConstants.getMimeType(fileNameExtension);
			
			
			/*String fileName = filePath+"."+ FilenameUtils.getExtension(FilenameUtils.getName(request.getRequestURI()));
			String fileNameExtension = FilenameUtils.getExtension(fileName);
			String mimeType = MimeTypeConstants.getMimeType(fileNameExtension);*/
			LOGGER.debug("fileName:"+ fileName+" fileNameExtension:"+fileNameExtension+" mimeType:"+mimeType);
			bis = new BufferedInputStream(channelSftp.get(fileName));
			response.setContentLength((int)channelSftp.stat(fileName).getSize());
			response.setContentType(mimeType);
			
			// response Header
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");

			// Write Response
			outStream = response.getOutputStream();
			
			bos = new BufferedOutputStream(outStream);
			int readCount;
			byte[] buffer = new byte[1024];
			while( (readCount = bis.read(buffer)) > 0) {
				bos.write(buffer, 0, readCount);
			}
			
			bos.flush();
			

		} catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if(null != bis) {
					bis.close();
				}
				if(null != bos) {
					bos.close();
				}
				if(null != outStream) {
					outStream.close();
				}
				if ( null != channel) {
					channel.disconnect();
				}
				if ( null != session) {
					session.disconnect();
				}
			}catch( IOException ioe) {
				throw new DocumentNotFoundException("Sorry !!! Document could not be found in the file system ");
				
			}
		}
		
		
	}
	
	/**
	 * Auto Complete suggestion feature
	 * @param model
	 * @param query
	 * @param pageable
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/autocomplete", produces = "application/json")
	public Set<String> autoComplete(Model model, @RequestParam("term") String query,
			@PageableDefault(page = 0, size = 1) Pageable pageable) {
		if (!StringUtils.hasText(query)) {
			return Collections.emptySet();
		}
		LOGGER.debug("Autocompleting the search box with the term :"+ query);
		
		FacetPage<Document> result = service.autocompleteNameFragment(query, pageable);

		Set<String> titles = new LinkedHashSet<String>();
		for (Page<FacetFieldEntry> page : result.getFacetResultPages()) {
			for (FacetFieldEntry entry : page) {
				if (entry.getValue().contains(query)) { // we have to do this as we do not use terms vector or a string field
					titles.add(entry.getValue());
				}
			}
		}
		return titles;
	}
}

