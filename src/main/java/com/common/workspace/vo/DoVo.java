package com.common.workspace.vo;

import java.util.Date;

/**
 * @author: football98
 * @createTime: 16-10-13
 * @classDescription:vo
 */
public class DoVo {
    private String id ;
    private String execution_id ;
    private String proc_inst_id ;
    private String name ;
    private Date create_time;
    private String user_id;
    private String task_def_key;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExecution_id() {
        return execution_id;
    }

    public void setExecution_id(String execution_id) {
        this.execution_id = execution_id;
    }

    public String getProc_inst_id() {
        return proc_inst_id;
    }

    public void setProc_inst_id(String proc_inst_id) {
        this.proc_inst_id = proc_inst_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTask_def_key() {
        return task_def_key;
    }

    public void setTask_def_key(String task_def_key) {
        this.task_def_key = task_def_key;
    }
}
