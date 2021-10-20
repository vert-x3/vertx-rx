package io.vertx.codegen.testmodel;

public enum TestCustomEnum {
  
  DEV("dev", "development"), 
  
  ITEST("itest", "integration-test");

  public static TestCustomEnum of(String pName) {
    for (TestCustomEnum item : TestCustomEnum.values()) {
      if (item.names[0].equalsIgnoreCase(pName) || item.names[1].equalsIgnoreCase(pName)
          || pName.equalsIgnoreCase(item.name())) {
        return item;
      }
    }
    return DEV;
  }

  private String[] names = new String[2];

  TestCustomEnum(String pShortName, String pLongName) {
    names[0] = pShortName;
    names[1] = pLongName;
  }

  public String getLongName() {
    return names[1];
  }

  public String getShortName() {
    return names[0];
  }

}
