package com.parq.server.dao.model.object;

import java.util.Date;

public class UserActionLog {
	private long userActionLog;
	private long userId;
	private Date logDate;
	private String log;

	public long getUserActionLog() {
		return userActionLog;
	}

	public void setUserActionLog(long userActionLog) {
		this.userActionLog = userActionLog;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

}
