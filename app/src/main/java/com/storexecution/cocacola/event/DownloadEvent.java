package com.storexecution.cocacola.event;
public class DownloadEvent {

    String progress;
    boolean indeterminate;
    String msg;

    public DownloadEvent(String progress,  String msg) {
        this.progress = progress;

        this.msg = msg;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public boolean isIndeterminate() {
        return indeterminate;
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
