package net.jsar.multigdrive.mvc;

import java.util.HashMap;
import java.util.Map;

import net.jsar.multigdrive.model.Account;
import net.jsar.multigdrive.mvc.TaskThread.TaskListener;
import net.jsar.multigdrive.mvc.TaskThread.TaskProcessor;
import net.jsar.multigdrive.util.Log;

public class TaskManager {

	public static long taskId = 0;

	public static long getNextTaskId() { return taskId++; }

	protected Map<String, TaskThread> tasks = new HashMap<String, TaskThread>();

	public void addTask(TaskThread th) {
		if (th != null) tasks.put(th.getTaskId(),th);
	}

	public void removeTask(TaskThread th) {
		if (th != null) tasks.remove(th.getTaskId());				
	}

	public TaskThread createTask(final String baseId, final TaskProcessor processor) {
		//Thread th = new ThreadUpdateAccount(ctx,acc);
		//th.start();	
		TaskThread th = new TaskThread(
				baseId+"_"+TaskManager.getNextTaskId(),
				processor,
				new TaskListener() {
					public void notifyStartTask(TaskThread th)	{	addTask(th);	}
					public void notifyFinishTask(TaskThread th) {	removeTask(th);}
				}
			);
		//addTask(th);
		return th;
	}
	
}
