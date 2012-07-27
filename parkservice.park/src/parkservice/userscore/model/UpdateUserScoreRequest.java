package parkservice.userscore.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class UpdateUserScoreRequest {

	private Score score;

	public void setScore(Score score) {
		this.score = score;
	}

	public Score getScore() {
		return score;
	}
}
