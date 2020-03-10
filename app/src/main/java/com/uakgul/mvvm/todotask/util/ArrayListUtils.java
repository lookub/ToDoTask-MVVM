package com.uakgul.mvvm.todotask.util;

import com.uakgul.mvvm.todotask.repository.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArrayListUtils {

    /**
     *
     * @param taskListToSort
     * @param ascOrDesc "A" or ASC - "D" for DESC
     * @return
     */
   public static ArrayList<Task> sortTaskTitle(ArrayList<Task> taskListToSort, final String ascOrDesc){
        Collections.sort( taskListToSort, new Comparator<Task>() {
            @Override
            public int compare(Task object1, Task object2) {
                if( ascOrDesc.equals( "A" ) ){
                    return object1.getTitle().compareTo( object2.getTitle() );
                }else{
                    return object2.getTitle().compareTo( object1.getTitle() );
                }
            }
        });
        return taskListToSort;
    }
    /**
     *
     * @param taskListToSort
     * @param ascOrDesc "A" or ASC - "D" for DESC
     * @return
     */
    public static ArrayList<Task> sortTaskCreateDate(ArrayList<Task> taskListToSort, final String ascOrDesc){
        Collections.sort( taskListToSort, new Comparator<Task>() {
            @Override
            public int compare(Task object1, Task object2) {
                if( ascOrDesc.equals( "A" ) ){
                    return object1.getCreateDate().compareTo( object2.getCreateDate() );
                }else{
                    return object2.getCreateDate().compareTo( object1.getCreateDate() );
                }
            }
        });
        return taskListToSort;
    }
    /**
     *
     * @param taskListToSort
     * @param ascOrDesc "A" or ASC - "D" for DESC
     * @return
     */
   public static ArrayList<Task> sortTaskDueDate(ArrayList<Task> taskListToSort, final String ascOrDesc){
        Collections.sort( taskListToSort, new Comparator<Task>() {
            @Override
            public int compare(Task object1, Task object2) {
                if( ascOrDesc.equals( "A" ) ){
                    return object1.getDueDate().compareTo( object2.getDueDate() );
                }else{
                    return object2.getDueDate().compareTo( object1.getDueDate() );
                }
            }
        });
        return taskListToSort;
    }
    /**
     *
     * @param taskListToSort
     * @param ascOrDesc "A" or ASC - "D" for DESC
     * @return
     */
   public static ArrayList<Task> sortTaskIsDone(ArrayList<Task> taskListToSort, final String ascOrDesc){
        Collections.sort( taskListToSort, new Comparator<Task>() {
            @Override
            public int compare(Task object1, Task object2) {
                if( ascOrDesc.equals( "A" ) ){
                    return String.valueOf( object1.getIsDone() ).compareTo( String.valueOf( object2.getIsDone() ) );
                }else{
                    return String.valueOf( object2.getIsDone() ).compareTo( String.valueOf( object1.getIsDone() ) );
                }
            }
        });
        return taskListToSort;
    }


}
