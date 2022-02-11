package com.storexecution.cocacola.event;

/**
 * Created by Koceila on 15/05/2017.
 */

public class DownloadFinishedEvent {

    boolean noError;

    public DownloadFinishedEvent(boolean noError) {
        this.noError = noError;
    }

    public boolean isNoError() {
        return noError;
    }

    public void setNoError(boolean noError) {
        this.noError = noError;
    }
}
