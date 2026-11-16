<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<title><fmt:message key="bpm.pageTitle" /></title>

<script language="Javascript" type="text/javascript">
	function highlightrow(obj) {
		jQuery(obj).addClass("row_selected");//='#FEFEED';
	}
	
	function unhighlightrow(obj) {
		jQuery(obj).removeClass("row_selected");//'#FFFFFF';
	}

</script>

