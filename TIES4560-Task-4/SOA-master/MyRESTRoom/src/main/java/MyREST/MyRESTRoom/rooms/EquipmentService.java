package MyREST.MyRESTRoom.rooms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EquipmentService {
	
	// HashMap which keys match to room ids.
	private static HashMap<Integer, List<Equipment>> eqMap = new HashMap<Integer, List<Equipment>>();

	
	public List<Equipment> getRoomEquipments(int roomId) {
		return eqMap.get(roomId);
	}

	/*
	 * Pystyykö muuttamaan Equipmentiksi? CustomerHistory-tyylisesti
	 */
	public Equipment addRoomEquipments(int roomId, Equipment eq) {
		if(eqMap.containsKey(roomId)) {
			List<Equipment> list = eqMap.get(roomId);
			list.add(eq);
			return eq;
		}
		else {
			List<Equipment> newList = new ArrayList<Equipment>();
			newList.add(eq);
			eqMap.put(roomId, newList);
			return eq;
		}
	}

	public List<Equipment> deleteRoomEquipments(int roomId) {
		return eqMap.remove(roomId);
	}

}
