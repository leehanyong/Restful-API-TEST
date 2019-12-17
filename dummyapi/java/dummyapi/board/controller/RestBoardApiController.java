package dummyapi.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import dummyapi.board.dto.BoardFileDto;
import dummyapi.board.dto.exportcontract.ExportContractDto;
import dummyapi.board.service.BoardService;
import dummyapi.common.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@CrossOrigin(origins = "*")
@Api(description = "블록체인 기반 수출채권 매입 및 한도관리 REST API")
@RestController
public class RestBoardApiController {
	
	CommonUtil commonUtil = new CommonUtil();
	
	@Autowired
	private BoardService boardService;
	
	@ApiOperation(value = "수출계약서류 조회 API - parameter없을 경우 전체리스트 조회, "
			+ "검색조건 포함(queryString 예시 : q(검색조건)=documentNumber(수출계약서류번호)=value,exportType(수출유형)=value,startDateTime(등록일자 시작일)=20190101,endDateTime(등록일자 종료일)=20191231"
			+ "&current_page=1(현재요청페이지)"
			+ "&cnt_per_page=10(한 페이지 출력 수)")
	@RequestMapping(value="/dummyapi/exportcontract", method=RequestMethod.GET)
	public Map<String, Object> getExportContractList(HttpServletRequest request) throws Exception{
		log.debug("수출계약서류 전체리스트 조회 - RESTful API : /dummyapi/exportcontract");
		log.debug("request URL : " + request.getRequestURI()+"?"+request.getQueryString());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, String> paramMap = new HashMap<String, String>();
		
		String param = request.getQueryString();
		
		if(param != null && param.equals("")==false){
			paramMap = commonUtil.queryToParam(param);
		}
		
		resultMap = boardService.selectExportContractList(paramMap);
		
		return resultMap;
	}
	
	@ApiOperation(value = "수출계약서류 상세정보 조회 API - parameter : /{contractIdx}")
	@RequestMapping(value="/dummyapi/exportcontract/{contractIdx}", method=RequestMethod.GET)
	public Map<String, Object> getExportContractDetail(@PathVariable("contractIdx") int contractIdx) throws Exception{
		log.debug("수출계약서류 상세정보 조회 - RESTful API : /dummyapi/exportcontract/"+contractIdx);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap = boardService.selectExportContractDetail(contractIdx);
		
		return resultMap; 
	}
	
	@ApiOperation(value = "수출계약서류 첨부파일 업로드 API - POST")
	@RequestMapping(value = "/dummyapi/exportcontract/fileupload", method = {RequestMethod.POST})
	public Map<String, Object> fileUpload(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
		log.debug("수출계약서류 첨부파일 업로드 - RESTful API : /dummyapi/exportcontract/fileupload");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap = boardService.fileUpload(multipartHttpServletRequest);
		
		return resultMap;
	}
	
	@ApiOperation(value = "수출계약서류 첨부파일 다운로드 API - parameter : /{fileIdx}")
	@RequestMapping(value = "/dummyapi/exportcontract/filedownload/{fileIdx}", method=RequestMethod.GET)
	public void fileDownload(@PathVariable("fileIdx") int fileIdx, HttpServletResponse response) throws Exception{
		log.debug("수출계약서류 첨부파일 다운로드 - RESTful API : /dummyapi/exportcontract/filedownload/"+fileIdx);
		BoardFileDto fileMap = boardService.fileDownload(fileIdx);
		
		if(ObjectUtils.isEmpty(fileMap) == false) {
			String fileName = fileMap.getOriginalFileName();
			
			byte[] files = FileUtils.readFileToByteArray(new File(fileMap.getStoredFilePath()));
			
			response.setContentType("application/octet-stream");
			response.setContentLength(files.length);
			response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName,"UTF-8")+"\";");
			response.setHeader("Content-Transfer-Encoding", "binary");
			
			response.getOutputStream().write(files);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}
	
	@ApiOperation(value = "수출계약서류 등록 API - POST")
	@RequestMapping(value = "/dummyapi/exportcontract", method = {RequestMethod.POST})
	public Map<String, Object> regExportContract(@RequestBody ExportContractDto exportDto) throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap = boardService.insertBoard(exportDto);
		
		return resultMap;
	}
	
	@ApiOperation(value = "수출계약서류 수정 API - PUT")
	@RequestMapping(value="dummyapi/exportcontract/{contractIdx}", method=RequestMethod.PUT)
	public Map<String, Object> updateExportContract(@PathVariable("contractIdx") int contractIdx ,@RequestBody ExportContractDto exportDto) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		resultMap = boardService.updateExportContract(contractIdx, exportDto);
		
		return resultMap;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@RequestMapping(value="/api/test", method=RequestMethod.GET)
//	public void openBoardDetaila(HttpServletRequest request, @RequestParam Map<String, Object> map) throws Exception{
//		
//		System.out.println("-------------------------------------");
//		System.out.println(request.getQueryString());
//		System.out.println("-------------------------------------");
//		
//		Set<String> keySet = map.keySet();
//		for(String key : keySet) {
//			System.out.println("key : " + key );
//			System.out.println("key : " + map.get(key).toString());
//		}
//		
//		Enumeration<String> paramNames = request.getParameterNames();
//		boolean first = true;
//		StringBuffer sf = new StringBuffer();
//		
//		while(paramNames.hasMoreElements()) {
//			String pName = paramNames.nextElement();
//			String pValue = request.getParameter(pName);
//			if(first==true){
//       	  		sf.append(pName+"="+pValue)	;
//       	  	}else{
//       	  		sf.append("&"+pName+"="+pValue);
//       	  	}
//       	  	if(first==true) first=false;
//		}
//		System.out.println("body : " + sf.toString());
//			
//		
//		
//	}
	
	
	
	
	
	
	
	/*********************************************************************************************************/


//	@RequestMapping(value="/api/board/{boardIdx}", method=RequestMethod.DELETE)
//	public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception{
//		boardService.deleteBoard(boardIdx);
//		return "redirect:/board";
//	}
//	
	/*********************************************************************************************************/
}

