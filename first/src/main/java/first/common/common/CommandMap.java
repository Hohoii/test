package first.common.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//<mvc:annotation-driven/>선언시 컨트롤러의 파라미터가 Map 형식이면 동작x
//편법으로 Map을 사용할 수 있는(대신 할) CommandMap을 만듦.
//내부를 보면 Map선언해서 Map의 기능을 호출하는 메소드 사용함 -> 이름 바꿔치기 => 자바빈 필요없이 모든 저장소의 역할을 함.
//이때 절대 Map을 상속받으면x
//CustomMapArgumentResolver 함께보자.
public class CommandMap {
	//String: 전송시켜주는 파라미터 이름 / Object: 세상 모든 객체를 다 받음 => 프로퍼티 매핑 필요x ex) name = 홍길동, address = 강남구역삼동
	Map<String, Object> map = new HashMap<String,Object>();
	
	public Object get(String key){
        return map.get(key);
    }
     
    public void put(String key, Object value){
        map.put(key, value);
    }
     
    public Object remove(String key){
        return map.remove(key);
    }
     
    public boolean containsKey(String key){
        return map.containsKey(key);
    }
     
    public boolean containsValue(Object value){
        return map.containsValue(value);
    }
     
    public void clear(){
        map.clear();
    }
     
    public Set<Entry<String, Object>> entrySet(){
        return map.entrySet();
    }
     
    public Set<String> keySet(){
        return map.keySet();
    }
     
    public boolean isEmpty(){
        return map.isEmpty();
    }
     
    public void putAll(Map<? extends String, ?extends Object> m){
        map.putAll(m);
    }
     
    public Map<String,Object> getMap(){
        return map;
    }
}