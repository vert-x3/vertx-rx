package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter and mapper for {@link io.vertx.codegen.testmodel.DataObjectWithValues}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithValues} original class using Vert.x codegen.
 */
public class DataObjectWithValuesConverter {


  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, DataObjectWithValues obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "booleanValue":
          if (member.getValue() instanceof Boolean) {
            obj.setBooleanValue((Boolean)member.getValue());
          }
          break;
        case "boxedBooleanValue":
          if (member.getValue() instanceof Boolean) {
            obj.setBoxedBooleanValue((Boolean)member.getValue());
          }
          break;
        case "boxedDoubleValue":
          if (member.getValue() instanceof Number) {
            obj.setBoxedDoubleValue(((Number)member.getValue()).doubleValue());
          }
          break;
        case "boxedFloatValue":
          if (member.getValue() instanceof Number) {
            obj.setBoxedFloatValue(((Number)member.getValue()).floatValue());
          }
          break;
        case "boxedIntValue":
          if (member.getValue() instanceof Number) {
            obj.setBoxedIntValue(((Number)member.getValue()).intValue());
          }
          break;
        case "boxedLongValue":
          if (member.getValue() instanceof Number) {
            obj.setBoxedLongValue(((Number)member.getValue()).longValue());
          }
          break;
        case "boxedShortValue":
          if (member.getValue() instanceof Number) {
            obj.setBoxedShortValue(((Number)member.getValue()).shortValue());
          }
          break;
        case "dataObjectValue":
          if (member.getValue() instanceof JsonObject) {
            obj.setDataObjectValue(new io.vertx.codegen.testmodel.TestDataObject((JsonObject)member.getValue()));
          }
          break;
        case "doubleValue":
          if (member.getValue() instanceof Number) {
            obj.setDoubleValue(((Number)member.getValue()).doubleValue());
          }
          break;
        case "enumValue":
          if (member.getValue() instanceof String) {
            obj.setEnumValue(io.vertx.codegen.testmodel.TestEnum.valueOf((String)member.getValue()));
          }
          break;
        case "floatValue":
          if (member.getValue() instanceof Number) {
            obj.setFloatValue(((Number)member.getValue()).floatValue());
          }
          break;
        case "genEnumValue":
          if (member.getValue() instanceof String) {
            obj.setGenEnumValue(io.vertx.codegen.testmodel.TestGenEnum.valueOf((String)member.getValue()));
          }
          break;
        case "instantValue":
          if (member.getValue() instanceof String) {
            obj.setInstantValue(Instant.from(DateTimeFormatter.ISO_INSTANT.parse((String)member.getValue())));
          }
          break;
        case "intValue":
          if (member.getValue() instanceof Number) {
            obj.setIntValue(((Number)member.getValue()).intValue());
          }
          break;
        case "jsonArrayValue":
          if (member.getValue() instanceof JsonArray) {
            obj.setJsonArrayValue(((JsonArray)member.getValue()).copy());
          }
          break;
        case "jsonObjectValue":
          if (member.getValue() instanceof JsonObject) {
            obj.setJsonObjectValue(((JsonObject)member.getValue()).copy());
          }
          break;
        case "longValue":
          if (member.getValue() instanceof Number) {
            obj.setLongValue(((Number)member.getValue()).longValue());
          }
          break;
        case "shortValue":
          if (member.getValue() instanceof Number) {
            obj.setShortValue(((Number)member.getValue()).shortValue());
          }
          break;
        case "stringValue":
          if (member.getValue() instanceof String) {
            obj.setStringValue((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(DataObjectWithValues obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(DataObjectWithValues obj, java.util.Map<String, Object> json) {
  }
}
