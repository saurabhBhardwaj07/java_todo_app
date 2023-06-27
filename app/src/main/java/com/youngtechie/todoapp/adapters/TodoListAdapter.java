package com.youngtechie.todoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.youngtechie.todoapp.R;
import com.youngtechie.todoapp.interfaces.RecyclerViewClickListener;
import com.youngtechie.todoapp.modelResponse.TodoResponse;

import java.util.ArrayList;
import java.util.Random;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder> {

    ArrayList<TodoResponse> listOfTodo;
    Context context;
    final private RecyclerViewClickListener clickListener;

    public TodoListAdapter(Context context, ArrayList<TodoResponse> listOfTodo, RecyclerViewClickListener clickListener) {
        this.context = context;
        this.listOfTodo = listOfTodo;
        this.clickListener = clickListener;
    }

    public class TodoListViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CardView todoItemCard;
        public TextView description;
        public RelativeLayout todoItemAccordianBody;
        public ImageView arrow, editButton, deleteButton, doneButton;


        public TodoListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.todo_item_title);
            this.description = itemView.findViewById(R.id.todo_item_dec);
            this.todoItemCard = itemView.findViewById(R.id.todo_item_card);
            this.todoItemAccordianBody = itemView.findViewById(R.id.todo_item_accordian_body);
            this.arrow = itemView.findViewById(R.id.todo_item_arrow);
            this.deleteButton = itemView.findViewById(R.id.todo_item_delete_icon);
            this.editButton = itemView.findViewById(R.id.todo_item_edit_icon);
            this.doneButton = itemView.findViewById(R.id.todo_item_done_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickListener.onLongItemClick(getAdapterPosition());
                    return true;
                }
            });
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onEditButtonClick(getAdapterPosition());
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onDeleteButtonClick(getAdapterPosition());
                }
            });
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onDoneButtonClick(getAdapterPosition());
                }
            });

        }


    }

    @NonNull
    @Override
    public TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listItem = inflater.inflate(R.layout.todo_single_item, parent, false);
        int[] androidColors = listItem.getResources().getIntArray(R.array.androidcolors);
        int randomColor = androidColors[new Random().nextInt(androidColors.length)];
        TodoListViewHolder viewHolder = new TodoListViewHolder(listItem);
        viewHolder.todoItemCard.setBackgroundColor(randomColor);

        viewHolder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.todoItemAccordianBody.getVisibility() == View.VISIBLE) {
                    viewHolder.todoItemAccordianBody.setVisibility(View.GONE);
                } else {
                    viewHolder.todoItemAccordianBody.setVisibility(View.VISIBLE);
                }
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListViewHolder holder, int position) {
        final TodoResponse element = listOfTodo.get(position);
        holder.description.setText(element.getDescription());
        holder.title.setText(element.getTitle());
    }

    @Override
    public int getItemCount() {
        return listOfTodo.size();
    }
}
