package com.youngtechie.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.youngtechie.todoapp.adapters.TodoListAdapter;
import com.youngtechie.todoapp.interfaces.RecyclerViewClickListener;
import com.youngtechie.todoapp.modelResponse.ApiResponse;
import com.youngtechie.todoapp.modelResponse.TodoResponse;
import com.youngtechie.todoapp.networkClient.RetrofitService;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements RecyclerViewClickListener {
    FloatingActionButton floatingActionButton;
    ProgressBar dialogProgressBar;
    RecyclerView recyclerView;
    TextView emptyTextview;
    ProgressBar todoProgressBar;
    ArrayList<TodoResponse> todoArrayList;
    TodoListAdapter todoListAdapter;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        floatingActionButton = view.findViewById(R.id.add_task_icon);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(false, null, null, null);
            }
        });
        recyclerView = view.findViewById(R.id.task_list_recycler_view);
        emptyTextview = view.findViewById(R.id.empty_list_text);
        todoProgressBar = view.findViewById(R.id.home_task_list_progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        getTodoTask();
        return view;
    }

    private void getTodoTask() {
        todoArrayList = new ArrayList<>();
        todoProgressBar.setVisibility(View.VISIBLE);
        Call<JsonObject> call = RetrofitService.getInstance(getContext()).getApi().fetchTodoTask();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                todoProgressBar.setVisibility(View.INVISIBLE);
                assert response.body() != null;
//                boolean success = response.body().get("success").getAsBoolean();
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

                        todoListAdapter = new TodoListAdapter(getContext(), todoArrayList, HomeFragment.this);
                        recyclerView.setAdapter(todoListAdapter);
                    }
                } catch (JsonParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                todoProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    public void showAlertDialog(@Nullable Boolean isEditable, @Nullable String id, @Nullable String title, @Nullable String description) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_dialog_layout, null);
        final EditText titleCtr = alertLayout.findViewById(R.id.add_task_title);
        final EditText descCtr = alertLayout.findViewById(R.id.add_task_des);
        if (Boolean.TRUE.equals(isEditable)) {
            titleCtr.setText(title);
            descCtr.setText(description);
        }
        dialogProgressBar = alertLayout.findViewById(R.id.dialog_progress_bar);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(alertLayout).setTitle("Add Task").
                setPositiveButton("Add", null).setNegativeButton("Cancel", null).create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Positive Button", Toast.LENGTH_LONG).show();
                        String title = titleCtr.getText().toString();
                        String dec = descCtr.getText().toString();
                        dialogProgressBar.setVisibility(View.VISIBLE);
                        HashMap<String, String> body = new HashMap<>();
                        body.put("title", title);
                        body.put("description", dec);

                        if (Boolean.TRUE.equals(isEditable)) {
                            Call<JsonObject> call = RetrofitService.getInstance(getContext()).getApi().updateTodoTask(id, body);
                            call.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    Gson gson = new Gson();
                                    JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                                    Toast.makeText(getActivity(), jsonObject.get("msg").getAsString(), Toast.LENGTH_LONG).show();
                                    getTodoTask();
                                    dialog.dismiss();
                                    dialogProgressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    dialog.dismiss();
                                    dialogProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {
                            if (!TextUtils.isEmpty(title)) {
                                Call<ApiResponse> call = RetrofitService.getInstance(getContext()).getApi().addTodoTask(body);
                                call.enqueue(new Callback<ApiResponse>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                                        dialog.dismiss();
                                        dialogProgressBar.setVisibility(View.GONE);
                                        ApiResponse resp = response.body();
                                        if (response.isSuccessful()) {
                                            Toast.makeText(getActivity(), resp.getMsg(), Toast.LENGTH_LONG).show();
                                            getTodoTask();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                                        dialog.dismiss();
                                        dialogProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getActivity(), "Please enter a title", Toast.LENGTH_LONG).show();
                            }

                        }


                    }
                });
            }
        });
        dialog.show();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {
        showAlertDialog(true, todoArrayList.get(position).getId(), todoArrayList.get(position).getTitle(), todoArrayList.get(position).getDescription());

    }

    @Override
    public void onDeleteButtonClick(int position) {
        showConfirmDialogAndDelete(position, todoArrayList.get(position).getId());
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


    @Override
    public void onDoneButtonClick(int position) {
        Gson gson = new Gson();
        Call<JsonObject> call = RetrofitService.getInstance(getContext()).getApi().taskDone(todoArrayList.get(position).getId(), true);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
                Toast.makeText(getActivity(), jsonObject.get("msg").getAsString(), Toast.LENGTH_LONG).show();
                getTodoTask();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onEditButtonClick(int position) {
        showAlertDialog(true, todoArrayList.get(position).getId(), todoArrayList.get(position).getTitle(), todoArrayList.get(position).getDescription());
    }


}