package first.sample.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import first.common.util.FileUtils;
import first.sample.dao.SampleDAO;

@Service("sampleService") //()없으면 클래스명에서 맨앞글자 소문자로 등록됨.
public class SampleServiceImpl implements SampleService {
	Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="fileUtils") //DI설정
    private FileUtils fileUtils;
	
	@Resource(name="sampleDAO") //DI설정
	private SampleDAO sampleDAO;
	
	@Override
	//selectBoardList()에 Map객체를 넣어주려고 Controller에서 commandMap.getMap()으로 map을 리턴해서 넣어준다.
	public List<Map<String, Object>> selectBoardList(Map<String, Object> map) throws Exception {
		//DAO를 수십개 만들어 두고(interface만들어 두고)구현해서 Class를 만들고 이것들을 합쳐서 Service를 만들고 이걸 이용해서 Controller를 돌린다. => 객체지향
		return sampleDAO.selectBoardList(map);
	}
	
	@Override
	public void insertBoard(Map<String, Object> map, HttpServletRequest request) throws Exception {
		sampleDAO.insertBoard(map); //SQL문 실행돼서 map에 IDX파라미터 추가됨.
		
		List<Map<String,Object>> list = fileUtils.parseInsertFileInfo(map, request);
        for(int i=0, size=list.size(); i<size; i++){
            sampleDAO.insertFile(list.get(i));
        /*
		//HttpServletRequest 그 자체로는 Multipart형식의 데이터를 조작하는데 어려움이 있기 때문에, MultipartHttpServletRequset 형식으로 형변환
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
		MultipartFile multipartFile = null;
		while(iterator.hasNext()) {
			multipartFile = multipartHttpServletRequest.getFile(iterator.next());
			if(multipartFile.isEmpty() == false) {
				log.debug("------------- file start -------------");
	            log.debug("name : "+multipartFile.getName());
	            log.debug("filename : "+multipartFile.getOriginalFilename());
	            log.debug("size : "+multipartFile.getSize());
	            log.debug("-------------- file end --------------\n");
			}
		}
		*/
            
        }
	}
	
	@Override
	//ServiceImpl에는 하나의 페이지를 호출할 때 필요한 비지니스 로직을 묶어서 처리하는 곳.
	//DAO는 단순히 DB에 접속하여 데이터를 조회하는 역할만 수행하는 클래스.
	//따라서 DAO에서 2개 이상의 동작을 수행하면 안된다.
	public Map<String, Object> selectBoardDetail(Map<String, Object> map) throws Exception {
		//이 두가지 동작은 하나의 트랜잭션으로 처리가 되어야 하는 부분
		sampleDAO.updateHitCnt(map);
		Map<String, Object> resultMap = new HashMap<String,Object>();
		Map<String, Object> tempMap = sampleDAO.selectBoardDetail(map); //게시글의 상세정보
		resultMap.put("map", tempMap);
		
		List<Map<String,Object>> list = sampleDAO.selectFileList(map); //게시글의 첨부파일 목록
	    resultMap.put("list", list);
		
		return resultMap;
	}
	
	@Override
	public void updateBoard(Map<String, Object> map, HttpServletRequest request) throws Exception {
		sampleDAO.updateBoard(map);
		
		//해당 게시글에 해당하는 첨부파일을 전부 삭제처리(DEL_GB = 'Y')
		sampleDAO.deleteFileList(map);
		//request에 담겨있는 파일 정보를 list로 변환
	    List<Map<String,Object>> list = fileUtils.parseUpdateFileInfo(map, request);
	    Map<String,Object> tempMap = null;
	    for(int i=0, size=list.size(); i<size; i++){
	        tempMap = list.get(i);
	        //"Y"인 경우는 신규 저장될 파일이라는 의미이고, "Y"가 아니면 기존에 저장되어 있던 파일이라는 의미
	        if(tempMap.get("IS_NEW").equals("Y")){
	            sampleDAO.insertFile(tempMap);
	        }
	        else{//기존파일인 경우
	            sampleDAO.updateFile(tempMap);
	        }
	    }
	}
	
	@Override
	public void deleteBoard(Map<String, Object> map) throws Exception {
		sampleDAO.deleteBoard(map);
	}

}
