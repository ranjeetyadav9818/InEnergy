package com.inenergis.entity.genericEnum;

public enum TaskStatus {

    PAUSED("Paused"),
    CANCELLED("Cancelled"),
    PENDING_ACTION("Pending Action"),
    IN_PROCESS("In Progress"),
    NOTIFIED("Notified"),
    COMPLETED("Completed"),
    ERROR("System failed, waiting to be re-executed");


    private String status;

    TaskStatus(String status) {
        this.status = status;
    }

    boolean isEditableByUser(TaskStatus taskStatus) {
        switch (taskStatus) {
            case IN_PROCESS:
            case PENDING_ACTION:
            case CANCELLED:
                return true;
            default:
                return false;
        }

    }

    public String getText() {
        return status;
    }
    
}
