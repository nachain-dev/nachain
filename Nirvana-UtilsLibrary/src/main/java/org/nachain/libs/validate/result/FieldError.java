package org.nachain.libs.validate.result;

public class FieldError {

    private String Field;

    private Object Value;

    private String Message;

    public String getField() {
        return Field;
    }

    public void setField(String field) {
        Field = field;
    }

    public Object getValue() {
        return Value;
    }

    public void setValue(Object value) {
        Value = value;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

}