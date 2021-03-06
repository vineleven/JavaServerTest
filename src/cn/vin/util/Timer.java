package cn.vin.util;

import java.util.Iterator;
import java.util.LinkedList;



public class Timer extends Thread {
	public static final long MIN_INTERVAL = 1000 / 50;
	private int _nextId = 0;
	public LinkedList< RunnerExecutor > executorList = new LinkedList< RunnerExecutor >();
	
	private boolean bRunning = true;
	private long interval = 0;
	
	
	public Timer( long interval ) {
		this.interval = interval < MIN_INTERVAL ? MIN_INTERVAL : interval;
	}
	
	
	public void stopRun(){
		bRunning = false;
	}
	
	
	public int add( Runner runner, long delayTime ){
		return add( runner, delayTime, null );
	}
	
	
	public int add( Runner runner, long delayTime, Object obj ){
		synchronized( executorList ){
			executorList.add( new RunnerExecutor( _nextId++, runner, delayTime + System.currentTimeMillis(), obj ) );
			return _nextId;
		}
	}
	
	
	public void remove( int id ){
		synchronized( executorList ){
			RunnerExecutor item;
			for ( Iterator<RunnerExecutor> iterator = executorList.iterator(); iterator.hasNext(); ) {
				item = ( RunnerExecutor ) iterator.next();
				if( item.id == id ){
					iterator.remove();
					break;
				}
			}
		}
	}
	
	
	@Override
	public void run() {
		while ( bRunning ) {
			long startTime = System.currentTimeMillis();
			synchronized( executorList ){
				RunnerExecutor item;
				for ( Iterator<RunnerExecutor> iterator = executorList.iterator(); iterator.hasNext(); ) {
					item = ( RunnerExecutor ) iterator.next();
					
					if( item.execut( startTime ) ){
						iterator.remove();
					}
				}
			}
			long endTime = System.currentTimeMillis();
			
			long dt = interval - ( endTime - startTime );

			dt = dt <= 0 ? 0 : dt;

			try {
				Thread.sleep( dt );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public class RunnerExecutor {
		public int id = 0;
		private Runner runner = null;
		private long executTime;
		private Object obj = null;
		
		
		public RunnerExecutor( int id, Runner runner, long executTime, Object obj ) {
			this.id = id;
			this.runner = runner;
			this.executTime = executTime;
			this.obj = obj;
		}
		
		
		public boolean execut( long curTime ){
			if( executTime <= curTime ){
				runner.run( obj );
				return true;
			}
			
			return false;
		}
	}
	
	
	public interface Runner {
		public void run( Object obj );
	}
}
