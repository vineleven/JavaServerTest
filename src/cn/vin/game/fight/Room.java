package cn.vin.game.fight;

import cn.vin.util.Tools;



/**
 * 容纳两个player，由GameServer管理
 * @author vineleven
 *
 */
public class Room {
	public static final int ST_EMPTY = 0;
	public static final int ST_WAIT = 0;
	public static final int ST_FULL = 0;
	public static final int ST_PLAY = 0;
	
	
	private int curState = ST_EMPTY;
	
	private Player player1 = null, player2 = null;
	
	public String name = null;
	
	
	public Room( String name ) {
		this.name = name;
	}

	
	public synchronized boolean addPlayer( Player player ){
		if( player1 == null ){
			player1 = player;
			return true;
		}
		
		if( player2 == null ){
			player2 = player;
			return true;
		}
		
		updateState();
		return true;
	}
	
	
	public synchronized void removePlayer( Player player ){
		if( player1 == player ){
			player1 = null;
		} else if( player2 == player ){
			player2 = null;
		}
	}
	
	
	private void updateState(){
		if( player1 == null && player2 == null ) {
			curState = ST_EMPTY;
		} else if ( player1 != null && player2 != null ) {
			curState = ST_FULL;
		} else {
			curState = ST_WAIT;
		}
		
		if( player1 == player2 ){
			Tools.error( "The Same Player In Room: " + name );
		}
	}
	
	
	public synchronized boolean canJoin(){
		return curState == ST_WAIT;
	}
	
	
	public synchronized boolean isPlaying(){
		return curState == ST_PLAY;
	}
	
	
	public synchronized void playerTurn( Player player ){
		if( isPlaying() ){ return; }
		if( player1 == player ){
//			player2.putResponse( );
		}
		
	}
	
	
	public synchronized boolean nextTurn(){
		return true;
	}
}
