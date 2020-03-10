package com.uakgul.mvvm.todotask.repository.model;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//@Entity(tableName = "Task", foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId") )
@Entity(tableName = "Task")
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "userId")
    private int userId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "dueDate")
    private String dueDate;

    @ColumnInfo(name = "dueTime")
    private String dueTime;

    @ColumnInfo(name = "dueDateTime")
    private String dueDateTime;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "color")
    private String color;

    @ColumnInfo(name = "isFav")
    private int isFav;

    @ColumnInfo(name = "isDone")
    private int isDone;

    @ColumnInfo(name = "createDate")
    private String createDate;

    @ColumnInfo(name = "createTime")
    private String createTime;

    @ColumnInfo(name = "createDateTime")
    private String createDateTime;

    public Task() {
    }

    @Ignore
    public Task(int id, int userId, String title, String description, String dueDate, String dueTime, String dueDateTime, String category, String color, int isFav, int isDone, String createDate, String createTime, String createDateTime) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.dueDateTime = dueDateTime;
        this.category = category;
        this.color = color;
        this.isFav = isFav;
        this.isDone = isDone;
        this.createDate = createDate;
        this.createTime = createTime;
        this.createDateTime = createDateTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", dueTime='" + dueTime + '\'' +
                ", dueDateTime='" + dueDateTime + '\'' +
                ", category='" + category + '\'' +
                ", color='" + color + '\'' +
                ", isFav=" + isFav +
                ", isDone=" + isDone +
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getDueDateTime() {
        return dueDateTime;
    }

    public void setDueDateTime(String dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
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
