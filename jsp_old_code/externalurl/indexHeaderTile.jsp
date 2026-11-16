<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" />
<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<title>${title}</title>
	
	<style type="text/css">
		body {
			margin-bottom: 0em;
		}
	</style>

	<script type="text/javascript">
		document.observe('dom:loaded', resizeFrame);
	
		Event.observe(window, 'resize', resizeFrame);

		function resizeFrame() {
			var headerHeight = jQuery("#masthead").height() + jQuery("#subhead").height();

			var windowHeight = window.innerHeight || (window.document.documentElement.clientHeight || window.document.body.clientHeight);
			var frameHeight = windowHeight - headerHeight - 35;

			var minHeight = 100;

			if (frameHeight < minHeight)
				frameHeight = minHeight;
			
			jQuery("#externalUrlFrame").height(frameHeight);
			jQuery("#externalUrlFrame").width(jQuery("#masthead").width());
		}
	</script>
