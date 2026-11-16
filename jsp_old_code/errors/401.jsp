<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" />

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  
  <title>IBM ICM</title>
  
  <link rel="stylesheet" href="<c:url value="/styles/varicent.css" />" type="text/css" />
  <style type="text/css">
		  .infobox h2 {
				background-color: ${theme.headerColor};
			}
		
			#customize_data .header_row {
				background-color: ${theme.headerColor};
			}
  </style>  
</head>

<body>

	<jsp:directive.include file="/WEB-INF/jsp/header/header.jsp" />

	<div id="errorPage">
		<p class="error"><fmt:message key="error.messages.unauthorized" bundle="${msgLang}" /></p>
	</div>
  
</body>

</html>
