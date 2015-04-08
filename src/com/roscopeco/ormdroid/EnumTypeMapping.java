package com.roscopeco.ormdroid;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by pr02nl on 16/03/15.
 */
public class EnumTypeMapping implements TypeMapping {

    @Override
    public Class<?> javaType() {
        return Enum.class;
    }

    @Override
    public String sqlType(Class<?> concreteType) {
        return "VARCHAR";
    }

    @Override
    public String encodeValue(SQLiteDatabase db, Object value) {
        if (value == null) {
            return null;
        }
        Enum e = (Enum) value;
        return DatabaseUtils.sqlEscapeString(e.name());
    }

    @Override
    public <T extends Entity> Object decodeValue(SQLiteDatabase db, Field field, Cursor c, int columnIndex, ArrayList<T> precursors) {
        String string = c.getString(columnIndex);
        if (string == null) {
            return null;
        }
        if (!field.getType().isEnum()) {
            throw new IllegalArgumentException("EnumTypeMapping can only be used with Enum subclasses");
        }
        Class<? extends Enum> type = (Class<? extends Enum>) field.getType();
        Enum<?> anEnum = Enum.valueOf(type, string);
        return anEnum;
    }
}
