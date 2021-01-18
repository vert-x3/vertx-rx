package io.vertx.codegen.testmodel;

import io.vertx.codegen.annotations.DataObject;

import java.util.Objects;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
@DataObject
public class TestStringDataObject {

  private String value;

  public TestStringDataObject() {
  }

  public TestStringDataObject(TestStringDataObject other) {
    this.value = other.value;
  }

  public TestStringDataObject(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public TestStringDataObject setValue(String value) {
    this.value = value;
    return this;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj instanceof TestStringDataObject) {
      TestStringDataObject that = (TestStringDataObject) obj;
      return Objects.equals(value, that.value);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  public String toJson() {
    return value;
  }
}
