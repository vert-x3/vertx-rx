package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter and mapper for {@link io.vertx.codegen.testmodel.DataObjectWithMaps}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithMaps} original class using Vert.x codegen.
 */
public class DataObjectWithMapsConverter {


  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, DataObjectWithMaps obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "booleanValues":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, java.lang.Boolean> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof Boolean)
                map.put(entry.getKey(), (Boolean)entry.getValue());
            });
            obj.setBooleanValues(map);
          }
          break;
        case "dataObjectValues":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, io.vertx.codegen.testmodel.TestDataObject> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof JsonObject)
                map.put(entry.getKey(), new io.vertx.codegen.testmodel.TestDataObject((JsonObject)entry.getValue()));
            });
            obj.setDataObjectValues(map);
          }
          break;
        case "doubleValues":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, java.lang.Double> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof Number)
                map.put(entry.getKey(), ((Number)entry.getValue()).doubleValue());
            });
            obj.setDoubleValues(map);
          }
          break;
        case "enumValues":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, io.vertx.codegen.testmodel.TestEnum> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof String)
                map.put(entry.getKey(), io.vertx.codegen.testmodel.TestEnum.valueOf((String)entry.getValue()));
            });
            obj.setEnumValues(map);
          }
          break;
        case "floatValues":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, java.lang.Float> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof Number)
                map.put(entry.getKey(), ((Number)entry.getValue()).floatValue());
            });
            obj.setFloatValues(map);
          }
          break;
        case "genEnumValues":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, io.vertx.codegen.testmodel.TestGenEnum> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof String)
                map.put(entry.getKey(), io.vertx.codegen.testmodel.TestGenEnum.valueOf((String)entry.getValue()));
            });
            obj.setGenEnumValues(map);
          }
          break;
        case "instantValues":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, java.time.Instant> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof String)
                map.put(entry.getKey(), Instant.from(DateTimeFormatter.ISO_INSTANT.parse((String)entry.getValue())));
            });
            obj.setInstantValues(map);
          }
          break;
        case "integerValues":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, java.lang.Integer> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof Number)
                map.put(entry.getKey(), ((Number)entry.getValue()).intValue());
            });
            obj.setIntegerValues(map);
          }
          break;
        case "jsonArrayValues":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, io.vertx.core.json.JsonArray> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof JsonArray)
                map.put(entry.getKey(), ((JsonArray)entry.getValue()).copy());
            });
            obj.setJsonArrayValues(map);
          }
          break;
        case "jsonObjectValues":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, io.vertx.core.json.JsonObject> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof JsonObject)
                map.put(entry.getKey(), ((JsonObject)entry.getValue()).copy());
            });
            obj.setJsonObjectValues(map);
          }
          break;
        case "longValues":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, java.lang.Long> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof Number)
                map.put(entry.getKey(), ((Number)entry.getValue()).longValue());
            });
            obj.setLongValues(map);
          }
          break;
        case "shortValues":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, java.lang.Short> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof Number)
                map.put(entry.getKey(), ((Number)entry.getValue()).shortValue());
            });
            obj.setShortValues(map);
          }
          break;
        case "stringValues":
          if (member.getValue() instanceof JsonObject) {
            java.util.Map<String, java.lang.String> map = new java.util.LinkedHashMap<>();
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof String)
                map.put(entry.getKey(), (String)entry.getValue());
            });
            obj.setStringValues(map);
          }
          break;
      }
    }
  }

  public static void toJson(DataObjectWithMaps obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(DataObjectWithMaps obj, java.util.Map<String, Object> json) {
  }
}
