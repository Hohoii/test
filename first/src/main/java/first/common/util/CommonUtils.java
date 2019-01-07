package first.common.util;

import java.util.UUID;

public class CommonUtils {
	
	//getRandomString(): 32글자의 랜덤한 문자열(숫자포함)을 만들어서 반환
	public static String getRandomString(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
