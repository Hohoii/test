package com.test.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.test.mybatis.Member;
import com.test.mybatis.MemberDAOService;

//MyBatis(MemberMapper.xml)에 정의한 id와 인터페이스(MemberMapper)의 메소드를 매핑해서 사용

@Controller //붙여줘야  component-scan했을 때 자동으로 등록된다.
public class MybatisController {
	
	@Autowired //관계설정. MemberDAOService가 @Repository붙어있으므로 자동 빈등록
	private MemberDAOService memberDAOService;
	
	private static final Logger logger = LoggerFactory.getLogger(MybatisController.class);
	
	
	//시작 메인화면.
	@RequestMapping("/main")
	public ModelAndView main(Locale locale, Model model) {
		logger.debug("Welcome main.", locale);

		// view 화면인 main.jsp에 DB로부터 읽어온 데이터를 보여준다.
		ModelAndView result = new ModelAndView();
		//addObject view에 넘어가는 데이터
		List<Member> memberList = memberDAOService.getMembers();
		result.addObject("result", memberList); //memberList를 뷰단에서 result로 사용하도록 영역에 저장
		result.setViewName("main");
		return result;
	}
	
	//insert 버튼 클릭시 값을 가져와서 result.jsp로 화면전환 해준다.
	@RequestMapping(value ="/insert", method = RequestMethod.POST)
	public ModelAndView insert(HttpServletRequest request){ //입력하면 request로 받음
		
		// HttpServletRequest를 이용하여 main.jsp로부터 데이터를 받아서 설정
		Member member = new Member();
		member.set_name((String) request.getParameter("name"));
		member.set_email((String) request.getParameter("email"));
		member.set_phone((String) request.getParameter("phone"));
		
		memberDAOService.insertMember(member);
		System.out.println("insert complete");
		
		//아래부분은 select값을 result.jsp파일에 보여주기 위해 또사용.
		ModelAndView result = new ModelAndView();
		List<Member> memberList = memberDAOService.getMembers();
		result.addObject("result", memberList);
		result.setViewName("result");
		return result;
	}
	
	@RequestMapping(value ="/updateform")
	public ModelAndView updateform(Model model) {
		ModelAndView result = new ModelAndView();
		//addObject view에 넘어가는 데이터
		List<Member> memberList = memberDAOService.getMembers();
		result.addObject("result", memberList); //memberList를 뷰단에서 result로 사용하도록 영역에 저장
		result.setViewName("update");
		return result;
	}
	
	@RequestMapping(value ="/update", method = RequestMethod.GET)
	public ModelAndView update(@RequestParam("name") String name) {
		
		System.out.println(name);
		
		memberDAOService.updateMember(name);
		System.out.println("update complete");
	
		ModelAndView result = new ModelAndView();
		List<Member> memberList = memberDAOService.getMembers();
		result.addObject("result", memberList);
		result.setViewName("result");
		return result;
	}
}




