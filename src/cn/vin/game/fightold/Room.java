package cn.vin.game.fightold;

/**
 * 只有创建者可以操作room
 * @author vineleven
 *
 */

public class Room {
	public static final int ST_EMPTY = 1;
	public static final int ST_WAIT = 2;
	public static final int ST_FULL = 3;
	
	public static final int ST_PLAYING = 4;
	
	private int curState = ST_EMPTY;
	
	public String id = null;
	
	public Player creator = null;
	public Player visitor = null;
	
	
//	private int state
	
	public Room( String roomId ) {
		id = roomId;
	}
	
	
	public void setCreator( Player creator ){
		this.creator = creator;
		updateRoomState();
	}
	
	
	public void setVisitor( Player visitor ){
		this.visitor = visitor;
		updateRoomState();
	}
	
	
	public void leave( Player leaver, RoomMgr roomMgr ){
		if( leaver != creator && leaver != visitor ){
			return;
		} else if ( leaver == creator ){
			if( visitor == null ){
				creator = null;
				roomMgr.removeRoom( id );
			} else {
				creator = visitor;
				visitor = null;
			}
		} else if ( leaver == visitor ) {
			visitor = null;
		}
		
		updateRoomState();
	}
	
	
	public void updateRoomState(){
		if( creator != null && visitor != null ){
			curState = ST_FULL;
		} else if ( creator != null || visitor != null ){
			curState = ST_WAIT;
		} else {
			curState = ST_EMPTY;
		}
	}
	
	
	public void changeState( int newState ){
		curState = newState;
		//TODO 这里需要发送一个更新消息给所有客户端
	}
	
	
	public boolean isRoomPrepared(){
		return curState == ST_FULL;
	}
	
	
	public boolean isRoomWait(){
		return curState == ST_WAIT;
	}
	
	public boolean isEmpty(){
		return curState == ST_EMPTY;
	}
	
	
	public void start( Player startor ){
		if( startor == creator ){
			if( curState == ST_FULL ){
				curState = ST_PLAYING;
			}			
		}
	}
	
	
}
