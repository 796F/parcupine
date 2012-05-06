package parkservice.gridservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GpsCoorWithOrder extends GpsCoordinate {
	private int order;

	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}
	
}
