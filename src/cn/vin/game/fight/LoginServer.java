package cn.vin.game.fight;

import java.util.Hashtable;

import cn.vin.util.HttpHead;
import cn.vin.util.Response;
import cn.vin.util.Tools;


/**
 * 负责玩家登陆、玩家连接状态管理
 * @author vineleven
 *
 */
public class LoginServer {
	private Hashtable<String, Player> playerTable = new Hashtable<String, Player>( 10 );
	

	/**
	 * 重复请求则重复创建，没有影响
	 * @param head
	 * @return
	 */
	public byte[] login( HttpHead head ){
		if( head.isRequestPid() ) {
			String pid = createPid();
			playerTable.put( pid, new Player( pid ) );
			return Response.packPid( pid );
		}
		
		return null;
	}
	
	
	public String createPid(){
		return "/".concat( Tools.getCurDate() );
	}
	
	
	public Player getPlayer( String pid ){
		return playerTable.get( pid );
	}
}
