package client;

import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import com.aonaware.services.webservices.Definition;
import com.aonaware.services.webservices.DictService;
import com.aonaware.services.webservices.DictServiceSoap;
import com.aonaware.services.webservices.WordDefinition;
import com.cdyne.ws.IP2Geo;
import com.cdyne.ws.IP2GeoSoap;

public class IPLocator {
    public IPLocator() {}
	
	public String getLocation(String inputStr) {
		String input_N = new String(inputStr);
		IP2Geo NC_service = new IP2Geo(); //created service object
		IP2GeoSoap NC_serviceSOAP = NC_service.getIP2GeoSoap(); //create SOAP object (a port of the service)
        String result = NC_serviceSOAP.resolveIP(input_N, "0").getCity() + ", " + NC_serviceSOAP.resolveIP(input_N, "0").getCountry();
        return result;
	}
}
