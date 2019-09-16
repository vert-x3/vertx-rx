package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter and mapper for {@link io.vertx.codegen.testmodel.DataObjectWithListAdders}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithListAdders} original class using Vert.x codegen.
 */
public class DataObjectWithListAddersConverter {


  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, DataObjectWithListAdders obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "booleanValues":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Boolean)
                obj.addBooleanValue((Boolean)item);
            });
          }
          break;
        case "dataObjectValues":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                obj.addDataObjectValue(new io.vertx.codegen.testmodel.TestDataObject((JsonObject)item));
            });
          }
          break;
        case "doubleValues":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                obj.addDoubleValue(((Number)item).doubleValue());
            });
          }
          break;
        case "enumValues":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                obj.addEnumValue(io.vertx.codegen.testmodel.TestEnum.valueOf((String)item));
            });
          }
          break;
        case "floatValues":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                obj.addFloatValue(((Number)item).floatValue());
            });
          }
          break;
        case "genEnumValues":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                obj.addGenEnumValue(io.vertx.codegen.testmodel.TestGenEnum.valueOf((String)item));
            });
          }
          break;
        case "instantValues":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                obj.addInstantValue(Instant.from(DateTimeFormatter.ISO_INSTANT.parse((String)item)));
            });
          }
          break;
        case "integerValues":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                obj.addIntegerValue(((Number)item).intValue());
            });
          }
          break;
        case "jsonArrayValues":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonArray)
                obj.addJsonArrayValue(((JsonArray)item).copy());
            });
          }
          break;
        case "jsonObjectValues":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                obj.addJsonObjectValue(((JsonObject)item).copy());
            });
          }
          break;
        case "longValues":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                obj.addLongValue(((Number)item).longValue());
            });
          }
          break;
        case "shortValues":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                obj.addShortValue(((Number)item).shortValue());
            });
          }
          break;
        case "stringValues":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                obj.addStringValue((String)item);
            });
          }
          break;
      }
    }
  }

  public static void toJson(DataObjectWithListAdders obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(DataObjectWithListAdders obj, java.util.Map<String, Object> json) {
  }
}
