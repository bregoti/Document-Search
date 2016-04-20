<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" href="http://www.impaqint.com/sites/default/files/favicon_0.png" type="image/png" />
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>iSearch</title>

    <!-- Bootstrap -->

	<spring:url value="/resources/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

	<spring:url value="/resources/css/bootstrap-theme.min.css" var="bootstrapThemeCss"/>
    <link href="${bootstrapThemeCss}" rel="stylesheet"/>

	<spring:url value="/resources/js/bootstrap.min.js" var="bootstrapJs"/>
    <script src="${bootstrapJs}"></script>
      
    <spring:url value="/resources/css/productsearch.css" var="productsearchCss"/>
    <link href="${productsearchCss}" rel="stylesheet"/>
	
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
      <%--   <spring:url value="/resources/js/jquery-ui.min.js" var="jQueryUI"/>
    <script src="${jQueryUI}"></script> --%>
    
    <link href="http://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css" rel="stylesheet">
    <script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
      
      
    <!-- Include all compiled plugins (below), or include individual files as needed -->

    
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->