package com.youngtechie.todoapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.youngtechie.todoapp.adapters.FinishedTodoListAdapter;
import com.youngtechie.todoapp.adapters.TodoListAdapter;
import com.youngtechie.todoapp.interfaces.RecyclerViewClickListener;
import com.youngtechie.todoapp.modelResponse.TodoResponse;
import com.youngtechie.todoapp.networkClient.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinishedTaskFragment extends Fragment  implements RecyclerViewClickListener {
    RecyclerView recyclerView;
    TextView emptyTextview;
    ProgressBar todoProgressBar;
    ArrayList<TodoResponse> todoArrayList;
    FinishedTodoListAdapter todoListAdapter;
    ImageView editIcon , doneIcon;
    public FinishedTaskFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finished_task, container, false);
        recyclerView = view.findViewById(R.id.completed_task_recycler_view);
        emptyTextview = view.findViewById(R.id.empty_list_text);
        todoProgressBar = view.findViewById(R.id.finished_task_progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        getTodoTask();
        return view;
    }

    private void getTodoTask(){
        todoArrayList = new ArrayList<>();
        todoProgressBar.setVisibility(View.VISIBLE);
        Call<JsonObject> call = RetrofitService.getInstance(getContext()).getApi().fetchFinishedTodoTask(true);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                todoProgressBar.setVisibility(View.INVISIBLE);
                assert response.body() != null;
                Gson gson = new Gson();
                JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                boolean success = false;

                try {
                    success = jsonObject.get("success").getAsBoolean();
                    if (success) {
                        Toast.makeText(getActivity(), jsonObject.get("msg").getAsString(), Toast.LENGTH_LONG).show();
                        JsonArray jsonArray = jsonObject.getAsJsonArray("todo");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject element = jsonArray.get(i).getAsJsonObject();
                            // Deserialize the JSON object to TodoResponse
                            TodoResponse todoResp = gson.fromJson(element.toString(), TodoResponse.class);
                            Log.d("TAG", todoResp.getTitle());
                            // Add the TodoResponse object to your ArrayList
                            todoArrayList.add(todoResp);
                        }
                        todoListAdapter = new FinishedTodoListAdapter(getContext(), todoArrayList, FinishedTaskFragment.this);
                        recyclerView.setAdapter(todoListAdapter);
                    }
                } catch (JsonParseException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                todoProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void onDeleteButtonClick(int position) {
        showConfirmDialogAndDelete(position, todoArrayList.get(position).getId());
    }

    @Override
    public void onDoneButtonClick(int position) {

    }

    @Override
    public void onEditButtonClick(int position) {

    }

    private void showConfirmDialogAndDelete(int position, String id) {

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Are you want to delete the task?").setPositiveButton("Yes", null)
                .setNegativeButton("No", null).create();
        alertDialog.setOnShowListener((dialog) -> {

            Button yesButton = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
            yesButton.setOnClickListener((v) -> {
                Gson gson = new Gson();
                Call<JsonObject> call = RetrofitService.getInstance(getContext()).getApi().deleteTodoTask(todoArrayList.get(position).getId());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
                        Toast.makeText(getActivity(), jsonObject.get("msg").getAsString(), Toast.LENGTH_LONG).show();
                        getTodoTask();
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        todoProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                    }
                });
            });

        });

        alertDialog.show();
    }
}