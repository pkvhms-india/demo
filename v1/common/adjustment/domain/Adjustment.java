package com.varicent.common.adjustment.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.varicent.core.math.Precision;

public class Adjustment {
	private int id;
	private String planId;
	private String payeeId;
	private Date date;
	private BigDecimal value;
	private String createdBy;
	private Date createdAt;
	private String comment;
	private boolean manual;

	public Adjustment(int id, String planId, String payeeId, Date date, BigDecimal value, String createdBy,
			Date createdAt, String comment, boolean manual) {
		this.id = id;
		this.planId = planId;
		this.payeeId = payeeId;
		this.date = new Date(date.getTime());
		this.value = Precision.roundMoney(value);
		this.createdBy = createdBy;
		this.createdAt = new Date(createdAt.getTime());
		this.comment = comment;
		this.manual = manual;
	}

	public String getComment() {
		return this.comment;
	}

	public Date getCreatedAt() {
		return (Date) this.createdAt.clone();
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public Date getDate() {
		return (Date) this.date.clone();
	}

	public int getId() {
		return this.id;
	}

	public String getPayeeId() {
		return this.payeeId;
	}

	public String getPlanId() {
		return this.planId;
	}

	public BigDecimal getValue() {
		return this.value;
	}

	public boolean isManual() {
		return this.manual;
	}
}
