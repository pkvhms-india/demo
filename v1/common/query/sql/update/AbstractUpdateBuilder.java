package com.varicent.common.query.sql.update;

import org.springframework.util.Assert;

import com.varicent.common.query.domain.Clause;
import com.varicent.common.query.domain.Constraint;
import com.varicent.common.query.domain.CustomConstraint;
import com.varicent.common.query.domain.DataField;
import com.varicent.common.query.domain.IConstraint;
import com.varicent.common.query.domain.Literal;
import com.varicent.common.query.domain.QueryDefinition;
import com.varicent.common.query.domain.SubQueryConstraint;
import com.varicent.common.query.domain.UpdateDefinition;
import com.varicent.common.query.domain.UpdateSourceItem;

public abstract class AbstractUpdateBuilder implements UpdateBuilder {
	@Override
	public abstract String toSql(UpdateDefinition var1);

	private boolean isSourceConstraint(UpdateDefinition update, Constraint c) {
		DataField left = (DataField) c.getLeft();
		if (!this.isSourceDataField(update, left)) {
			return false;
		} else if (c.getOperator() != Constraint.Operator.IsNotNull && c.getOperator() != Constraint.Operator.IsNull) {
			if (c.getRight() instanceof Literal) {
				return true;
			} else {
				DataField right = (DataField) c.getRight();
				return this.isSourceDataField(update, right);
			}
		} else {
			return true;
		}
	}

	private boolean isSourceCustomConstraint(UpdateDefinition update, CustomConstraint c) {
		return false;
	}

	private boolean isSourceDataField(UpdateDefinition update, DataField f) {
		return this.isSourceName(update, f.getTable());
	}

	protected boolean isSourceRestriction(UpdateDefinition update, IConstraint c) {
		if (c instanceof Clause) {
			for (IConstraint constraint : ((Clause) c).getConstraints()) {
				if (!this.isSourceRestriction(update, constraint)) {
					return false;
				}
			}

			return true;
		} else if (c instanceof Constraint) {
			return this.isSourceConstraint(update, (Constraint) c);
		} else if (c instanceof CustomConstraint) {
			return this.isSourceCustomConstraint(update, (CustomConstraint) c);
		} else if (c instanceof SubQueryConstraint) {
			return this.isSourceSubQueryConstraint(update, (SubQueryConstraint) c);
		} else {
			Assert.state(false);
			return false;
		}
	}

	private boolean isSourceSubQueryConstraint(UpdateDefinition update, SubQueryConstraint c) {
		return false;
	}

	protected boolean isSourceName(UpdateDefinition update, String name) {
		return name.equals(update.getTable());
	}

	protected void selectSourceItem(UpdateDefinition update, QueryDefinition query, int index) {
		UpdateSourceItem sourceItem = update.getSources().get(index);
		switch (sourceItem.getType()) {
		case FIELD:
			query.select(sourceItem.getField().getTable(), sourceItem.getField().getColumn());
			break;
		case VALUE:
			query.select(sourceItem.getLiteral(), update.getFields().get(index).getColumn());
			break;
		default:
			Assert.state(false);
		}

	}
}
