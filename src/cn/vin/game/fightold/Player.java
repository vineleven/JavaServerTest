package cn.vin.game.fightold;

import cn.vin.util.Tools;


/**
 * 伪 client
 * @author vineleven
 *
 */




public class Player {
	public static final int ST_LOGIN = 0;
	public static final int ST_WAIT = 1;
	public static final int ST_PLAY = 2;
	
	
//	public static final String CMD_GET_ROOMS = "P_GET_ROOMS";
	public static final String CMD_CREATE_ROOM = "P_CREATE_ROOM";
	public static final String CMD_VISIT_ROOM = "P_VISIT_ROOM";
	
	public static final String CMD_SUSPEND = "P_SUSPEND";
	
//	public static final String CMD_START_GAME = "P_START_GAME";
	public static final String CMD_UPDATE_GAME = "P_UPDATE_GAME";
	
	
	public static RoomMgr roomMgr = new RoomMgr();
	
	
	/**
	 * 最后一次请求时间（超时判断）
	 */
	public long lastRequestTime = 0;
	
	/**
	 * 玩家唯一标识
	 */
	public String PID = "";
	
	
	public String name = "";
	
	/**
	 * 是否在线
	 */
	private boolean isOnline = false;
	
	
	private Room room = null;
	
	
	public Player( String pid ) {
		this.PID = pid;
		this.lastRequestTime = System.currentTimeMillis();
		this.isOnline = true;
	}

	
	public void setName( String name ){
		this.name = name;
	}
	
	
	public void refreshRequestTime(){
		lastRequestTime = System.currentTimeMillis();
	}
	
	
	public void setOnline( boolean bOnline ){
		isOnline = bOnline;
	}
	
	
	public boolean isOnline(){
		return isOnline;
	}
	
	
	public void leave(){
		isOnline = false;
		if( room != null ){
			room.leave( this, roomMgr );
			room = null;
		}
	}
	
	
	public byte[] parseCommand( String cmd, String param ){
		if( CMD_CREATE_ROOM.equals( cmd ) ){
			if( createRoom( param ) ){
				return param.getBytes();
			}
		} else if( CMD_VISIT_ROOM.equals( cmd ) ){
			if( visitRoom( param ) ){
				return param.getBytes();
			}
		} else if ( CMD_SUSPEND.equals( cmd ) ) {
			return suspend();
		} else if ( CMD_UPDATE_GAME.equals( cmd ) ) {
			return updateGame( param );
		}
		
		return null;
	}
	
	
	public boolean createRoom( String roomId ){
		Tools.debugf( "%s createRoom: %s", PID, roomId );
		if( roomId != null && room == null ){
			room = roomMgr.addRoom( roomId );
			room.setCreator( this );
			return true;
		} else {
			return false;
		}
	}
	
	
	public boolean visitRoom( String roomId ){
		Tools.debugf( "%s visitRoom: %s", PID, roomId );
		if( roomId != null && room == null ){
			room = roomMgr.getRoom( roomId );
			if( room != null ){
				room.setVisitor( this );
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * 挂起
	 * @return
	 */
	public byte[] suspend(){
		Tools.debugf( "%s suspend.", PID );
		while ( isOnline && room != null && room.isRoomPrepared() ) {
			return "GAME_START".getBytes();
		}
		return null;
	}
	
	
	/**
	 * 可能挂起
	 * @param param
	 * @return
	 */
	public byte[] updateGame( String param ){
		
		return null;
	}
	
	
	public boolean startGame(){
		if( room != null && room.isRoomPrepared() ){
			room.start( this );
			return true;
		}
		return false;
	}
}
