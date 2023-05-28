package org.nachain.libs.validate;

import lombok.extern.slf4j.Slf4j;
import org.nachain.libs.util.CommUtil;
import org.nachain.libs.validate.result.FieldError;
import org.nachain.libs.validate.result.VldResult;
import org.nachain.libs.validate.type.Length;
import org.nachain.libs.validate.type.NotBlank;
import org.nachain.libs.validate.type.VldType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;


@Slf4j
public class Vld {


    public static <T> VldResult validate(T t) throws IllegalArgumentException, IllegalAccessException {

        Class<? extends Object> clazz = t.getClass();

        Field[] fieldArray = clazz.getDeclaredFields();

        VldResult vldResult = new VldResult();

        for (Field field : fieldArray) {


            Annotation[] anArray = field.getDeclaredAnnotations();
            for (Annotation an : anArray) {

                String typeName = an.annotationType().getSimpleName();


                VldType vldType = VldType.valueOf(typeName);


                field.setAccessible(true);


                Object fieldValue = field.get(t);
                String fieldName = field.getName();
                log.debug("fieldName:" + fieldName + ", value:" + CommUtil.null2String(fieldValue));


                boolean checkVld;

                switch (vldType) {
                    case Length:
                        Length len = (Length) an;

                        checkVld = VldUtil.vldLength(CommUtil.null2String(fieldValue), len);

                        if (!checkVld) {
                            FieldError fieldError = new FieldError();
                            fieldError.setField(fieldName);
                            fieldError.setValue(fieldValue);
                            fieldError.setMessage(len.message());
                            vldResult.addError(fieldError);
                        }
                        break;
                    case NotBlank:
                        NotBlank nb = (NotBlank) an;

                        checkVld = VldUtil.vldNotBlank(CommUtil.null2String(fieldValue));

                        if (!checkVld) {
                            FieldError fieldError = new FieldError();
                            fieldError.setField(fieldName);
                            fieldError.setValue(fieldValue);
                            fieldError.setMessage(nb.message());
                            vldResult.addError(fieldError);
                        }
                        break;
                    case Email:
                        break;
                    default:
                        break;
                }
            }

        }

        return vldResult;
    }
}