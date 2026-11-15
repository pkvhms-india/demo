package com.varicent.common.query.sql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.varicent.common.query.domain.AndClause;
import com.varicent.common.query.domain.Clause;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.CustomConstraint;
import com.varicent.common.query.domain.DataField;
import com.varicent.common.query.domain.Function;
import com.varicent.common.query.domain.GroupItem;
import com.varicent.common.query.domain.IConstraint;
import com.varicent.common.query.domain.ISelectItem;
import com.varicent.common.query.domain.Join;
import com.varicent.common.query.domain.Literal;
import com.varicent.common.query.domain.OrderAggregate;
import com.varicent.common.query.domain.OrderItem;
import com.varicent.common.query.domain.SelectAggregate;
import com.varicent.common.query.domain.SelectAll;
import com.varicent.common.query.domain.SelectCustom;
import com.varicent.common.query.domain.SelectItem;
import com.varicent.common.query.domain.SelectLiteral;
import com.varicent.common.query.domain.SelectTableAll;
import com.varicent.common.query.domain.SqlDataSource;
import com.varicent.common.query.domain.SubQueryConstraint;
import com.varicent.common.query.domain.Variable;
import com.varicent.core.database.portability.SqlSyntax;

public class SqlBuilder {
	private final SqlSyntax syntax;

	public SqlBuilder(SqlSyntax syntax) {
		this.syntax = syntax;
	}

	private String getSql(Constraint constraint) {
		DataField left = (DataField) constraint.getLeft();
		if (constraint.getOperator() == Constraint.Operator.IsNull) {
			return this.getSql(left) + " IS NULL";
		} else if (constraint.getOperator() == Constraint.Operator.IsNotNull) {
			return this.getSql(left) + " IS NOT NULL";
		} else {
			Object right = constraint.getRight();
			String rightSql = null;
			if (right instanceof Literal) {
				rightSql = this.getSql((Literal) right);
			} else if (right instanceof DataField) {
				rightSql = this.getSql((DataField) right);
			} else if (right instanceof Variable) {
				rightSql = this.getSql((Variable) right);
			} else {
				Assert.state(false);
			}

			return this.getSql(left) + " " + this.getSql(constraint.getOperator()) + " " + rightSql;
		}
	}

	public String getSql(Constraint.Operator operator) {
		switch (operator) {
		case LessThan:
			return "<";
		case LessThanEquals:
			return "<=";
		case Equals:
			return "=";
		case NotEquals:
			return "<>";
		case GreaterThan:
			return ">";
		case GreaterThanEquals:
			return ">=";
		default:
			Assert.state(false);
			return null;
		}
	}

	private String getSql(CustomConstraint constraint) {
		if (this.syntax.isSqlServer()) {
			return constraint.getSqlServerConstraint();
		} else {
			Assert.state(false);
			return null;
		}
	}

	public String getSql(DataField field) {
		return this.syntax.quote(field.getTable()) + "." + this.syntax.quote(field.getColumn());
	}

	public String getSql(Variable variable) {
		return "@" + variable.getName();
	}

	public String getSql(Function fn) {
		switch (fn.getType()) {
		case GET_DATE:
			return this.syntax.getDate();
		case GET_DATETIME:
			return this.syntax.getDateTime();
		case GET_DATE_UTC:
			return this.syntax.getDateUtc();
		case GET_DATETIME_UTC:
			return this.syntax.getDateTimeUtc();
		default:
			Assert.state(false);
			return null;
		}
	}

	public String getSql(IConstraint iConstraint) {
		if (!(iConstraint instanceof Clause)) {
			if (iConstraint instanceof Constraint) {
				return this.getSql((Constraint) iConstraint);
			} else if (iConstraint instanceof CustomConstraint) {
				return this.getSql((CustomConstraint) iConstraint);
			} else if (iConstraint instanceof SubQueryConstraint) {
				return this.getSql((SubQueryConstraint) iConstraint);
			} else {
				Assert.state(false);
				return null;
			}
		} else {
			Clause clause = (Clause) iConstraint;
			List<String> constraintSql = new ArrayList();

			for (IConstraint ic : clause.getConstraints()) {
				constraintSql.add("(" + this.getSql(ic) + ")");
			}

			String delim = clause instanceof AndClause ? " AND " : " OR ";
			return StringUtils.collectionToDelimitedString(constraintSql, delim);
		}
	}

