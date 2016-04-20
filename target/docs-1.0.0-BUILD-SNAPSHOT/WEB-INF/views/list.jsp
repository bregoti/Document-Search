<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
 
<!DOCTYPE html>
<html lang="en">
  <head>
     <jsp:include page="../fragments/meta.jsp"></jsp:include>
  </head>
  <body>
  
	<jsp:include page="../fragments/header.jsp"></jsp:include>
	 
 	 <div class="container">
 	 	
 	  <!--  Header row -->
 	 	<jsp:include page="../fragments/searchBox.jsp"></jsp:include>
 		
 		<div class="container">
			 <c:choose> 
				<c:when test="${fn:length(page.content) == 0}">
					<p class="text-danger"><h3>Sorry, no match found for <strong>'${query}'</strong>.</h3></p>
				</c:when>
				<c:otherwise>
					<p class="text-success"><h3>Found ${page.totalElements} item(s). </h3></p>
					<fmt:setLocale value="en_US"/>	
					<c:forEach var="doc" items="${page.content}" varStatus="status">
						<div class="ui-widget-content ui-corner-all" style="margin-top: .5em; padding-left: .5em; background: ${status.index %2==0 ?'#eee':'#fff'}">
									<p><h3 class="breakword"><a href="<c:url value="/downloadFile/?filePath=${doc.resourcename}" />" class="doclink" target="_blank" >${doc.fileName}</a></h3></p>
									<c:if test="${not empty doc.title}">
										<p><strong>Title:</strong><c:out value="${doc.title}"/></p>
									</c:if>
									<c:if test="${not empty doc.subject}">
										<p>Subject:<c:out value="${doc.subject}"/></p>
									</c:if>
						        	<c:if test="${not empty doc.keywords}">
										<p>Keywords:<c:out value="${doc.keywords}"/></p>
									</c:if>
						        	<c:if test="${not empty doc.author}">
										<p>Author:<c:out value="${doc.author}"/></p>
									</c:if>
						        	<c:if test="${not empty doc.resourcename}">
										<p>File Location:<c:out value="${doc.resourcename}"/></p>
									</c:if>
						        	<c:if test="${not empty doc.lastModified}">
										<p>Last Modified:<c:out value="${doc.lastModified}"/></p>
									</c:if>
								<p>
									<c:forEach var="highlight" items="${page.getHighlights(doc) }" varStatus="highlightStatus">
									${highlight.getSnipplets()}
									</c:forEach>
								</p>
						</div>
					</c:forEach>
						<jsp:include page="../fragments/pager.jsp"></jsp:include>	
					</c:otherwise>
				</c:choose>
		</div>
	</div>
	 
      <jsp:include page="../fragments/footer.jsp"></jsp:include>
    
  </body>
</html>