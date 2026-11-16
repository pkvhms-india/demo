<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />
	<script src="<c:url value='/scripts/form.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/webforms.js' />" type="text/javascript"></script>
	<style type="text/css">
		<c:forEach items="${form.uniqueStyleMap}" var="style">td.webform_cell_style_$ {
			style.key.id
		}

			{
			background-color: rgb($ {
					style.key.backColor.red
				}

				, $ {
					style.key.backColor.green
				}

				, $ {
					style.key.backColor.blue
				});

			color: rgb($ {
					style.key.color.red
				}

				, $ {
					style.key.color.green
				}

				, $ {
					style.key.color.blue
				});

			font-family: $ {
				style.key.font.family
			}

			;

			font-size: $ {
				style.key.font.size
			}

			pt;

			font-style: $ {
				style.key.font.style
			}

			;

			font-weight: $ {
				style.key.font.weight
			}

			;

			text-align: $ {
				fn: toLowerCase(style.key.alignment)
			}

			;

			text-decoration: $ {
				style.key.font.decoration
			}

			;
		}

		</c:forEach>
	</style>
	<title>
		<fmt:message key="webforms.pageTitle" />
	</title>
