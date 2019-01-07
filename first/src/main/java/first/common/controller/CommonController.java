package first.common.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import first.common.common.CommandMap;
import first.common.service.CommonService;

@Controller
public class CommonController {
	Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="commonService")
	private CommonService commonService;
	
	@RequestMapping(value="/common/downloadFile.do")
	//서버로 어떤 요청을 할때는 request가 전송되고, 반대로 서버에서 화면으로 응답을 할때는 response에 응답내용이 담김.
	public void downloadFile(CommandMap commandMap, HttpServletResponse response) throws Exception {
		Map<String,Object> map = commonService.selectFileInfo(commandMap.getMap()); //첨부파일의 정보가져옴.
		String storedFileName = (String)map.get("STORED_FILE_NAME");
		String originalFileName = (String)map.get("ORIGINAL_FILE_NAME");
		
		//FileUtils이용해서 저장된 위치에서 해당 첨부파일을 읽어서 byte[] 형태로 변환(pom.xml에 commons-io와 commons-fileupload dependency를 추가한 내용)
		//우리가 만든 FileUtils가 아니다.
		byte fileByte[] = FileUtils.readFileToByteArray(new File("C:\\Java\\upload\\"+storedFileName));
		
		//인터넷을 통해서 데이터를 전송하면 request나 response에는 전송할 데이터 뿐만이 아니라 여러가지 정보가 담김. 이걸 설정(띄어쓰기, 대소문자 구분 등도 주의)
		response.setContentType("application/octet-stream");
		response.setContentLength(fileByte.length);
		//Content-Disposition을 attachment로 설정 = 첨부파일을 의미함
		//기존의 첨부파일을 전송할 때 패킷을 보면 request의 Content-Disposition이 "multipart-form/data"다. -> 즉 Content-Disposition 속성을 이용하여 해당 패킷이 어떤 형식의 데이터인지 알 수 있다.
		//fileName부분은 첨부파일의 이름을 지정. 다운로드하라는 팝업창에 뜨는 파일이름 / 한글이 깨지지 않게 하기위해 UTF-8설정
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalFileName, "UTF-8")+"\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
	    response.getOutputStream().write(fileByte);
		
	    //response를 정리하고 닫음.
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
	}
}
