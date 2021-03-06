package bruteforce.business;
import java.util.Date;
import java.util.List;

import bruteforce.business.Exceptions.DateException;
import bruteforce.objects.Task;
import bruteforce.persistence.TaskPersistence;
import bruteforce.application.Services;
/**
Class: AccessTask
Author:  Yunpeng Zhong
Purpose: Business layer logic for a user Task
*/

public class AccessTask {

    private List<Task> tasks;
    private TaskPersistence taskPersistence;
    private Task currentTask;

    public AccessTask() {
        taskPersistence = Services.getTaskPersistence();
        tasks = null;
        currentTask = null;
    }

    public AccessTask(String userName) {
        taskPersistence = Services.getTaskPersistence();
        tasks = taskPersistence.getTasks(userName);
        currentTask = null;
    }

    public AccessTask(final TaskPersistence taskPersistence, String userName) {
        this();
        this.taskPersistence = taskPersistence;
        tasks = taskPersistence.getTasks(userName);
        //System.out.println(tasks);
    }

    public AccessTask(final TaskPersistence taskPersistence){
        this();
        this.taskPersistence = taskPersistence;

    }
    /**
     getTaskList

     Purpose: get a list of tasks with current user
     Parameters: None
     Returns: List<Task>
     */
    public List<Task> getTaskList() {
        return tasks;
    }

    /**
     getTask

     Purpose: get a specific task base on accountName and taskID
     Parameters: String accountName, taskID
     Returns: void
     */
    public Task getTask(int taskId) {
        if(tasks != null) {
            Task task;
            for(int current = 0; current < tasks.size(); current++) {
                task = tasks.get(current);
                if(task.getTaskID()==taskId)
                {
                    currentTask = task;
                    return currentTask;
                }
            }
        }
        return null;
    }

    /**
    insertTask

    Purpose: insert a new Task into task list
    Parameters: Task task
    Returns: void
    */
    public void insertTask(Task task) {
        taskPersistence.insertTask(task);
        tasksRenew(task.getUsername());
    }

    /**
    updateTask

    Purpose: update task into database
    Parameters: Task task
    Returns: void
    */
    public void updateTask(){
        if(currentTask!=null) {
            taskPersistence.updateTask(currentTask);
            tasksRenew(currentTask.getUsername());
            currentTask = null;
        }
    }

    /**
    deleteTask

    Purpose: delete task from the task list
    Parameters: Task task
    Returns: void
    */
    public void deleteTask(){
        if(currentTask!=null) {
            taskPersistence.deleteTask(currentTask);
            tasksRenew(currentTask.getName());
            currentTask = null;
        }
    }

    /**
    updateName

    Purpose: update the new name for task
    Parameters: String newName
    Returns: void
    */
    public void updateName(String newName) {
        if(currentTask!=null) {
            currentTask.setName(newName);
        }
    }

    /**
    updateDeadline

    Purpose: update the new Deadline for task
    Parameters: Date newDate
    Returns: void
    */
    public void updateDeadline(Date newDate) {
        if(currentTask!=null) {
            currentTask.setDeadline(newDate);
        }
    }

    /**
    updateComplete

    Purpose: update the new complete for task
    Parameters: boolean newComplete
    Returns: void
    */
    public void updateComplete(boolean newComplete) {
        if(currentTask!=null) {
            currentTask.setCompleted(newComplete);
        }
    }

    /**
    updatePriority

    Purpose: update the new priority for task
    Parameters: boolean newComplete
    Returns: void
    */
    public void updatePriority(int newPriority) {
        if(currentTask!=null) {
            currentTask.setPriority(newPriority);
        }
    }

    /**
    completeTask

    Purpose: when complete a task, set that task to be complete and return the priority for that to calculate the points
    Parameters: none
    Returns: int
    */
    public int completeTask() {
        if(currentTask!=null&&!currentTask.getCompleted()) {
            currentTask.setCompleted(true);
            taskPersistence.updateTask(currentTask);

            return currentTask.getPriority();
        }
        return -1;
    }

    /**
    removeAllTask

    Purpose: when delete user account also delete all user tasks for that user
    Parameters: none
    Returns: none
    */
    public void removeAllTask(){
        if(tasks.size()>0) {
            currentTask = tasks.get(0);
            deleteTask();
        }
    }

    /**
     isExist

     Purpose: check the taskId is exist in this user or not
     Parameters: in taskID
     Returns: boolean
     */
    public boolean isExist(int taskId){
        Task task;
        for(int current = 0; current < tasks.size(); current++) {
            task = tasks.get(current);
            if(task.getTaskID()==taskId)
            {
                return true;
            }
        }
        return false;
    }

    /**
    tasksRenew

    Purpose: renew the current task list for that user
    Parameters: String userName
    Returns: None
    */
    private void tasksRenew(String userName) {
        tasks = taskPersistence.getTasks(userName);
    }
}