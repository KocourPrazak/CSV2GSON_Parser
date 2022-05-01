package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.example.Employee.Employee;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json);
/*        list.forEach(System.out::println);
        System.out.println(json);*/
    }

    public static List parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) { //Create CSVReader obj with file obj

            //Add CSV columns to obj fields mapping
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class); //type class Employee
            strategy.setColumnMapping(columnMapping); //array of class field names ordered by columns numbers (column names)

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader) //CSV parser creates list of objects with field values from CSV
                    .withMappingStrategy(strategy) //CSV to obj fields assignment strategy
                    .build();

            staff = csv.parse(); //let's list begins and CVS's parsers )

        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }

    public static final String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        /*If the object that you are serializing/deserializing is a ParameterizedType
         (i.e. contains at least one type parameter and may be an array) then you must use the toJson(Object, Type)
         or fromJson(String, Type) method.*/
        Type listType = new TypeToken<List<Employee>>() {
        }.getType(); //Constructs a new type literal and gets underlying Type instance
        return gson.toJson(list, listType);
    }

    public static final void writeString(String json) {
        try (FileWriter file = new FileWriter("data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
