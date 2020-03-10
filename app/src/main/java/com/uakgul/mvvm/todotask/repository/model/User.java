package com.uakgul.mvvm.todotask.repository.model;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "tasks")
    private int tasks;

    @ColumnInfo(name = "doneTasks")
    private int doneTasks;

    @ColumnInfo(name = "pendingTasks")
    private int pendingTasks;

    @ColumnInfo(name = "createDate")
    private String createDate;

    @ColumnInfo(name = "createTime")
    private String createTime;

    @ColumnInfo(name = "createDateTime")
    private String createDateTime;

    @Ignore
    public User() {
    }

    public User(String username, String password, String name, int tasks, int doneTasks, int pendingTasks, String createDate, String createTime, String createDateTime) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.tasks = tasks;
        this.doneTasks = doneTasks;
        this.pendingTasks = pendingTasks;
        this.createDate = createDate;
        this.createTime = createTime;
        this.createDateTime = createDateTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", tasks=" + tasks +
                ", doneTasks=" + doneTasks +
                ", pendingTasks=" + pendingTasks +
                ", createDate='" + createDate + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createDateTime='" + createDateTime + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTasks() {
        return tasks;
    }

    public void setTasks(int tasks) {
        this.tasks = tasks;
    }

    public int getDoneTasks() {
        return doneTasks;
    }

    public void setDoneTasks(int doneTasks) {
        this.doneTasks = doneTasks;
    }

    public int getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(int pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }
}
