package com.varicent.common.query.xml;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.springframework.util.Assert;

import com.varicent.common.query.domain.AndClause;
import com.varicent.common.query.domain.Clause;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.DataField;
import com.varicent.common.query.domain.Function;
import com.varicent.common.query.domain.ISelectItem;
import com.varicent.common.query.domain.Join;
import com.varicent.common.query.domain.Literal;
import com.varicent.common.query.domain.OrClause;
import com.varicent.common.query.domain.OrderItem;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.domain.SelectAll;
import com.varicent.common.query.domain.SelectItem;
import com.varicent.common.query.domain.SelectLiteral;
import com.varicent.common.query.domain.SelectTableAll;
import com.varicent.common.query.domain.SqlDataSource;

public class QueryDefinitionParser {
	public static QueryDefinition getQueryDefinition(XMLStreamReader streamReader) throws XMLStreamException {
		streamReader.nextTag();
		boolean selectDistinct = false;
		if (streamReader.getLocalName().equals("distinct")) {
			selectDistinct = true;
			streamReader.nextTag();
			streamReader.nextTag();
		}

		List<ISelectItem> selectItems = new ArrayList();
		streamReader.nextTag();

		while (streamReader.getEventType() == 1) {
			String localName = streamReader.getLocalName();
			if (localName.equals("select-item")) {
				selectItems.add(getSelectItem(streamReader));
			} else if (localName.equals("select-literal")) {
				selectItems.add(getSelectLiteral(streamReader));
			} else {
				selectItems.add(getSelectAll(streamReader));
			}
		}

		streamReader.nextTag();
		streamReader.nextTag();
		QueryDefinition query = new QueryDefinition(getDataSource(streamReader));
		if (selectDistinct) {
			query.selectDistinct();
		}

		streamReader.nextTag();

		for (ISelectItem item : selectItems) {
			query.addSelect(item);
		}

		if (streamReader.getLocalName().equals("joins")) {
			streamReader.nextTag();

			while (streamReader.getEventType() == 1) {
				query.joinWith(getJoin(streamReader));
			}

			streamReader.nextTag();
		}

		if (streamReader.getLocalName().equals("where")) {
			streamReader.nextTag();

			while (streamReader.getEventType() == 1) {
				query.setRestriction(getClause(streamReader));
			}

			streamReader.nextTag();
		}

		if (streamReader.getLocalName().equals("order")) {
			streamReader.nextTag();

			while (streamReader.getEventType() == 1) {
				query.addOrder(getOrderItem(streamReader));
			}

			streamReader.nextTag();
		}

		if (streamReader.getLocalName().equals("union")) {
			streamReader.nextTag();
			QueryDefinition unionQuery = getQueryDefinition(streamReader);
			query.union(unionQuery);
			streamReader.nextTag();
		}

		streamReader.nextTag();
		return query;
	}

