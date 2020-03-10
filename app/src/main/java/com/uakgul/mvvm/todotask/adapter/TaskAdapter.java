package com.uakgul.mvvm.todotask.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.uakgul.mvvm.todotask.R;
import com.uakgul.mvvm.todotask.repository.db.AppDatabase;
import com.uakgul.mvvm.todotask.repository.model.Task;
import com.uakgul.mvvm.todotask.repository.model.User;
import com.uakgul.mvvm.todotask.util.DateUtils;
import com.uakgul.mvvm.todotask.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> implements Filterable {

    private static String TAG = "Adptr";

    private Context context;

    private  AppDatabase appDatabase;

    private Utils utils;

    private List<Task> tasks;
    private List<Task> filteredTasks;

    private User currentUser;

    public TaskAdapter(Context context, List<Task> tasks, int userId ) {
        this.context = context;
        this.tasks = tasks;
        this.filteredTasks = tasks;
        appDatabase = AppDatabase.getAppDatabase( context );
        utils    = new Utils(  this.context );
        currentUser = appDatabase.daoUser().getUserFromId( userId );
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.item_task, viewGroup, false);
        return new TaskViewHolder( view );
    }

    @Override
    public void onBindViewHolder(TaskViewHolder taskViewHolder, int i) {
        taskViewHolder.bind( filteredTasks.get(i));
    }

    @Override
    public int getItemCount()  {
        return filteredTasks.size();
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Task task;

        TextView tvTitle;
        TextView tvDesc;
        TextView tvDates;

        CardView cvColor;

        ImageView imDone;
        ImageView imDelete;
        ImageView imFav;


        TaskViewHolder(View itemView) {
            super(itemView);

            tvTitle  = itemView.findViewById(R.id.item_task_title);
            tvDesc   = itemView.findViewById(R.id.item_task_desc);
            tvDates  = itemView.findViewById(R.id.item_task_dates);

            cvColor  = itemView.findViewById(R.id.cv_item_task_color);

            imDone   = itemView.findViewById(R.id.item_task_done);
            imDelete = itemView.findViewById(R.id.item_task_delete);
            imFav    = itemView.findViewById(R.id.item_task_fav);

            imDone.setOnClickListener(this);
            imDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( task.getIsDone() == 0){
                        task.setIsDone( 1 );
                        imDone.setImageResource( R.drawable.done_true );
                    }else{
                        task.setIsDone( 0 );
                        imDone.setImageResource( R.drawable.done_false );
                    }
                    appDatabase.daoTask().updateTask( task );
                    notifyDataSetChanged();
                }
            });

            imDelete.setOnClickListener(this);
            imDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filteredTasks.remove( task );
                    appDatabase.daoTask().deleteTaskFromId( task.getId() );
                    notifyDataSetChanged();

                    currentUser.setTasks( currentUser.getTasks() - 1 );
                    if( task.getIsDone() == 1 ){
                        currentUser.setDoneTasks( currentUser.getDoneTasks() - 1 );
                    }else{
                        currentUser.setPendingTasks( currentUser.getPendingTasks() - 1 );
                    }
                    appDatabase.daoUser().updateUser( currentUser );
                }
            });

            imFav.setOnClickListener(this);
            imFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( task.getIsFav() == 0){
                        imFav.setImageResource( R.drawable.favorite_true );
                        task.setIsFav( 1 );
                    }else{
                        imFav.setImageResource( R.drawable.favorite_false );
                        task.setIsFav( 0 );
                    }
                    appDatabase.daoTask().updateTask( task );
                    notifyDataSetChanged();
                }
            });

            cvColor.setOnClickListener(this);
            cvColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String colorRandom = getRandomColorHEX();
                    cvColor.setCardBackgroundColor( Color.parseColor( colorRandom ) ); // "#636161"
                    task.setColor( String.valueOf( colorRandom ) );
                    appDatabase.daoTask().updateTask( task );
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    int pos = getLayoutPosition();
                    showNewTaskUpdateDialog( task );
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
//                    int pos = getLayoutPosition();
                    showNewTaskUpdateDialog( task );
                    return true;
                }
            });


        }//end of TaskViewHolder



        void bind(Task task) {

            this.task = task;

            String title = "Title : " + task.getTitle() + " ( " + task.getCreateDate() + " )";
            tvTitle.setText( title );
            String desc = "Description : " + task.getDescription();
            tvDesc.setText( desc );

            String dates = "DueDate : " + task.getDueDateTime();
            tvDates.setText( dates );

            boolean expire = DateUtils.isExpireDate( task.getDueDate() );

            if( expire ){
                tvDates.setTextColor( context.getResources().getColor( R.color.red800 ) );
                tvDates.setPaintFlags( tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
            }else{
                tvDates.setTextColor( context.getResources().getColor( R.color.blackBG ) );
                tvDates.setPaintFlags( 0 );
            }

            cvColor.setCardBackgroundColor( Color.parseColor( task.getColor() ) ); // "#636161"

            int isDone = task.getIsDone();
            if( isDone == 0 ){
                imDone.setImageResource(R.drawable.done_false);
                tvTitle.setPaintFlags( 0 );
            }else{
                imDone.setImageResource(R.drawable.done_true);
                tvTitle.setPaintFlags( tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
            }

            int isFav = task.getIsFav();
            if( isFav == 0 ){
                imFav.setImageResource(R.drawable.favorite_false);
            }else{
                imFav.setImageResource(R.drawable.favorite_true);
            }


        }//end of bind


        @Override
        public void onClick(View view) {
            Log.i(TAG, "TaskAdapter.ViewHolder.onClick().view id : " + view.getId()  +  "  tag " + view.getTag() );
        }

    }//end of ViewHolder


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {


                String charString = charSequence.toString();
                Log.i(TAG, "TaskAdapter.FilterResults : charString : " + charString );

                if (charString.isEmpty()) {
                    filteredTasks = tasks;
                } else {
                    filteredTasks = getFilteredTask( charString );
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredTasks;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredTasks = (ArrayList<Task>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }//end of getFilter


    private List<Task> getFilteredTask(String filterString) {

        Log.i(TAG, "TaskAdapter.getFilteredTask : filterString : " + filterString );

        List<Task> filteredList = new ArrayList<>();
        if( filterString.equalsIgnoreCase( "ALL" ) ){
            filteredList = tasks;

        }else if( filterString.equalsIgnoreCase( "DONE" ) ){
            for (Task row : tasks) {
                if ( row.getIsDone() == 1 ) {
                    filteredList.add( row );
                }
            }
        }else if( filterString.equalsIgnoreCase( "PENDING" ) ){
            for (Task row : tasks) {
                if ( row.getIsDone() == 0 ) {
                    filteredList.add( row );
                }
            }

        }else if( filterString.equalsIgnoreCase( "EXPIRE" ) ){
            for (Task row : tasks) {
                if( DateUtils.isExpireDate( row.getDueDate() ) ){
                    filteredList.add( row );
                }
            }

        }else if( filterString.equalsIgnoreCase( "NONEXPIRE" ) ){
            for (Task row : tasks) {
                if( !DateUtils.isExpireDate( row.getDueDate() ) ){
                    filteredList.add( row );
                }
            }

        }else if( filterString.equalsIgnoreCase( "FAV" ) ){
            for (Task row : tasks) {
                if ( row.getIsFav() == 1 ) {
                    filteredList.add( row );
                }
            }

        // for searchview text
        }else{
            for (Task row : tasks) {
                // name match condition. this might differ depending on your requirement
                // here we are looking for name or phone number match
                if ( row.getTitle().toLowerCase().contains( filterString.toLowerCase() ) ||
                        row.getDescription().toLowerCase().contains( filterString.toLowerCase() ) ) {
                    filteredList.add( row );
                }
            }
        }
        Log.i(TAG, "TaskAdapter.getFilteredTask : filteredList : " + filteredList.size() );
        return filteredList;
    }//end of getFilteredTask



    public void showNewTaskUpdateDialog(final Task task){

        final AlertDialog taskUpdateDialog;
        AlertDialog.Builder taskUpdateDialogBuilder = new AlertDialog.Builder( context );
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View mView = inflater.inflate(R.layout.dialog_new_task, null);

        final EditText etTitle;
        final EditText etDescr;
        final TextView tvDueDate;
        final CheckBox cbFav;
        final CheckBox cbDone;
        final TextView tvColor;

        final Button btnSave;
        final Button btnCancel;
        final Button btDate;

        etTitle   = mView.findViewById(R.id.etTitle);
        etDescr   = mView.findViewById( R.id.etDescription);
        tvDueDate = mView.findViewById(R.id.tvDueDate);
        tvColor   = mView.findViewById(R.id.tvColorPicker);
        cbFav     = mView.findViewById( R.id.cbFav);
        cbDone    = mView.findViewById( R.id.cbDone);

        btnSave   = mView.findViewById(R.id.btnSave);
        btnCancel = mView.findViewById(R.id.btnCancel);
        btDate = mView.findViewById(R.id.btDate);

        taskUpdateDialogBuilder.setView(mView);
        taskUpdateDialog = taskUpdateDialogBuilder.create();
        taskUpdateDialog.getWindow().setWindowAnimations( R.style.DialogAnimation_UpBottom );
        taskUpdateDialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        taskUpdateDialog.setCanceledOnTouchOutside(false);
        taskUpdateDialog.show();

        taskUpdateDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    taskUpdateDialog.dismiss();
                }
                return true;
            }
        });


        tvColor.setText( task.getColor() );
        tvColor.setTextColor( Color.parseColor( task.getColor() ) );

        etTitle.setText(  task.getTitle() );
        etDescr.setText(  task.getDescription() );
        tvDueDate.setText(  task.getDueDate() );

        if( task.getIsFav() == 1 ) cbFav.setChecked( true );

        if( task.getIsDone() == 1 ) cbDone.setChecked( true );

        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year        = calendar.get(Calendar.YEAR);
                int month       = calendar.get(Calendar.MONTH);
                int dayOfMonth  = calendar.get(Calendar.DAY_OF_MONTH);

                int hour        = calendar.get(Calendar.HOUR_OF_DAY);
                int min         = calendar.get(Calendar.MINUTE);
                int sec         = calendar.get(Calendar.SECOND);

                final String time =  hour + ":" + min + ":" + sec;

                DatePickerDialog datePickerDialog = new DatePickerDialog( context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                String result = year + "-" + (month + 1) + "-" + day;

                                if( day < 10 ) result = year + "-" + (month + 1) + "-0" + day;

                                tvDueDate.setText( result );
                                task.setDueDate( result );
                                task.setDueTime( time );
                                task.setDueDateTime( result + " " + time );

                            }
                        }, year, month, dayOfMonth);

                datePickerDialog.getDatePicker().setMinDate( DateUtils.getFewDaysAgoMilisecond(20 ) );
                datePickerDialog.getDatePicker().setMaxDate( DateUtils.getFewDaysAgoMilisecond(30 ) );
                datePickerDialog.show();

            }
        });

        cbFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( task.getIsFav() == 0 ){
                    task.setIsFav(1);
                }else{
                    task.setIsFav(0);
                }
            }
        });

        cbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( task.getIsDone() == 0 ){
                    task.setIsDone(1);
                }else{
                    task.setIsDone(0);
                }
            }
        });



        tvColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String colorRandom = getRandomColorHEX();
                tvColor.setText( String.valueOf( colorRandom ) );
                tvColor.setTextColor( Color.parseColor( colorRandom ) );
                task.setColor( String.valueOf( colorRandom ) );
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskUpdateDialog.dismiss();
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( etTitle.getText().toString().isEmpty() || etDescr.getText().toString().isEmpty() ){
                    utils.customToastMessage("Please enter required area : Title and Description!" , "Fail", "S");
                }else{

                    task.setTitle( etTitle.getText().toString() );
                    task.setDescription( etDescr.getText().toString() );

                    appDatabase.daoTask().updateTask( task );
                    notifyDataSetChanged();

                    if( task.getIsDone() == 1 ){
                        currentUser.setDoneTasks( currentUser.getDoneTasks() - 1 );
                    }else{
                        currentUser.setPendingTasks( currentUser.getPendingTasks() - 1 );
                    }
                    appDatabase.daoUser().updateUser( currentUser );

                    taskUpdateDialog.dismiss();
                }

            }
        });



    }//end of showNewTaskDialog




    public void appendTask(Task taskToAppend) {
        this.filteredTasks.add( taskToAppend );
        notifyDataSetChanged();
    }


    public void clearTasks() {
        filteredTasks.clear();
        notifyDataSetChanged();
    }


    public String getRandomColorHEX(){
        Random obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);
        String color = String.format("#%06x", rand_num);;
        return color;
    }


}
