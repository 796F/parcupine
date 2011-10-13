This is a simple project to show how to create and use jaw-ws to create and manage webservices

1) The webservice has been tested to work with tomcat. The default deploy URL is:
   http://localhost:8080/TestService

2) A sample SOAP request (tested with soap-ui)

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:TestServiceSchema">
   <soapenv:Header/>
   <soapenv:Body>
      <urn:TestRequest>
	<UserName>JohnDoe</UserName>
	<Password>testPass</Password>
      </urn:TestRequest>
   </soapenv:Body>
</soapenv:Envelope>

3) A sample SOAP response from the above request (taken from soap-ui)

<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
   <S:Body>
      <TestResponse xmlns="urn:TestServiceSchema">
         <UserName xmlns:ns2="urn:TestServiceSchema" xmlns="">JohnDoe</UserName>
         <Password xmlns:ns2="urn:TestServiceSchema" xmlns="">testPass</Password>
      </TestResponse>
   </S:Body>
</S:Envelope>

-Gordon Zheng