	public String getSql(ISelectItem item) {
		if (item instanceof SelectAll) {
			return "*";
		} else if (item instanceof SelectItem) {
			return this.getSql((SelectItem) item);
		} else if (item instanceof SelectLiteral) {
			return this.getSql((SelectLiteral) item);
		} else if (item instanceof SelectAggregate) {
			return this.getSql((SelectAggregate) item);
		} else if (item instanceof SelectCustom) {
			return this.getSql((SelectCustom) item);
		} else if (item instanceof SelectTableAll) {
			return this.getSql((SelectTableAll) item);
		} else if (item instanceof SelectAll) {
			return this.getSql((SelectAll) item);
		} else {
			Assert.state(false);
			return null;
		}
	}

	public String getSql(Join join) {
		String joinSql = this.getSql(join.getJoinType()) + " " + this.getSql(join.getSource());
		if (join.getRestriction().hasConstraints()) {
			joinSql = joinSql + " ON " + this.getSql(join.getRestriction());
		}

		return joinSql;
	}

	public String getSql(Join.Type joinType) {
		switch (joinType) {
		case INNER:
			return "INNER JOIN";
		case LEFT:
			return "LEFT JOIN";
		case RIGHT:
			return "RIGHT JOIN";
		case CROSS:
			return "CROSS JOIN";
		default:
			Assert.state(false);
			return null;
		}
	}

	public String getSql(Literal literal) {
		switch (literal.getType()) {
		case NULL:
			return "NULL";
		case STRING:
		case LONG_STRING:
			return this.syntax.cast((String) literal.getValue());
		case INTEGER:
			return literal.getValue().toString();
		case DECIMAL:
			return this.syntax.cast((BigDecimal) literal.getValue());
		case DATE:
			return this.syntax.toDate(literal.getValue());
		case DATETIME:
			return this.syntax.toDateTime(literal.getValue());
		case FUNCTION:
			return this.getSql((Function) literal.getValue());
		case BLOB:
			return this.syntax.toBlob((byte[]) literal.getValue());
		default:
			Assert.state(false);
			return null;
		}
	}

	public String getSql(GroupItem item) {
		StringBuilder sb = new StringBuilder();
		if (item.hasTable()) {
			sb.append(this.syntax.quote(item.getTable()));
			sb.append(".");
		}

		sb.append(this.syntax.quote(item.getColumn()));
		return sb.toString();
	}

	public String getSql(OrderItem item) {
		StringBuilder sb = new StringBuilder();
		if (item instanceof OrderAggregate) {
			sb.append(this.getSql(((OrderAggregate) item).getType()));
			sb.append("(");
		}

		if (item.hasTable()) {
			sb.append(this.syntax.quote(item.getTable()));
			sb.append(".");
		}

		sb.append(this.syntax.quote(item.getColumn()));
		if (item instanceof OrderAggregate) {
			sb.append(")");
		}

		sb.append(" ");
		sb.append(this.getSql(item.getDirection()));
		return sb.toString();
	}

	public String getSql(OrderItem.Direction direction) {
		switch (direction) {
		case ASCENDING:
			return "ASC";
		case DESCENDING:
			return "DESC";
		default:
			Assert.state(false);
			return null;
		}
	}

