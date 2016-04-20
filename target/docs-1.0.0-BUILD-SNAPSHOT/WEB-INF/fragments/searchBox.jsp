<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>

<div class="row">
	 <div class="col-md-12">
 	 	<form:form class="form"  action="/docs/search" method="GET" role="search" name="searchform">
               <div class="input-group">
                    <input type="text" class="form-control input-lg" placeholder="What are searching for?" id="queryField" name="q" value="${query}">
                   <div class="input-group-btn">
                       <button class="btn btn-lg btn-primary" type="submit"><i class="glyphicon glyphicon-search"></i></button>
                   </div> 
               </div>
               <script type="text/javascript">
		/* 	    $(function() {
			    	$( "#queryField" ).autocomplete({
			    		source: '${pageContext.request.contextPath}/autocomplete',
						minLength: 1,
			    		select: function( event, ui ) {   
			    			$( "#queryField" ).value=ui.item.value; 
			    		    document.searchform.submit();  
			    		}
			    	});
			    }); */
			    </script>
		
         </form:form>
  	</div>
</div> 