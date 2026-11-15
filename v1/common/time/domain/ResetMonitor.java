package com.varicent.common.time.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResetMonitor {
	private final List<String> periods = new ArrayList();
	private final List<Boolean> resets = new ArrayList();

	public ResetMonitor(List<TimePeriod> levelPeriods, List<TimePeriod> resetPeriods) {
		Date resetOn = resetPeriods.size() > 0 ? resetPeriods.get(0).getStarting() : null;
		int i = 0;

		for (TimePeriod period : levelPeriods) {
			boolean reset = period.getStarting().equals(resetOn);
			if (reset) {
				++i;
				if (i < resetPeriods.size()) {
					resetOn = resetPeriods.get(i).getStarting();
				} else {
					resetOn = null;
				}
			}

			this.periods.add(period.getName());
			this.resets.add(reset);
		}

	}

	public boolean doesReset(String currentPeriod, String nextPeriod) {
		int start = this.periods.indexOf(currentPeriod);
		int end = this.periods.indexOf(nextPeriod);

		for (int i = start + 1; i <= end; ++i) {
			if (this.resets.get(i)) {
				return true;
			}
		}

		return false;
	}
}
