package org.praveen.practice;

import org.praveen.annoatation.Column;
import org.praveen.annoatation.Entity;
import org.praveen.annoatation.PrimaryKey;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Hibernate<T> {

    private Connection con;
    private AtomicLong id = new AtomicLong(0L);

    public static <T> Hibernate<T> getConnection() throws SQLException {
        return new Hibernate<T>();
    }

    private Hibernate() throws SQLException {
        this.con = DriverManager.getConnection("jdbc:h2:/Users/praveenkumarthangaraj/Documents/JavaCourse/SampleORMFramework/database/practise1","sa","");
    }

    public void write(T t) throws Exception{
        Class<?> tClass = t.getClass();
        Entity tClassAnnotation = tClass.getAnnotation(Entity.class);
        AtomicReference<Field> pKey = new AtomicReference<>();
        // table name retrieval here
        String tableName = tClassAnnotation.tableName();
        Field[] declaredFields = tClass.getDeclaredFields();
        // column value mapping done here
        Map<String,Field> columnValueMapping = new HashMap<>();
        Arrays.stream(declaredFields)
                .forEach(field -> {
                    field.setAccessible(true);
                    if(field.isAnnotationPresent(PrimaryKey.class)) {
                        pKey.set(field);
                    }else{
                        columnValueMapping.put(field.getName(),field);
                    }
                });
        // getting all the column names
        String columnNames = columnValueMapping.keySet()
                .stream()
                .reduce((s, s2) -> s.concat(",")+s2)
                .orElse("");
        // getting all the column values
        int totalLength = columnValueMapping.values().size()+1;
        String concatValues = IntStream.range(0, totalLength)
                .mapToObj(value -> "?")
                .collect(Collectors.joining(","));

        Field pk = pKey.get();
        String sql = "insert into "+tableName+"("+pk.getName()+","+columnNames+") values ("+concatValues+")";
        System.out.println(sql);
        System.out.println("********** SQL prepared successfully ******************");
        PreparedStatement stmt = con.prepareStatement(sql);
        if (long.class.equals(pk.getType())) {
            stmt.setLong(1, id.incrementAndGet());
        }

        int index = 2;
        for(Field field:columnValueMapping.values()){
            field.setAccessible(true);
            if (int.class.equals(field.getType())) {
                stmt.setInt(index++, (int) field.get(t));
            } else if (String.class.equals(field.getType())) {
                stmt.setString(index++, (String) field.get(t));
            } else if (double.class.equals(field.getType())) {
                stmt.setDouble(index++, (double) field.get(t));
            } else if (float.class.equals(field.getType())) {
                stmt.setFloat(index++, (float) field.get(t));
            } else if (long.class.equals(field.getType())) {
                stmt.setLong(index++, (long) field.get(t));
            } else {
                stmt.setShort(index++, (short) field.get(t));
            }
        }

        stmt.executeUpdate();
    }

    public T read(Class<T> transactionHistoryClass, long l) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Entity entity = transactionHistoryClass.getAnnotation(Entity.class);
        Field[] declaredFields = transactionHistoryClass.getDeclaredFields();
        AtomicReference<Field> pKey = new AtomicReference<>();
        Arrays.stream(declaredFields)
                .forEach(field -> {
                    field.setAccessible(true);
                    if(field.isAnnotationPresent(PrimaryKey.class))
                        pKey.set(field);
                });

        String sql = "select * from "+entity.tableName()+" where "+pKey.get().getName()+" ="+l;
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        T instance = transactionHistoryClass.getConstructor().newInstance();
        Field primaryKeyField = pKey.get();
        long id = resultSet.getInt(primaryKeyField.getName());
        primaryKeyField.set(instance,id);

        for(Field field:declaredFields){
            if(field.isAnnotationPresent(Column.class)){
                field.setAccessible(true);
                if(field.getType() == int.class){
                    field.set(instance,resultSet.getInt(field.getName()));
                }else if(field.getType() == String.class){
                    field.set(instance,resultSet.getString(field.getName()));
                }else if(field.getType() == long.class){
                    field.set(instance,resultSet.getLong(field.getName()));
                }else{
                    throw new IllegalArgumentException("The Argument is unknown");
                }
            }
        }
        return instance;
    }
}
