package cn.vin.game.fightold;

import java.awt.List;
import java.util.ArrayList;
import java.util.Hashtable;

import cn.vin.util.Timer;
import cn.vin.util.Timer.Runner;

public class TestThread extends Thread {
    private static ArrayList< Room > list = new ArrayList<Room>();
    private int index;
    public TestThread( int index ) {
    	this.index = index;
	}
 
    public void m1() {
    	Room room = list.get( this.index );
        synchronized ( room ) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println( "b1" );
        }
    }
 
    public void m2( int index ) {
    	Room room = list.get( index );
    	synchronized( room ){
    		System.out.println( "b2" );
    	}
    }
    
    
    public void fun1( int i, long delay ){
    	long cur = System.currentTimeMillis();
    	while( delay > cur ){
    		cur = System.currentTimeMillis();
    	}
    	System.out.println( "----" + i );
    }
 
    public void run() {
//        this.m1();
    	fun1( 1, System.currentTimeMillis() + 3000 );
    }
    

    public static void main  (String[] args) throws Exception {
    	System.out.println( "------0" );
    	Timer timer = new Timer( 20 );
    	byte[] b = new byte[1];
    	b[0] = 1;
    	Runner run = ( o1 ) ->{
    		b[0] = 0;
    		System.out.println( "===============================" + b[0] );
    	};
    	timer.start();
    	
    	timer.add( run, 2000 );
    	while ( true ) {
			System.out.println( b[0] );
    		if( b[0] != 1 ){
//    			break;
    		}
    		Thread.yield();
    		Thread.sleep( 500 );
		}
    	
//    	System.out.println( "complete" );
    }
}