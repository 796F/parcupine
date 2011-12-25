package parkservice.resources;
/*

 * Before working with this sample code, please be sure to read the accompanying Readme.txt file.
 * It contains important information regarding the appropriate use of and conditions for this sample
 * code. Also, please pay particular attention to the comments included in each individual code file,
 * as they will assist you in the unique and correct implementation of this code on your specific platform.
 *
 * Copyright 2008 Authorize.Net Corp.
 */

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import AuthNet.Rebill.MerchantAuthenticationType;
import AuthNet.Rebill.Service;
import AuthNet.Rebill.ServiceSoap;

public class SoapAPIUtilities{
	
	public static final String EXAMPLE_WSDL_URL = "https://apitest.authorize.net/soap/v1/Service.asmx?wsdl";
	
	// TODO: Specify you merchant account name
	//
	public static final String EXAMPLE_MERCHANT_NAME = "2KBJ4ry77f";

	// TODO: Specify you merchant transaction key
	//
	public static final String EXAMPLE_TRANSACTION_KEY = "2C38t2j2Y7h7kPbk";
	
	private static MerchantAuthenticationType m_auth = null;
	
	public static String getExampleLabel(String example_name){
		
		return "\r\nAuthorize.Net Customer API: " + example_name + "\r\nURL: " + EXAMPLE_WSDL_URL + "\r\nMERCHANT: " + EXAMPLE_MERCHANT_NAME + "\r\n\r\n";
	}
	
	public static MerchantAuthenticationType getMerchantAuthentication(){
		if(m_auth == null){
			m_auth = new MerchantAuthenticationType();

			// TODO: Specify Merchant Name
			//
			//m_auth.setName("MerchantName");
			m_auth.setName(EXAMPLE_MERCHANT_NAME);
			
			// TODO: Specify Transaction Key
			//
			//m_auth.setTransactionKey("TransactionKey");
			m_auth.setTransactionKey(EXAMPLE_TRANSACTION_KEY);
		}
		return m_auth;
	}
	
	private static ServiceSoap service_soap = null;
	public static ServiceSoap getServiceSoap(){
		if(service_soap != null) return service_soap;
		return getServiceSoap(EXAMPLE_WSDL_URL);
	}
	public static ServiceSoap getServiceSoap(String url){
	
		URL wsdl_url = null;
		try {
			wsdl_url = new URL(url);
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if(wsdl_url == null){
			return null;
		}

		Service rebill_ws = new Service(wsdl_url, new QName("https://api.authorize.net/soap/v1/", "Service"));
		service_soap = rebill_ws.getServiceSoap();
		return service_soap;
	}

}
