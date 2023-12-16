package com.example.spring.fifth.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class EntityUtil {

    @SneakyThrows
    public void copyNotNullFields(Object from, Object to) {
        Class<?> clazz = from.getClass();
        Field[] fromFields = clazz.getDeclaredFields();

        for (Field field : fromFields) {
            field.setAccessible(true);

            Object fieldValue = field.get(from);

            if (fieldValue != null) {
                field.set(to, fieldValue);
            }
        }
    }
}
