package com.parq.web;


public class ReportDateRangeFilter {
	private DateRange dateRangeFilter = DateRange.NONE;
	
	private CallerPage callerPage;
	private boolean jumpToReportSection = false;
	

	public enum DateRange {
		NONE, THIS_MONTH, LAST_MONTH, LAST_THREE_MONTH
	}
	
	public enum CallerPage {
		ADMIN, USER
	}

	/**
	 * @return the filter
	 */
	public String getDateRangeFilter() {
		return dateRangeFilter.toString();
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setDateRangeFilter(String filter) {
		this.dateRangeFilter = DateRange.valueOf(filter);
	}

	/**
	 * @return the callerPage
	 */
	public CallerPage getCallerPage() {
		return callerPage;
	}

	/**
	 * @param callerPage the callerPage to set
	 */
	public void setCallerPage(CallerPage callerPage) {
		this.callerPage = callerPage;
	}

	/**
	 * @return the jumpToReportSection
	 */
	public boolean isJumpToReportSection() {
		return jumpToReportSection;
	}

	/**
	 * @param jumpToReportSection the jumpToReportSection to set
	 */
	public void setJumpToReportSection(boolean jumpToReportSection) {
		this.jumpToReportSection = jumpToReportSection;
	};
}
