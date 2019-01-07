package first.sample.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import first.common.common.CommandMap;
import first.sample.service.SampleService;

/*
=>컨트롤러가 있는데 얘는 서비스 가져다 쓴다. 서비스를 보니까 interface가 있고 class가 있다. 이 서비스는 DAO를 가져다 쓴다.
DAO에서 mybatis를 붙여서 작업하는 애는 AbstractDAO고 얘를 상속받아서 포장한 SampleDAO를 서비스에서 가져다 쓴다.
*/
@Controller
public class SampleController {
	Logger log = Logger.getLogger(this.getClass());
	
	//@Autowired는 type으로 찾고 @Resource는 등록된 id로 찾는다.
	@Resource(name="sampleService")
	private SampleService sampleService;
	
	//CustomMapArgumentResolver가 사용자 요청이 Controller에 도달하기 전에 그 요청의 파라미터들을 수정, 추가 후 commandMap리턴
	@RequestMapping(value="/sample/openBoardList.do")
	public ModelAndView openBoardList(CommandMap commandMap) throws Exception {
		//포워딩
		ModelAndView mv = new ModelAndView("/sample/boardList");
		/*
		//commandMap.getMap()은 map을 리턴.(그냥 Map을 파라미터로 쓸 수 없으니 이렇게 함. 실제로 사용하기 위해서 commandMap을 실제 map으로 바꿔줌)
		//map으로 뽑아 온 데이터를 list에 넣는다.(목록이니까 여러개를 담아야한다.)
		List<Map<String,Object>> list = sampleService.selectBoardList(commandMap.getMap());
		//jsp에서 사용하기 위해 list라는 이름으로 등록
		mv.addObject("list", list);
		*/
		return mv;
	}
	
	@RequestMapping(value="/sample/selectBoardList.do")
    public ModelAndView selectBoardList(CommandMap commandMap) throws Exception{
    	//jsonView: action-servlet.xml에 등록한 빈의  id / jsonView= 데이터를 json 형식으로 변환
		ModelAndView mv = new ModelAndView("jsonView");
    	
		//commandMap.getMap()은 map을 리턴.(그냥 Map을 파라미터로 쓸 수 없으니 이렇게 함. 실제로 사용하기 위해서 commandMap을 실제 map으로 바꿔줌)
		//map으로 뽑아 온 데이터를 list에 넣는다.(목록이니까 여러개를 담아야한다.)
		List<Map<String,Object>> list = sampleService.selectBoardList(commandMap.getMap());
    	//jsp에서 사용하기 위해 list라는 이름으로 등록
    	mv.addObject("list", list);
        if(list.size() > 0){
            mv.addObject("TOTAL", list.get(0).get("TOTAL_COUNT")); //전체행수리턴
        }
        else{
            mv.addObject("TOTAL", 0);
        }
    	//Controller에서 json 형식의 데이터를 화면에 전달하는데 그 값은 data라는 이름으로 화면에 전달.
        //꼭 data일 필요는 없다. 그렇지만 필자는 ComAjax에서 callback을 수행할 때, data라는 이름으로 보내주도록 해놨다. ComAjax를 보면 확인할 수 있다
    	//따라서 jsp(boardList.jsp)에서 각각 data.list, data.TOTAL이라는 형식으로 값에 접근
        return mv;
    }
	
	@RequestMapping(value="/sample/openBoardWrite.do")
	public ModelAndView openBoardWrite(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("/sample/boardWrite");
		
		return mv;
	}
	
	@RequestMapping(value="/sample/insertBoard.do")
	//화면에서 전송한 모든 데이터는 HttpServletRequest에 담겨서 전송. HandlerMethodArgumentResolver를 이용하여 CommandMap에 담음.
	//첨부파일은 CommandMap에서 처리를 하지x 따라서 HttpServletRequest를 추가.
	//원래 HttpServletRequest에는 모든 텍스트 데이터 뿐만이 아니라 화면에서 전송된 파일정보도 함께 담김.
	//CommandMap을 이용하여 텍스트 데이터는 처리하기 때문에, HttpServletRequest는 파일정보만 활용
	public ModelAndView insertBoard(CommandMap commandMap, HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("redirect:/sample/openBoardList.do");
	
		sampleService.insertBoard(commandMap.getMap(), request);
	
		return mv;
	}
	
	@RequestMapping(value="/sample/openBoardDetail.do")
	public ModelAndView openBoardDetail(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("/sample/boardDetail");
		
		//commandMap.getMap()은 map을 리턴.(그냥 Map을 파라미터로 쓸 수 없으니 이렇게 함. 실제로 사용하기 위해서 commandMap을 실제 map으로 바꿔줌)
		//목록보기와 다르게 하나의 행만 조회하기떄문에 list가 아닌 map형태로 받는다.
		Map<String,Object> map = sampleService.selectBoardDetail(commandMap.getMap());
		//jsp에서 사용하기 위해 각각의 이름으로 등록
		mv.addObject("map", map.get("map")); //기존의 게시글 상세정보
		mv.addObject("list", map.get("list")); //첨부파일의 목록
		
		return mv;
	}
	
	@RequestMapping(value="/sample/openBoardUpdate.do")
	public ModelAndView openBoardUpdate(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("/sample/boardUpdate");
		
		Map<String,Object> map = sampleService.selectBoardDetail(commandMap.getMap());
		mv.addObject("map", map.get("map")); //기존의 게시글 상세정보
		mv.addObject("list", map.get("list")); //첨부파일의 목록
		
		return mv;
	}
	
	@RequestMapping(value="/sample/updateBoard.do")
	public ModelAndView updateBoard(CommandMap commandMap, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("redirect:/sample/openBoardDetail.do");
		
		sampleService.updateBoard(commandMap.getMap(), request);
		
		mv.addObject("IDX", commandMap.get("IDX"));
		return mv;
	}
	
	@RequestMapping(value="/sample/deleteBoard.do")
	public ModelAndView deleteBoard(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("redirect:/sample/openBoardList.do");
		
		sampleService.deleteBoard(commandMap.getMap());
		
		return mv;
	}
}
	
