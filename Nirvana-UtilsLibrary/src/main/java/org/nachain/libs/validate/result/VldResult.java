package org.nachain.libs.validate.result;

import org.nachain.libs.util.MapUtil;

import java.util.LinkedHashMap;
import java.util.List;

public class VldResult {


    private LinkedHashMap<String, FieldError> errorMap = new LinkedHashMap<String, FieldError>();

    public VldResult() {
        super();
    }


    public void addError(FieldError fieldError) {
        errorMap.put(fieldError.getField(), fieldError);
    }


    public void addError(String valueName, String message) {
        FieldError fieldError = new FieldError();
        fieldError.setField(valueName);
        fieldError.setValue(null);
        fieldError.setMessage(message);
        errorMap.put(fieldError.getField(), fieldError);
    }


    public void addError(String valueName, Object value, String message) {
        FieldError fieldError = new FieldError();
        fieldError.setField(valueName);
        fieldError.setValue(value);
        fieldError.setMessage(message);
        errorMap.put(fieldError.getField(), fieldError);
    }


    public boolean isSuccess() {
        return errorMap.size() == 0;
    }


    public boolean hasError() {
        return errorMap.size() > 0;
    }


    public LinkedHashMap<String, FieldError> getErrorMap() {
        return errorMap;
    }


    public FieldError getFieldError(String fieldName) {
        return errorMap.get(fieldName);
    }


    public List<Object> getFieldErrorList() {
        return MapUtil.map2ValueList(errorMap);
    }
}