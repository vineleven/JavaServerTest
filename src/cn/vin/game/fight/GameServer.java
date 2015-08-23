package cn.vin.game.fight;

import java.util.Hashtable;

import cn.vin.Config;
import cn.vin.util.HttpHead;
import cn.vin.util.Response;
import cn.vin.util.Timer;
import cn.vin.util.Timer.Runner;

public class GameServer {
	
	private Hashtable<String, Room> roomTable = null;
	private Timer timer = null;
	
	
	
	public GameServer() {
		roomTable = new Hashtable<String, Room>();
		timer = new Timer( Config.GAME_SRERVER_TIMER_UPDATE_INTERVAL );
		
		timer.start();
	}
	
	
	
	public byte[] response( Player player, HttpHead head ){
		if( head.isRequestJoinRoom() ){
			return joinRoom( player, head.getValueByIndex( 0 ) );
		} else if ( head.isRequestSuspend() ){
			return suspend( player );
		} else if ( head.isRequestFrameUpdate() ){
			return frameUpdate( player, head.getValueByIndex( 0 ) );
		}
		return null;
	}
	
	
	public byte[] joinRoom( Player player, String roomName ){
		if( roomName == null || "".equals( roomName ) ){ return null; }
		
		synchronized( player ){
			Room room = roomTable.get( roomName );
			if( room == null ){
				room = new Room( roomName );
				room.addPlayer( player );
				roomTable.put( roomName, room );
				return Response.packJoinSuccess( roomName );
			}
			
			if( !room.canJoin() ){
				return Response.packJoinFail( "Room is full." );
			}
			
			room.addPlayer( player );
		}
		
		return Response.packJoinSuccess( roomName );
	}
	
	
	public byte[] suspend( Player player ){
		boolean[] b = { true };
		Runner breakLater = ( obj ) -> b[0] = false;;
		timer.add( breakLater, Config.SERVER_RESPONSE_INTERVAL );
		byte[] result = null;
		while( b[0] ){
			result = player.getResponse();
			if( result != null ){
				return result;
			}
			Thread.yield();
		}
		
		return Response.packSuspend();
	}
	
	
	public byte[] frameUpdate( Player player, String frameData ){
		return null;
	}
}
