package io.vertx.codegen.testmodel;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonMapper;

/**
 * Converter and mapper for {@link io.vertx.codegen.testmodel.DataObjectWithLists}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.codegen.testmodel.DataObjectWithLists} original class using Vert.x codegen.
 */
public class DataObjectWithListsConverter implements JsonMapper<DataObjectWithLists, JsonObject> {

  public static final DataObjectWithListsConverter INSTANCE = new DataObjectWithListsConverter();

  @Override public JsonObject serialize(DataObjectWithLists value) { return (value != null) ? value.toJson() : null; }

  @Override public DataObjectWithLists deserialize(JsonObject value) { return (value != null) ? new DataObjectWithLists(value) : null; }

  @Override public Class<DataObjectWithLists> getTargetClass() { return DataObjectWithLists.class; }

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, DataObjectWithLists obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "booleanValues":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.Boolean> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Boolean)
                list.add((Boolean)item);
            });
            obj.setBooleanValues(list);
          }
          break;
        case "dataObjectValues":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.codegen.testmodel.TestDataObject> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.vertx.codegen.testmodel.TestDataObject((JsonObject)item));
            });
            obj.setDataObjectValues(list);
          }
          break;
        case "doubleValues":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.Double> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                list.add(((Number)item).doubleValue());
            });
            obj.setDoubleValues(list);
          }
          break;
        case "enumValues":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.codegen.testmodel.TestEnum> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add(io.vertx.codegen.testmodel.TestEnum.valueOf((String)item));
            });
            obj.setEnumValues(list);
          }
          break;
        case "floatValues":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.Float> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                list.add(((Number)item).floatValue());
            });
            obj.setFloatValues(list);
          }
          break;
        case "genEnumValues":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.codegen.testmodel.TestGenEnum> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add(io.vertx.codegen.testmodel.TestGenEnum.valueOf((String)item));
            });
            obj.setGenEnumValues(list);
          }
          break;
        case "instantValues":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.time.Instant> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add(Instant.from(DateTimeFormatter.ISO_INSTANT.parse((String)item)));
            });
            obj.setInstantValues(list);
          }
          break;
        case "integerValues":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.Integer> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                list.add(((Number)item).intValue());
            });
            obj.setIntegerValues(list);
          }
          break;
        case "jsonArrayValues":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.core.json.JsonArray> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonArray)
                list.add(((JsonArray)item).copy());
            });
            obj.setJsonArrayValues(list);
          }
          break;
        case "jsonObjectValues":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.core.json.JsonObject> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(((JsonObject)item).copy());
            });
            obj.setJsonObjectValues(list);
          }
          break;
        case "longValues":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.Long> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                list.add(((Number)item).longValue());
            });
            obj.setLongValues(list);
          }
          break;
        case "shortValues":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.Short> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof Number)
                list.add(((Number)item).shortValue());
            });
            obj.setShortValues(list);
          }
          break;
        case "stringValues":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.String> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add((String)item);
            });
            obj.setStringValues(list);
          }
          break;
      }
    }
  }

  public static void toJson(DataObjectWithLists obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(DataObjectWithLists obj, java.util.Map<String, Object> json) {
  }
}
