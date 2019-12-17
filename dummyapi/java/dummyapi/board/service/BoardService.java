package dummyapi.board.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import dummyapi.board.dto.BoardFileDto;
import dummyapi.board.dto.exportcontract.ExportContractDto;

public interface BoardService {
	
	/*
	 * dummyAPI 수출계약서류 리스트 조회
	 */
	Map<String, Object> selectExportContractList(Map<String, String> param) throws Exception;
	
	/*
	 * dummyAPI 수출계약서류 상세정보 조회
	 */
	Map<String, Object> selectExportContractDetail(int contractIdx) throws Exception;	
	
	/*
	 * dummyAPI 수출계약 서류 첨부파일 업로드
	 */
	Map<String, Object> fileUpload(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;
	
	/*
	 * dummyAPI 수출계약 서류 첨부파일 다운로드
	 */
	BoardFileDto fileDownload(int fileIdx) throws Exception;
	
	/*
	 * dummyAPI 수출계약 서류 등록
	 */	
	Map<String, Object> insertBoard(ExportContractDto exportDto) throws Exception;
	
	/*
	 * dummyAPI 수출계약 서류 수정
	 */	
	Map<String, Object> updateExportContract(int contractIdx, ExportContractDto exportDto) throws Exception;
	
	
	
	
	
	
	
	
	
	
	/*********************************************************************************************************/

//	void deleteBoard(int boardIdx) throws Exception;
	

}
