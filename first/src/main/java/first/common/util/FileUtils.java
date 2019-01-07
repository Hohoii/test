package first.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
 
import javax.servlet.http.HttpServletRequest;
 
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Component("fileUtils")
public class FileUtils {
	//파일이 저장될 위치
	private static final String filePath = "C:\\Java\\upload\\";
	
	public List<Map<String,Object>> parseInsertFileInfo(Map<String,Object> map, HttpServletRequest request) throws Exception{
		//HttpServletRequest 그 자체로는 Multipart형식의 데이터를 조작하는데 어려움이 있기 때문에, MultipartHttpServletRequset 형식으로 형변환
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
		
		MultipartFile multipartFile = null;
		String originalFileName = null;
		String originalFileExtension = null;
		String storedFileName = null;
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> listMap = null;
		
		//ServiceImpl 영역에서 전달해준 map에서 신규 생성되는 게시글의 번호를 받음.
		String boardIdx = (String)map.get("IDX");
		
		File file = new File(filePath);
		//해당폴더가 없으면 폴더를 생성
		if(file.exists() == false) {
			file.mkdirs();
		}
		
		while(iterator.hasNext()) {
			multipartFile = multipartHttpServletRequest.getFile(iterator.next());
			if(multipartFile.isEmpty() == false) {
				//파일의 정보를 받아서 새로운 이름으로 변경
				originalFileName = multipartFile.getOriginalFilename();
				originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
				storedFileName = CommonUtils.getRandomString() + originalFileExtension;
				
				//서버에 실제 파일을 저장
				file = new File(filePath + storedFileName);
				multipartFile.transferTo(file);
				
				listMap = new HashMap<String,Object>();
				listMap.put("BOARD_IDX", boardIdx);
				listMap.put("ORIGINAL_FILE_NAME", originalFileName);
				listMap.put("STORED_FILE_NAME", storedFileName);
				listMap.put("FILE_SIZE", multipartFile.getSize());
				list.add(listMap);
			}
		}
		return list;
	}
	
	public List<Map<String, Object>> parseUpdateFileInfo(Map<String, Object> map, HttpServletRequest request) throws Exception{
		//HttpServletRequest 그 자체로는 Multipart형식의 데이터를 조작하는데 어려움이 있기 때문에, MultipartHttpServletRequset 형식으로 형변환
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
	    Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
	     
	    MultipartFile multipartFile = null;
	    String originalFileName = null;
	    String originalFileExtension = null;
	    String storedFileName = null;
	     
	    List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	    Map<String, Object> listMap = null;
	     
	    String boardIdx = (String)map.get("IDX");
	    String requestName = null;
	    String idx = null;
	     
	    //첨부파일이 있다는 것은 해당 파일이 변경됨을 뜻하고 이는 새롭게 저장을 해야한다. 따라서 기존에 parseInsertFileInfo에서 한것과 동일하게 파일을 저장하고 그걸 정보를 list에 추가 
	    while(iterator.hasNext()){
	        multipartFile = multipartHttpServletRequest.getFile(iterator.next());
	        if(multipartFile.isEmpty() == false){
	            originalFileName = multipartFile.getOriginalFilename();
	            originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
	            storedFileName = CommonUtils.getRandomString() + originalFileExtension;
	             
	            multipartFile.transferTo(new File(filePath + storedFileName));
	             
	            listMap = new HashMap<String,Object>();
	            listMap.put("IS_NEW", "Y");
	            listMap.put("BOARD_IDX", boardIdx);
	            listMap.put("ORIGINAL_FILE_NAME", originalFileName);
	            listMap.put("STORED_FILE_NAME", storedFileName);
	            listMap.put("FILE_SIZE", multipartFile.getSize());
	            list.add(listMap);
	        }
	        else{
	        	//html 태그에서 file 태그의 name(file_${var.index }) 값
	            requestName = multipartFile.getName();
	            idx = "IDX_"+requestName.substring(requestName.indexOf("_")+1);
	            //update.jsp를 보면 기존파일은 <input type="hidden" id="IDX" name="IDX_${var.index }" value="${row.IDX }">가 있다.
	            //새로생성된 파일은 이게 없다. 따라서 map에 파라미터로 IDX_${var.index } 값이 존재하면 기존파일이다.
	            if(map.containsKey(idx) == true && map.get(idx) != null){
	                listMap = new HashMap<String,Object>();
	                listMap.put("IS_NEW", "N");
	                listMap.put("FILE_IDX", map.get(idx));
	                list.add(listMap);
	            }
	        }
	    }
	    return list;
	}

}