	public static QueryDefinition getQueryDefinition(String xml) throws XMLStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader streamReader = factory.createXMLStreamReader(new StringReader(xml));
		return getQueryDefinition(streamReader);
	}

	private static OrderItem getOrderItem(XMLStreamReader streamReader) throws XMLStreamException {
		streamReader.nextTag();
		String table = null;
		if (streamReader.getLocalName().equals("table")) {
			table = XMLStreamUtility.getCurrentNodeValue(streamReader);
		}

		String column = XMLStreamUtility.getCurrentNodeValue(streamReader);
		String direction = XMLStreamUtility.getCurrentNodeValue(streamReader);
		streamReader.nextTag();
		return new OrderItem(table, column, OrderItem.Direction.valueOf(direction.toUpperCase()));
	}

	private static Join getJoin(XMLStreamReader streamReader) throws XMLStreamException {
		String joinType = streamReader.getAttributeValue(0);
		streamReader.nextTag();
		Join join = new Join(getDataSource(streamReader), Join.Type.valueOf(joinType.toUpperCase()));

		while (streamReader.getEventType() == 1) {
			join.setRestriction(getClause(streamReader));
		}

		streamReader.nextTag();
		return join;
	}

	private static Clause getClause(XMLStreamReader streamReader) throws XMLStreamException {
		String attrib = streamReader.getAttributeValue(0);
		Clause.Type clauseType = Clause.Type.valueOf(attrib.toUpperCase());
		Clause clause = clauseType == Clause.Type.AND ? new AndClause() : new OrClause();
		streamReader.nextTag();

		while (streamReader.getEventType() == 1) {
			if (streamReader.getLocalName().equals("clause")) {
				clause.addConstraint(getClause(streamReader));
			} else {
				streamReader.nextTag();
				String table = XMLStreamUtility.getCurrentNodeValue(streamReader);
				String column = XMLStreamUtility.getCurrentNodeValue(streamReader);
				String operator = XMLStreamUtility.getCurrentNodeValue(streamReader);
				if (operator.equals("IsNull")) {
					clause.addConstraint(Constraint.newNullConstraint(table, column, true));
					streamReader.nextTag();
					streamReader.nextTag();
				} else if (operator.equals("IsNotNull")) {
					clause.addConstraint(Constraint.newNullConstraint(table, column, false));
					streamReader.nextTag();
					streamReader.nextTag();
				} else {
					clause.addConstraint(Constraint.newConstraint(table, column, Constraint.Operator.valueOf(operator),
							getOperand(streamReader)));
					streamReader.nextTag();
				}

				streamReader.nextTag();
			}
		}

		streamReader.nextTag();
		return clause;
	}

	private static ISelectItem getSelectItem(XMLStreamReader streamReader) throws XMLStreamException {
		ISelectItem selectItem = null;
		streamReader.nextTag();
		String table = XMLStreamUtility.getCurrentNodeValue(streamReader);
		String column = XMLStreamUtility.getCurrentNodeValue(streamReader);
		if (streamReader.getEventType() == 1) {
			String alias = XMLStreamUtility.getCurrentNodeValue(streamReader);
			selectItem = new SelectItem(table, column, alias, SelectItem.Type.NONE);
		} else {
			selectItem = new SelectItem(table, column);
		}

		streamReader.nextTag();
		return selectItem;
	}

	private static ISelectItem getSelectLiteral(XMLStreamReader streamReader) throws XMLStreamException {
		ISelectItem selectItem = null;
		Literal literal = (Literal) getOperand(streamReader);
		if (streamReader.getEventType() == 1) {
			String alias = XMLStreamUtility.getCurrentNodeValue(streamReader);
			selectItem = new SelectLiteral(literal, alias);
		} else {
			selectItem = new SelectLiteral(literal, (String) null);
		}

		streamReader.nextTag();
		return selectItem;
	}

	private static ISelectItem getSelectAll(XMLStreamReader streamReader) throws XMLStreamException {
		ISelectItem selectItem = null;
		streamReader.nextTag();
		if (streamReader.getEventType() == 1) {
			String table = XMLStreamUtility.getCurrentNodeValue(streamReader);
			selectItem = new SelectTableAll(table);
		} else {
			selectItem = new SelectAll();
		}

		streamReader.nextTag();
		return selectItem;
	}

	private static Object getOperand(XMLStreamReader streamReader) throws XMLStreamException {
		streamReader.nextTag();
		if (streamReader.getEventType() == 2) {
			return null;
		} else if (streamReader.getLocalName().equals("literal")) {
			String typeAttribute = streamReader.getAttributeValue(0);
			Literal.Type type = Literal.Type.valueOf(typeAttribute.toUpperCase());
			String nodeValue = XMLStreamUtility.getCurrentNodeValue(streamReader);
			Object value;
			switch (type) {
			case STRING:
				value = nodeValue;
				break;
			case INTEGER:
				value = new Integer(nodeValue);
				break;
			case DECIMAL:
				value = new BigDecimal(nodeValue);
				break;
			case DATE:
				value = Date.valueOf(nodeValue);
				break;
			case DATETIME:
				value = Timestamp.valueOf(nodeValue);
				break;
			case SYSTEMTIME:
				type = Literal.Type.FUNCTION;
				Object var8 = Function.getDate();
				return new Literal(type, var8);
			case NULL:
				value = null;
				break;
			default:
				Assert.state(false);
				value = null;
			}

			return new Literal(type, value);
		} else {
			String table = XMLStreamUtility.getCurrentNodeValue(streamReader);
			String column = XMLStreamUtility.getCurrentNodeValue(streamReader);
			return new DataField(table, column);
		}
	}

	private static SqlDataSource getDataSource(XMLStreamReader streamReader) throws XMLStreamException {
		streamReader.nextTag();
		if (streamReader.getLocalName().equals("table")) {
			String table = XMLStreamUtility.getCurrentNodeValue(streamReader);
			String alias = null;
			if (streamReader.getLocalName().equals("alias")) {
				alias = XMLStreamUtility.getCurrentNodeValue(streamReader);
			}

			streamReader.nextTag();
			return new SqlDataSource(table, alias);
		} else if (streamReader.getLocalName().equals("query")) {
			QueryDefinition query = getQueryDefinition(streamReader);
			String alias = null;
			if (streamReader.getLocalName().equals("alias")) {
				alias = XMLStreamUtility.getCurrentNodeValue(streamReader);
			}

			streamReader.nextTag();
			return alias != null ? new SqlDataSource(query, alias) : new SqlDataSource(query);
		} else {
			streamReader.nextTag();
			return null;
		}
	}
}
