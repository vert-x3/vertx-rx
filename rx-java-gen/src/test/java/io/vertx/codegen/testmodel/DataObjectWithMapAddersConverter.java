package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter and mapper for {@link io.vertx.codegen.testmodel.DataObjectWithMapAdders}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithMapAdders} original class using Vert.x codegen.
 */
public class DataObjectWithMapAddersConverter {


  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, DataObjectWithMapAdders obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "booleanValues":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof Boolean)
                obj.addBooleanValue(entry.getKey(), (Boolean)entry.getValue());
            });
          }
          break;
        case "dataObjectValues":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof JsonObject)
                obj.addDataObjectValue(entry.getKey(), new io.vertx.codegen.testmodel.TestDataObject((JsonObject)entry.getValue()));
            });
          }
          break;
        case "doubleValues":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof Number)
                obj.addDoubleValue(entry.getKey(), ((Number)entry.getValue()).doubleValue());
            });
          }
          break;
        case "enumValues":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof String)
                obj.addEnumValue(entry.getKey(), io.vertx.codegen.testmodel.TestEnum.valueOf((String)entry.getValue()));
            });
          }
          break;
        case "floatValues":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof Number)
                obj.addFloatValue(entry.getKey(), ((Number)entry.getValue()).floatValue());
            });
          }
          break;
        case "genEnumValues":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof String)
                obj.addGenEnumValue(entry.getKey(), io.vertx.codegen.testmodel.TestGenEnum.valueOf((String)entry.getValue()));
            });
          }
          break;
        case "instantValues":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof String)
                obj.addInstantValue(entry.getKey(), Instant.from(DateTimeFormatter.ISO_INSTANT.parse((String)entry.getValue())));
            });
          }
          break;
        case "integerValues":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof Number)
                obj.addIntegerValue(entry.getKey(), ((Number)entry.getValue()).intValue());
            });
          }
          break;
        case "jsonArrayValues":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof JsonArray)
                obj.addJsonArrayValue(entry.getKey(), ((JsonArray)entry.getValue()).copy());
            });
          }
          break;
        case "jsonObjectValues":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof JsonObject)
                obj.addJsonObjectValue(entry.getKey(), ((JsonObject)entry.getValue()).copy());
            });
          }
          break;
        case "longValues":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof Number)
                obj.addLongValue(entry.getKey(), ((Number)entry.getValue()).longValue());
            });
          }
          break;
        case "shortValues":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof Number)
                obj.addShortValue(entry.getKey(), ((Number)entry.getValue()).shortValue());
            });
          }
          break;
        case "stringValues":
          if (member.getValue() instanceof JsonObject) {
            ((Iterable<java.util.Map.Entry<String, Object>>)member.getValue()).forEach(entry -> {
              if (entry.getValue() instanceof String)
                obj.addStringValue(entry.getKey(), (String)entry.getValue());
            });
          }
          break;
      }
    }
  }

  public static void toJson(DataObjectWithMapAdders obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(DataObjectWithMapAdders obj, java.util.Map<String, Object> json) {
  }
}
