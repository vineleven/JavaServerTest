package cn.vin.game.fightold;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import cn.vin.Config;

/**
 * 客户端管理器
 * 1、超时管理
 * @author vineleven
 *
 */



public class PlayerMgr extends Thread {
	private boolean isRuning = true;
	private HashMap< String, Player > playerMap = new HashMap<String, Player>( 10 );
	
	
	public synchronized void addPlayer( String pid ){
		playerMap.put( pid, new Player( pid ) );
	}
	
	
	public synchronized boolean isPlayerOnline( String pid ){
		return playerMap.containsKey( pid );
	}
	
	
	public synchronized Player getPlayer( String pid ){
		return playerMap.get( pid );
	}
	
	
	@Override
	public void run() {
		while ( isRuning ) {
			synchronized( this ){
				Player player;
				long curTime = System.currentTimeMillis();
				Iterator< Entry<String, Player> > iterator = playerMap.entrySet().iterator();
				while ( iterator.hasNext() ) {
					Entry<String, Player> entry = iterator.next();					
					player = entry.getValue();
					if( curTime - player.lastRequestTime > Config.CLIENT_TIME_OUT ){
						player.leave();
						iterator.remove();
					}
				}
			}
			
			try {
				Thread.sleep( Config.SERVER_TIME_OUT_INTERVAL );
			} catch (InterruptedException e) {
				e.printStackTrace();
//				break;
			}
		}
	}
	
	
	public void stopRun() {
		isRuning = false;
	}
}
