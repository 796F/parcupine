package parkservice.userscore.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class GetUserReportingHistoryResponse {

	List<UserParkingStatusReport> reports;

	public List<UserParkingStatusReport> getReports() {
		return reports;
	}

	public void setReports(List<UserParkingStatusReport> reports) {
		this.reports = reports;
	}
}
