package com.globant.eventscorelib.baseExceptions;

/**
 * Created by ariel.cattaneo on 6/30/2015.
 */
public class CheckinException extends BaseException {
    public final static int SUBSCRIBER_NOT_SUBSCRIBED = 1;
    public final static int SUBSCRIBER_NOT_ACCEPTED = 2;

    protected int mExceptionCode;

    public CheckinException(int exceptionCode) {
        this.mExceptionCode = exceptionCode;
    }

    public int getExceptionCode() {
        return mExceptionCode;
    }

}
