package cn.vin.game.fightold;

import java.util.Hashtable;

public class RoomMgr {
	private Hashtable<String, Room> roomTable = new Hashtable<String, Room>();
	
	
	public Room addRoom( String roomId ){
		Room room = new Room( roomId );
		roomTable.put( roomId, room );
		return room;
	}
	
	
	public Room getRoom( String roomId ){
		return roomTable.get( roomId );
	}
	
	
	public void removeRoom( String roomId ){
		roomTable.remove( roomId );
	}
	
	
	public int getRoomCount(){
		return roomTable.size();
	}
	
	
	public String getRoomIds(){
		return roomTable.keys().toString();
	}
}
