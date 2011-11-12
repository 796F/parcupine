package parkservice.model;

public class SpecialRate {
	
	private int minTime;
	private int maxTime;
	private int specialIncrement;
	private int specialRate;
	/**
	 * @return the minTime
	 */
	public int getMinTime() {
		return minTime;
	}
	/**
	 * @param minTime the minTime to set
	 */
	public void setMinTime(int minTime) {
		this.minTime = minTime;
	}
	/**
	 * @return the maxTime
	 */
	public int getMaxTime() {
		return maxTime;
	}
	/**
	 * @param maxTime the maxTime to set
	 */
	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}
	/**
	 * @return the specialIncrement
	 */
	public int getSpecialIncrement() {
		return specialIncrement;
	}
	/**
	 * @param specialIncrement the specialIncrement to set
	 */
	public void setSpecialIncrement(int specialIncrement) {
		this.specialIncrement = specialIncrement;
	}
	/**
	 * @return the specialRate
	 */
	public int getSpecialRate() {
		return specialRate;
	}
	/**
	 * @param specialRate the specialRate to set
	 */
	public void setSpecialRate(int specialRate) {
		this.specialRate = specialRate;
	}
	
}
