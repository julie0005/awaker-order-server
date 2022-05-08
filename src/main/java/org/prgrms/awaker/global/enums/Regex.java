package org.prgrms.awaker.global.enums;

public enum Regex {
   USER_EMAIL("\\w+@\\w+\\.\\w+(\\.\\w+)?"),
   USER_NAME("[a-zA-Z가-힣]+( [a-zA-Z가-힣]+)*"),
   USER_PASSWORD("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");

   private final String regex;

   Regex(String regex) {
      this.regex = regex;
   }

   public String get() {
      return regex;
   }
}
