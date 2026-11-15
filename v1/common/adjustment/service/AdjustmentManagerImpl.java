package com.varicent.common.adjustment.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.varicent.common.adjustment.dao.AdjustmentDao;
import com.varicent.common.adjustment.domain.Adjustment;
import com.varicent.common.audit.dao.AuditDao;
import com.varicent.common.payee.dao.PayeeDao;
import com.varicent.common.payee.domain.Payee;
import com.varicent.common.plan.dao.PlanDao;
import com.varicent.common.plan.domain.Plan;
import com.varicent.common.time.dao.TimeDao;
import com.varicent.common.time.domain.TimePeriod;
import com.varicent.core.i18n.Message;
import com.varicent.core.i18n.Messages;
import com.varicent.security.domain.UserAccount;
import com.varicent.workflow.dao.WorkflowDao;

@Service("adjustmentManager")
@Transactional(readOnly = true)
public class AdjustmentManagerImpl implements AdjustmentManager {
	private final AdjustmentDao adjustmentDao;
	private final PayeeDao payeeDao;
	private final PlanDao planDao;
	private final TimeDao timeDao;
	private final WorkflowDao workflowDao;
	private final AuditDao auditDao;

	@Autowired
	public AdjustmentManagerImpl(AdjustmentDao adjustmentDao, PayeeDao payeeDao, PlanDao planDao, TimeDao timeDao,
			WorkflowDao workflowDao, AuditDao auditDao) {
		this.adjustmentDao = adjustmentDao;
		this.payeeDao = payeeDao;
		this.planDao = planDao;
		this.timeDao = timeDao;
		this.workflowDao = workflowDao;
		this.auditDao = auditDao;
	}

	@Override
	@Transactional(readOnly = false)
	public void addManualAdjustment(String planId, String payeeId, String period, BigDecimal value, String comment) {
		if (this.authorized(planId, payeeId)) {
			Plan plan = this.planDao.getPlan(planId);
			List<TimePeriod> periods = this.timeDao.getLowestTimePeriods(plan.getCalendar());
			Date periodEnd = null;

			for (TimePeriod p : periods) {
				if (p.getName().equals(period)) {
					periodEnd = p.getEnding();
					break;
				}
			}

			UserAccount currentUser = UserAccount.getCurrent();
			String currentUserPayeeId = currentUser.getPayeeId();
			Adjustment adjustment = new Adjustment(-1, planId, payeeId, periodEnd, value, currentUserPayeeId,
					new Date(), comment, true);
			this.adjustmentDao.addManualAdjustment(adjustment);
			Object[] values = new Object[] { this.payeeDao.getPayee(payeeId).GetFormattedName(), value,
					this.payeeDao.getPayee(currentUserPayeeId).GetFormattedName() };
			this.auditDao.log(new Message(Messages.Key.MODULE_WEB, new Object[0]),
					new Message(Messages.Key.EVENT_EDIT_SUBMIT, new Object[0]),
					new Message(Messages.Key.EVENT_WEBFORM_UPDATE_MANUALADJUST_MESSAGE, values));
		}
	}

	@Override
	public Payee getPayee(String payeeId) {
		return this.payeeDao.getPayee(payeeId);
	}

	@Override
	public Plan getPlan(String planId) {
		return this.planDao.getPlan(planId);
	}

	private boolean authorized(String planId, String payeeId) {
		Set<String> viewablePayees = this.workflowDao.getAuthorizedPayeeIds(planId);
		return this.workflowDao.canAdjust(planId) && viewablePayees.contains(payeeId);
	}
}
