package client;

import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import com.aonaware.services.webservices.Definition;
import com.aonaware.services.webservices.DictService;
import com.aonaware.services.webservices.DictServiceSoap;
import com.aonaware.services.webservices.WordDefinition;

public class WordDefinator {
    public WordDefinator() {}
	
	public String defineWord(String inputStr) {
		String input_N = new String(inputStr);
		DictService NC_service = new DictService(); //created service object
		DictServiceSoap NC_serviceSOAP = NC_service.getDictServiceSoap(); //create SOAP object (a port of the service)
        List<Definition> result = NC_serviceSOAP.define(input_N).getDefinitions().getDefinition();
        String b = "";
        for(Definition item : result) {
        	b += item.getWordDefinition() + " ";
        }
        
        return b;
	}
}
