package cn.vin.game.fight;

import java.util.LinkedList;


/**
 * 玩家
 * @author vineleven
 *
 */


public class Player {
	public String pid = null;
	
	public String name = null;

	public LinkedList<byte[]> responseList = new LinkedList<byte[]>();
	
	
	
	
	
	public Player( String pid ) {
		this.pid = pid;
	}
	
	
	public synchronized byte[] getResponse(){
		if ( responseList.size() != 0 ) {
			return responseList.removeFirst();
		}
		return null;
	}
	
	
	public synchronized void putResponse( byte[] respons ){
		responseList.addLast( respons );
	}
}


