package ru.job4j.chat.dto;

import java.util.Date;

public class ExceptionResponseDto {

    private String message;
    private Date time;

    public ExceptionResponseDto(String aMsg, Date aTime) {
        message = aMsg;
        time = aTime;
    }

    public ExceptionResponseDto(Exception source) {
        message = source.getMessage();
        time = new Date();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
