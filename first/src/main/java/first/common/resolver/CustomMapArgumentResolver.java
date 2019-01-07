package first.common.resolver;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import first.common.common.CommandMap;

//HandlerMethodArgumentResolver: 사용자 요청이 Controller에 도달하기 전에 그 요청의 파라미터들을 수정, 추가
//데이터를 CommandMap으로 보내고 받는 기능을 사용하려면 HandlerMethodArgumentResolver을 구현한 CustomMapArgumentResolver를 만들어야 함.
//action-servlet.xml에 <mvc:annotation-driven>밑에 <mvc:argument-resolvers>로 CustomMapArgumentResolver등록
public class CustomMapArgumentResolver implements HandlerMethodArgumentResolver{
	
	@Override
	//supportsParameter(): CommandMap을 사용하겠다
	public boolean supportsParameter(MethodParameter parameter) {
		return CommandMap.class.isAssignableFrom(parameter.getParameterType());
}
	
	@Override
	//resolveArgument(): CommandMap을 어떻게 쓰는지
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		//CommandMap 객체 생성하고
		CommandMap commandMap = new CommandMap();
		
		//이름들을 몽창 뽑아서
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		Enumeration<?> enumeration = request.getParameterNames();
		
		String key = null;
		String[] values = null;
		//반복처리해서 키와 값을 뽑아내서 내가 만든 CommandMap에 넣는다. 
		while(enumeration.hasMoreElements()) {
			key = (String) enumeration.nextElement();
			values = request.getParameterValues(key);
			if(values != null) {
				commandMap.put(key, (values.length > 1) ? values:values[0] );
			}
		}
		return commandMap;
	}

}
//전송된아규먼트에 대한 리졸버 설정 (이렇게하면 전송된건 내가만든 커맨드맵에 다 꽂힌다)
/* => 전송된 데이터를 하나하나 받을 때는 request.getParameter. 여러개를 하나의 맵에 꽂는 방법없다. 
request로 받아서 블럭안에서 getParameter로 하나씩 뽑아서 맵에 다시 넣어줘야 함.
이걸 한번에 하기 위해서 CommandMap만들고 CustomMapArgumentResolver만듦. */
/* => 기존에는 테이블마다 자바빈을 하나씩 만들었지만 이렇게 하면 하나의 Map으로 해결 가능하다.
단, Map은 자바빈이 아니니까 Validation할 수 없다.(단점)*/


