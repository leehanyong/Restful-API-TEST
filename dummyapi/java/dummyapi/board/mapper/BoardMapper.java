package dummyapi.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import dummyapi.board.dto.BoardFileDto;
import dummyapi.board.dto.exportcontract.ExportContractDto;
import dummyapi.board.dto.exportcontract.ExportContractHistoryDto;

@Mapper
public interface BoardMapper {
	
	/*
	 * dummyAPI 수출계약서류 정보 전체 리스트 조회 쿼리
	 */
	List<ExportContractDto> selectExportContractList(Map<String, Object> map) throws Exception;
	
	/*
	 * dummyAPI 수출계약서류 전체 리스트 조회결과 건수 조회
	 */
	int selectExportContractListCnt(Map<String, Object> map) throws Exception;
	
	/*
	 * dummyAPI 수출계약서류 상세정보 조회
	 */
	ExportContractDto selectExportContractDetail(int contractIdx) throws Exception;
	
	/*
	 * dummyAPI 수출계약서류 상세점보 - 처리내역 조회
	 */
	List<ExportContractHistoryDto> selectExportContractHistoryList(int contractIdx) throws Exception;
	
	/*
	 * dummyAPI 수출계약서류 상세점보 - 첨부파일 리스트 조회
	 */
	List<BoardFileDto> selectExportContractFileList(int fileObjectIdentifier) throws Exception;
	
	/*
	 * dummyAPI 수출계약서류 - 파일 테이블 fileObjectIdentifier 값 없을 시 초기 값 생성 
	 */	
	int getfileIdx() throws Exception;
	
	/*
	 * dummyAPI 수출계약서류 - contractIdx에 해당하는 파일 리스트 fileObjectIdentifier값 조회 
	 */
	int getfileIdentifier(int contractIdx) throws Exception;
	
	/*
	 * dummyAPI 수출계약서류 식별 키에 해당하는 마지막 처리내역 조회
	 */
	String getStatus(int contractIdx) throws Exception;
	
	/*
	 * dummyAPI 수출계약서류 첨부파일 업로드
	 */
	void uploadFileList(List<BoardFileDto> list) throws Exception;
	
	/*
	 * dummyAPI 수출계약서류 첨부파일 다운로드 정보 조회
	 */
	BoardFileDto selectExportFileInfo(@Param("fileIdx") int fileIdx);
	
	/*
	 * dummyAPI 수출계약서류 등록
	 */
	void insertExportContract(ExportContractDto exportDto) throws Exception;
	
	/*
	 * dummyAPI 수출계약서류 등록
	 */
	void updateExportContract(ExportContractDto exportDto) throws Exception;
	
	
	/*********************************************************************************************************/
		

	
//	void deleteBoard(int boardIdx) throws Exception;
	
	/*********************************************************************************************************/

}