	public String getSql(SelectAggregate item) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getSql(item.getType()));
		sb.append("(");
		if (item.getType() == SelectAggregate.Type.COUNT) {
			sb.append("*");
		} else {
			DataField field = new DataField(item.getTable(), item.getColumn());
			sb.append(this.getSql(field));
		}

		sb.append(")");
		if (item.hasAlias()) {
			sb.append(" AS ");
			sb.append(this.syntax.quote(item.getAlias()));
		}

		return sb.toString();
	}

	public String getSql(SelectAggregate.Type type) {
		switch (type) {
		case AVG:
			return "AVG";
		case COUNT:
			return "COUNT";
		case MAX:
			return "MAX";
		case MIN:
			return "MIN";
		case SUM:
			return "SUM";
		default:
			Assert.state(false);
			return null;
		}
	}

	public String getSql(SelectCustom item) {
		StringBuilder sb = new StringBuilder();
		sb.append(item.getSqlServer());
		if (item.hasAlias()) {
			sb.append(" AS ");
			sb.append(this.syntax.quote(item.Alias));
		}

		return sb.toString();
	}

	public String getSql(SelectTableAll item) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.syntax.quote(item.getTable()));
		sb.append(".*");
		return sb.toString();
	}

	public String getSql(SelectAll item) {
		return "*";
	}

	public String getSql(SelectItem item) {
		DataField field = new DataField(item.getTable(), item.getColumn());
		String itemSql = "";
		if (item.getType() != SelectItem.Type.NONE) {
			itemSql = itemSql + "CAST(";
		}

		itemSql = itemSql + this.getSql(field);
		switch (item.getType()) {
		case INT:
			itemSql = itemSql + " AS " + this.syntax.toDatabaseType(Literal.Type.INTEGER) + ")";
			break;
		case DECIMAL:
			itemSql = itemSql + " AS " + this.syntax.toDatabaseType(Literal.Type.DECIMAL) + ")";
			break;
		case DATE_TIME:
			itemSql = itemSql + " AS " + this.syntax.toDatabaseType(Literal.Type.DATETIME) + ")";
			break;
		case STRING:
			itemSql = itemSql + " AS " + this.syntax.toDatabaseType(Literal.Type.STRING) + ")";
			break;
		case BLOB:
			itemSql = itemSql + " AS " + this.syntax.toDatabaseType(Literal.Type.BLOB) + ")";
		}

		if (item.hasAlias()) {
			itemSql = itemSql + " AS " + this.syntax.quote(item.getAlias());
		}

		return itemSql;
	}

	public String getSql(SelectLiteral item) {
		String itemSql = this.getSql(item.getValue());
		if (item.hasAlias()) {
			itemSql = itemSql + " " + this.syntax.quote(item.getAlias());
		}

		return itemSql;
	}

	public String getSql(SqlDataSource source) {
		StringBuilder sb = new StringBuilder();
		if (source.isTable()) {
			sb.append(this.syntax.quote(source.getTable()));
		} else {
			sb.append("(");
			QueryBuilder qb = new QueryBuilder(this.syntax);
			sb.append(qb.toSql(source.getQuery()));
			sb.append(")");
		}

		if (source.hasAlias()) {
			sb.append(" ");
			sb.append(this.syntax.quote(source.getAlias()));
		}

		return sb.toString();
	}

	private String getSql(SubQueryConstraint constraint) {
		StringBuilder sb = new StringBuilder();
		if (constraint.getLeft() != null) {
			sb.append(this.getSql(constraint.getLeft()));
			sb.append(" ");
		}

		sb.append(this.getSql(constraint.getOperator()));
		sb.append(" (");
		QueryBuilder qb = new QueryBuilder(this.syntax);
		sb.append(qb.toSql(constraint.getRight()));
		sb.append(")");
		return sb.toString();
	}

	public String getSql(SubQueryConstraint.Operator operator) {
		switch (operator) {
		case In:
			return "IN";
		case NotIn:
			return "NOT IN";
		case Exists:
			return "EXISTS";
		case NotExists:
			return "NOT EXISTS";
		case Equals:
			return "=";
		case LessThanEquals:
			return "<=";
		default:
			Assert.state(false);
			return null;
		}
	}
}
