package com.cmcc.vrp.queue.task;

/**
 * 抽象的任务执行类
 * <p>
 * Created by sunyiwei on 2016/4/8.
 */
public abstract class Worker {
    private String taskString;

    public String getTaskString() {
        return taskString;
    }

    public void setTaskString(String taskString) {
        this.taskString = taskString;
    }

    /**
     * 执行操作
     */
    public abstract void exec();
}
