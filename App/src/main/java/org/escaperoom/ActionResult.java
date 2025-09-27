package org.escaperoom;
public class ActionResult {
    private final String message;
    private final boolean success;

    public ActionResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}

