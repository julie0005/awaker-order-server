package org.prgrms.awaker.domain.user;

import org.prgrms.awaker.global.enums.Constant;
import org.prgrms.awaker.global.enums.Regex;
import org.prgrms.awaker.global.exception.LengthErrorException;
import org.prgrms.awaker.global.exception.WrongFormatException;

import java.util.regex.Pattern;

public class UserValidator {

    public static void validateEmail(String email){
        if(!Pattern.matches(Regex.USER_EMAIL.get(), email)){
            throw new WrongFormatException("올바르지 않은 이메일입니다.");
        }
    }

    public static void validateUserName(String name){
        if(name.length()> Constant.MAX_USER_NAME_LENGTH.getValue()){
            throw new LengthErrorException("유저 이름은 %d자를 초과할 수 없습니다.".formatted(Constant.MAX_USER_NAME_LENGTH.getValue()));
        }
        if(!Pattern.matches(Regex.USER_NAME.get(), name)){
            throw new WrongFormatException("올바르지 않은 유저 이름입니다.");
        }
    }

    public static void validateAge(int age){
        if(age <= 0) {
            throw new WrongFormatException("나이는 0 이하일 수 없습니다.");
        }
    }

    public static void validatePassword(String password){
        if(Pattern.matches(Regex.USER_PASSWORD.get(), password)){
            throw new WrongFormatException("올바르지 않은 비밀번호입니다.");
        }
    }

    public static void validateAddress(String address){
        if(address.length() > Constant.MAX_USER_ADDRESS_LENGTH.getValue()){
            throw new LengthErrorException("주소는 200자를 초과할 수 없습니다.");
        }
    }

    public static void validatePostcode(String postcode){
        if(postcode.length() > Constant.MAX_USER_POSTCODE_LENGTH.getValue()){
            throw new LengthErrorException("우편번호는 200자를 초과할 수 없습니다.");
        }
    }

    public static void validatePoint(long point){
        if(point < 0) {
            throw new WrongFormatException("포인트는 0 미만이 될 수 없습니다.");
        }
    }
}
