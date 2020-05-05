package MyREST.MyRESTRoom;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerHistoryService {
	
	// HashMap which keys match to Customer ids.
	private static HashMap<Integer, CustomerHistory> historyMap = new HashMap<Integer, CustomerHistory>();

	
	public CustomerHistoryService() {}
	
	public CustomerHistory getCustomerHistory(int customerId) {
		return historyMap.get(customerId);
	}

	public CustomerHistory addCustomerHistory(int customerId, CustomerHistory newHistory) {
		if(historyMap.containsKey(customerId)) {
			CustomerHistory ch = historyMap.get(customerId);
			ch.updateCustomerHistory(newHistory);
			return ch;
		}
		else {
			historyMap.put(customerId, newHistory);
			return newHistory;
		}
	}

	public CustomerHistory deleteCustomerHistory(int customerId, CustomerHistory historyToBeRemoved) {
		ArrayList<Integer> removelist = historyToBeRemoved.getRoomHistory();
		ArrayList<Integer> currentlist = historyMap.get(customerId).getRoomHistory();
		currentlist.removeAll(removelist);
		return historyMap.get(customerId);
	}

}
