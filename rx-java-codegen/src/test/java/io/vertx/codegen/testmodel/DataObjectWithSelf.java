package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class DataObjectWithSelf {

  private DataObjectWithSelf self;

  public DataObjectWithSelf() {
  }

  public DataObjectWithSelf(JsonObject json) {
    JsonObject e = json.getJsonObject("self");
    this.self = e != null ? new DataObjectWithSelf(e) : null;
  }

  public DataObjectWithSelf getSelf() {
    return self;
  }

  public DataObjectWithSelf setSelf(DataObjectWithSelf self) {
    this.self = self;
    return this;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    if (self != null) {
      json.put("self", self.toJson());
    }
    return json;
  }
}
