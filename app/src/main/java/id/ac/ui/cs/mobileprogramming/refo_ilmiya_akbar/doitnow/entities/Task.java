package id.ac.ui.cs.mobileprogramming.refo_ilmiya_akbar.doitnow.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task implements Serializable, Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "task")
    private String task;

    @ColumnInfo(name = "desc")
    private String desc;

    @ColumnInfo(name = "finish_by")
    private String finishBy;

    @ColumnInfo(name = "finished")
    private boolean finished;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "userMail")
    private String userMail;

    @ColumnInfo(name = "attachment_file_path")
    private String filePath;

    @ColumnInfo(name = "reminder")
    private boolean reminder;


    protected Task(Parcel in) {
        id = in.readInt();
        task = in.readString();
        desc = in.readString();
        finishBy = in.readString();
        finished = in.readByte() != 0;
        category = in.readString();
        userMail = in.readString();
        filePath = in.readString();
        reminder = in.readByte() != 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFinishBy() {
        return finishBy;
    }

    public void setFinishBy(String finishBy) {
        this.finishBy = finishBy;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(task);
        dest.writeString(desc);
        dest.writeString(finishBy);
        dest.writeByte((byte) (finished ? 1 : 0));
        dest.writeString(category);
        dest.writeString(userMail);
        dest.writeString(filePath);
        dest.writeByte((byte) (reminder ? 1 : 0));
    }
}