package net.jsar.multigdrive.mvc;

import net.jsar.multigdrive.model.Account;
import net.jsar.multigdrive.model.Context;
import net.jsar.multigdrive.util.Log;

public class TaskThread extends Thread {
	

	//protected Context ctx;
	protected TaskProcessor taskProcessor;
	protected TaskListener taskListener;
	protected String taskId;
	
	public TaskThread(String taskId, TaskProcessor taskProcessor, TaskListener taskListener) {
		//this.ctx = ctx;
		this.taskId = taskId;
		this.taskProcessor = taskProcessor;
		this.taskListener = taskListener;
		
		if (this.taskProcessor == null) this.taskProcessor = new TaskProcessor() {
			public void onStart() {}			
			public boolean onProcess() { return true; }			
			public void onEnd() {}
		};
		if (this.taskListener == null) this.taskListener = new TaskListener() {			
			public void notifyStartTask(TaskThread th) {}
			public void notifyFinishTask(TaskThread th) {}
		};
	}
	/*
	public TaskProcessor getTaskProcessor() {
		return this.taskProcessor;		
	}
	*/
	public String getTaskId() {
		return this.taskId;		
	}		
	
	@Override
	public void run() {
		//boolean success = false;
		try {	taskListener.notifyStartTask(this);	} catch (Exception e) { e.printStackTrace(); }
		try {	taskProcessor.onStart();	} catch (Exception e) { e.printStackTrace(); }
		try {			
			boolean success = taskProcessor.onProcess();
			if (!success) {
				Log.error("Cannot process task '"+this.taskId+"'");				
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Log.error("Fatal error processing task '"+this.taskId+"'");
		}		
		try {	taskProcessor.onEnd();	} catch (Exception e) { e.printStackTrace(); }
		try {	taskListener.notifyFinishTask(this);	} catch (Exception e) { e.printStackTrace(); }
	}

	
	public interface TaskProcessor {
		public void onStart();
		public boolean onProcess();
		public void onEnd();
	}
	
	public interface TaskListener {
		public void notifyStartTask(TaskThread th);		
		public void notifyFinishTask(TaskThread th);
	}
}
