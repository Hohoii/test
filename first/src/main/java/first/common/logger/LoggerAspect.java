package first.common.logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

//LoggerAspect: Controller, Service, DAO가 실행될 때, 어떤 계층의 어떤 메서드가 실행되었는지를 log찍어서 보여주는 역할

@Aspect //공통모듈(한개 이상의 포인트컷과 어드바이스의 조합으로 만들어짐)
public class LoggerAspect {
	protected Log log = LogFactory.getLog(LoggerAspect.class);
	static String name = "";
	static String type = "";
	
	//@Around(=Around Advice): 적용하는 메소드('포인트컷'으로 지정)의 전,후에 적용
	@Around("execution(* first..controller.*Controller.*(..)) or execution(* first..service.*Impl.*(..)) or execution(* first..dao.*DAO.*(..))")
	public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
		type = joinPoint.getSignature().getDeclaringTypeName(); //JoinPoint를 선언하고 있는 타입의 이름을 반환 즉,JoinPoint가 메소드이면, 해당 메소드의 클래스 이름을 반환
		
		if (type.indexOf("Controller") > -1) {
			name = "Controller  \t:  ";
		}
		else if (type.indexOf("Service") > -1) {
			name = "ServiceImpl  \t:  ";
		}
		else if (type.indexOf("DAO") > -1) {
			name = "DAO  \t\t:  ";
		}
		//joinPoint.getSignature().getName(): JointPoint의 이름을 반환. 메소드 JoinPoint이면 메소드 이름
		log.debug(name + type + "." + joinPoint.getSignature().getName() + "()");
		return joinPoint.proceed();
	}
}
