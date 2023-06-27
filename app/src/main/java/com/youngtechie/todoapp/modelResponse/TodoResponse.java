package com.youngtechie.todoapp.modelResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TodoResponse {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("finished")
    @Expose
    private Boolean finished;

    public TodoResponse(String id, String title, String description, Boolean finished) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.finished = finished;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }




}
