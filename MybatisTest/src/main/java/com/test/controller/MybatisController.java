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

//MyBatis(MemberMapper.xml)�� ������ id�� �������̽�(MemberMapper)�� �޼ҵ带 �����ؼ� ���

@Controller //�ٿ����  component-scan���� �� �ڵ����� ��ϵȴ�.
public class MybatisController {
	
	@Autowired //���輳��. MemberDAOService�� @Repository�پ������Ƿ� �ڵ� ����
	private MemberDAOService memberDAOService;
	
	private static final Logger logger = LoggerFactory.getLogger(MybatisController.class);
	
	
	//���� ����ȭ��.
	@RequestMapping("/main")
	public ModelAndView main(Locale locale, Model model) {
		logger.debug("Welcome main.", locale);

		// view ȭ���� main.jsp�� DB�κ��� �о�� �����͸� �����ش�.
		ModelAndView result = new ModelAndView();
		//addObject view�� �Ѿ�� ������
		List<Member> memberList = memberDAOService.getMembers();
		result.addObject("result", memberList); //memberList�� ��ܿ��� result�� ����ϵ��� ������ ����
		result.setViewName("main");
		return result;
	}
	
	//insert ��ư Ŭ���� ���� �����ͼ� result.jsp�� ȭ����ȯ ���ش�.
	@RequestMapping(value ="/insert", method = RequestMethod.POST)
	public ModelAndView insert(HttpServletRequest request){ //�Է��ϸ� request�� ����
		
		// HttpServletRequest�� �̿��Ͽ� main.jsp�κ��� �����͸� �޾Ƽ� ����
		Member member = new Member();
		member.set_name((String) request.getParameter("name"));
		member.set_email((String) request.getParameter("email"));
		member.set_phone((String) request.getParameter("phone"));
		
		memberDAOService.insertMember(member);
		System.out.println("insert complete");
		
		//�Ʒ��κ��� select���� result.jsp���Ͽ� �����ֱ� ���� �ǻ��.
		ModelAndView result = new ModelAndView();
		List<Member> memberList = memberDAOService.getMembers();
		result.addObject("result", memberList);
		result.setViewName("result");
		return result;
	}
	
	@RequestMapping(value ="/updateform")
	public ModelAndView updateform(Model model) {
		ModelAndView result = new ModelAndView();
		//addObject view�� �Ѿ�� ������
		List<Member> memberList = memberDAOService.getMembers();
		result.addObject("result", memberList); //memberList�� ��ܿ��� result�� ����ϵ��� ������ ����
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




