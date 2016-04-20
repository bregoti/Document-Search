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
 	 	
 	 	<br/><br/><br/><br/><br/><br/><br/>
 	  <!--  Header row -->
 	    <jsp:include page="../fragments/searchBox.jsp"></jsp:include>
 		
	</div>
  
   <jsp:include page="../fragments/footer.jsp"></jsp:include>
  
  </body>
</html>