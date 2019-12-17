package dummyapi.board.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import dummyapi.board.dto.BoardFileDto;
import dummyapi.board.dto.exportcontract.ExportContractDto;
import dummyapi.board.dto.exportcontract.ExportContractHistoryDto;
import dummyapi.board.mapper.BoardMapper;
import dummyapi.common.CommonUtil;
import dummyapi.common.FileUtils;


@Service
public class BoardServiceImpl implements BoardService{
	
	CommonUtil commonUtil = new CommonUtil();
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private FileUtils fileUtils;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> selectExportContractList(Map<String, String> param) throws Exception {
		
		Map<String, Object> resultMap 		= new HashMap<String, Object>();		//서비스 처리 결과를 담을 오브젝트
		Map<String, String> conditionMap	= new HashMap<String, String>();		//상세검색 조건 파라미터를 담을 오브젝트
		List<ExportContractDto> resultList 	= new ArrayList<ExportContractDto>();	//검색결과 리스트를 담을 오브젝트
		
		if(param.containsKey("q")==true) {
			conditionMap = commonUtil.detailConditionToParam(param.get("q").toString());
		}
		
		/*
		 * 1. 검색조건 파라미터 validation check
		 */
		String documentNumber	= conditionMap.containsKey("documentNumber") 	== true ? documentNumber = conditionMap.get("documentNumber").toString() 	: "";	//수출계약서류번호
		String exportType		= conditionMap.containsKey("exportType") 		== true ? exportType 	 = conditionMap.get("exportType").toString() 		: "";	//수출유형
		String startDateTime	= conditionMap.containsKey("startDateTime") 	== true ? startDateTime	 = conditionMap.get("startDateTime").toString()		: "";	//서류등록일자 시작
		String endDateTime		= conditionMap.containsKey("endDateTime") 		== true ? endDateTime	 = conditionMap.get("endDateTime").toString() 		: "";	//서류등록일자 끝
		String current_page		= param.containsKey("current_page") 			== true ? current_page	 = param.get("current_page").toString() 			: "1";	//현재 요청 페이지
		String cnt_per_page		= param.containsKey("cnt_per_page") 			== true ? cnt_per_page	 = param.get("cnt_per_page").toString() 			: "";	//현재 페이지 출력 수
		
		/*
		 * 2. 검색조건 파라미터 validation check 및 타입변환
		 */
		if(startDateTime.equals("") == true && endDateTime.equals("") == false) startDateTime = endDateTime;
		if(startDateTime.equals("") == false && endDateTime.equals("") == true) endDateTime = startDateTime;
		
		if(startDateTime.equals("") == false ){
			startDateTime = commonUtil.getDateFormat(startDateTime,"-") + " 00:00:00";
			endDateTime   = commonUtil.getDateFormat(endDateTime,"-") + " 23:59:59";
		}

		int from_cnt = 0;
        int to_cnt   = 0;
        if(cnt_per_page != null && cnt_per_page.equals("")==false) {
	        from_cnt     = (( Integer.parseInt(current_page) - 1 ) * Integer.parseInt(cnt_per_page)) ;
	        to_cnt       = Integer.parseInt(cnt_per_page);
//	        to_cnt       = (( Integer.parseInt(current_page) - 1 ) * Integer.parseInt(cnt_per_page)) + Integer.parseInt(cnt_per_page);
        }
		/*
		 * 3. 검색조건 파라미터 구성
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		if(documentNumber.equals("")== false) { map.put("documentNumber"	, documentNumber); 	} else {  map.put("documentNumber", ""); 	};
		if(exportType.equals("")	== false) { map.put("exportType"		, exportType); 		} else {  map.put("exportType", ""); 		};
		if(startDateTime.equals("")	== false) { map.put("startDateTime"	, startDateTime); 	} else {  map.put("startDateTime", ""); 	};
		if(endDateTime.equals("")	== false) { map.put("endDateTime"	, endDateTime); 	} else {  map.put("endDateTime", ""); 	};
		map.put("from_cnt"	, from_cnt);
		map.put("to_cnt"	, to_cnt);
		
		
		/*
		 * 4. 검색조건 리스트 조회 
		 */
		int resultCnt = boardMapper.selectExportContractListCnt(map);
		resultMap.put("pagetotalCount", resultCnt);
		
		resultList = boardMapper.selectExportContractList(map);
		
		
		int listCnt = 0;
		int listPage = 0;
		
		if(resultList.isEmpty()==false) {
			listCnt = resultList.size();
		}
		
		if(cnt_per_page != null && cnt_per_page.equals("")==false) {
			if (listCnt % Integer.parseInt(cnt_per_page) == 0)
				listPage = listCnt / Integer.parseInt(cnt_per_page);
	        else
	        	listPage = (listCnt - (listCnt % Integer.parseInt(cnt_per_page))) / Integer.parseInt(cnt_per_page) + 1;
		} else {
			listPage = 1;
		}
		resultMap.put("totalCount", ""+listCnt);
		resultMap.put("totalPage", ""+listPage);
		
		
		/*
		 * 5. 서비스 호출 결과 조합 
		 */
		if(resultList.isEmpty() == true) {
			resultMap.put("state"		, "WARNNING");
			resultMap.put("stateDesc"	, "수출계약서류 리스트 조회 결과가 존재하지 않습니다.");
			resultMap.put("searchList", "");
			return resultMap;
		} else {
			
			JSONArray returnArray = new JSONArray();
			
			for(ExportContractDto dtoMap : resultList) {
				
				//dummy 데이터이기 때문에 대기상태로 모두 초기화
				dtoMap.setStatus("PRO01");
				
//				String status = boardMapper.getStatus(dtoMap.getContractIdx()); 
//						
//				if(status != null && status.equals("")==false) {
//					dtoMap.setStatus(status);
//				} else {
//					dtoMap.setStatus("PRO01");
//				}
						
				returnArray.add(commonUtil.getJSONObject(dtoMap));
			}
			
			resultMap.put("state"		, "SUCCESS");
			resultMap.put("stateDesc"	, "수출계약서류 리스트 조회를 완료하였습니다.");
			resultMap.put("searchList", returnArray);
		}
		
		return resultMap; 
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> selectExportContractDetail(int contractIdx) throws Exception{
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(contractIdx == -1 || contractIdx == 0) {
			resultMap.put("state"		, "WARNNING");
			resultMap.put("stateDesc"	, "수출계약서류 상세정보 조회 필수 파라미터가 존재하지 않습니다.(/dummyapi/exportcontract/contractIdx - 식별 값)");
			resultMap.put("searchList", "");
			return resultMap;
		}
		
		ExportContractDto dataMap = boardMapper.selectExportContractDetail(contractIdx);
		
		
		if(dataMap.getContractIdx() != -1 || dataMap.getContractIdx() != 0) {
			
			JSONObject resultDataMap = new JSONObject();
			
			/* 수입서류 식별자에 대한 데이터 존재 시 반환 JSONOBJECT 생성 */
			resultDataMap = commonUtil.getJSONObject(dataMap);
			
			/* 수입서류 처리내역 리스트 조회 */
			List<ExportContractHistoryDto> historyList =  boardMapper.selectExportContractHistoryList(dataMap.getContractIdx());
			
			if(historyList.size() > 0) {
				JSONArray historyArray = new JSONArray();
				
				for(ExportContractHistoryDto dtoMap : historyList) {
					historyArray.add(commonUtil.getJSONObject(dtoMap));
				}
				resultDataMap.put("historyList", historyArray);
			} else {
				resultDataMap.put("historyList", "");
			}
			
			/* 수입서류 상세정보에 포함되는 파일 리스트 조회 */
			
			int fileObjectIdentifier = 0; 
					
			fileObjectIdentifier = boardMapper.getfileIdentifier(contractIdx);
			
			if(fileObjectIdentifier != 0 || fileObjectIdentifier != -1) {
				List<BoardFileDto> fileList = boardMapper.selectExportContractFileList(fileObjectIdentifier);
				
				if(fileList.size() > 0) {
					JSONArray fileArray = new JSONArray();
					
					for(BoardFileDto fileMap : fileList) {
						fileArray.add(commonUtil.getJSONObject(fileMap));
					}
					resultDataMap.put("fileList", fileArray);
					 
				} else {
					resultDataMap.put("fileList", "");
				}
			} else {
				resultDataMap.put("fileList", "");
			}
			
			resultMap.put("state"		, "SUCCESS");
			resultMap.put("stateDesc"	, "수출계약서류 상세정보 결과 조회를 완료하였습니다.");
			resultMap.put("searchList"	, resultDataMap);
		} else {
			resultMap.put("state"		, "WARNNING");
			resultMap.put("stateDesc"	, "수출계약서류 상세정보 조회 결과가 존재하지 않습니다.");
			resultMap.put("searchList", "");	
		}
		
		return resultMap;
	}
	
	
	@Override
	public Map<String, Object> fileUpload(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<BoardFileDto> list = fileUtils.parseFileInfo(multipartHttpServletRequest);

		int fileIdx = 0;
		
		if(CollectionUtils.isEmpty(list) == false){
			
			fileIdx = list.get(0).getFileObjectIdentifier();
			
			if(fileIdx != 0 || fileIdx != -1) {
				resultMap.put("fileObjectIdentifier", fileIdx);
			} else {
				resultMap.put("state"		, "WARNNING");
				resultMap.put("stateDesc"	, "파일 업로드 식별자 생성에 실패하였습니다.");
				resultMap.put("fileObjectIdentifier", "");
				return resultMap;
			}
			
			boardMapper.uploadFileList(list);
			
		}
		
		resultMap.put("state"		, "SUCCESS");
		resultMap.put("stateDesc"	, "파일 업로드를 완료하였습니다.");
		
		return resultMap;
	}
	
	
	@Override
	public BoardFileDto fileDownload(int fileIdx) throws Exception {
		
		return boardMapper.selectExportFileInfo(fileIdx);
		
	}
	
	
	@Override
	public Map<String, Object> insertBoard(ExportContractDto exportDto) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String exportType 				= exportDto.getExportType().equals("") 				== true ? exportType 			= "" : exportDto.getExportType().toString();			//수출유형코드 
		String documentNumber 			= exportDto.getDocumentNumber().equals("") 			== true ? documentNumber 		= "" : exportDto.getDocumentNumber().toString();		//수출계약 수출서류번호 
		String currencyCode 			= exportDto.getCurrencyCode().equals("") 			== true ? currencyCode 			= "" : exportDto.getCurrencyCode().toString();			//수출계약 계약금액의 통화코드 
		String contractDateTime 		= exportDto.getContractDateTime().equals("") 		== true ? contractDateTime 		= "" : exportDto.getContractDateTime().toString();		//수출계약 계약일자 
		String priceTerms 				= exportDto.getPriceTerms().equals("") 				== true ? priceTerms 			= "" : exportDto.getPriceTerms().toString();			//수출계약 가격조건 
		String shipmentDueDateTime 		= exportDto.getShipmentDueDateTime().equals("") 	== true ? shipmentDueDateTime 	= "" : exportDto.getShipmentDueDateTime().toString();	//수출계약 선적기일 
		String exporterName 			= exportDto.getExporterName().equals("") 			== true ? exporterName 			= "" : exportDto.getExporterName().toString();			//수출계약 수출자 상호 
		String exporterSsn 				= exportDto.getExporterSsn().equals("") 			== true ? exporterSsn 			= "" : exportDto.getExporterSsn().toString();			//수출계약 수출자 사업자번호 
		String importerName 			= exportDto.getImporterName().equals("") 			== true ? importerName 			= "" : exportDto.getImporterName().toString();			//수출계약 수입자 상호
		String importerCountryCode 		= exportDto.getImporterCountryCode().equals("") 	== true ? importerCountryCode 	= "" : exportDto.getImporterCountryCode().toString();	//수출계약 수입자 국가코드 
		String shipmentCountryCode 		= exportDto.getShipmentCountryCode().equals("") 	== true ? shipmentCountryCode 	= "" : exportDto.getShipmentCountryCode().toString();	//선적지 국가코드 
		String shipmentPortCode 		= exportDto.getShipmentPortCode().equals("") 		== true ? shipmentPortCode 		= "" : exportDto.getShipmentPortCode().toString();		//선적지 항만코드 
		String shipmentPortName 		= exportDto.getShipmentPortName().equals("") 		== true ? shipmentPortName 		= "" : exportDto.getShipmentPortName().toString();		//선적지 이름 
		String destinationCountryCode 	= exportDto.getDestinationCountryCode().equals("") 	== true ? destinationCountryCode= "" : exportDto.getDestinationCountryCode().toString();//도착지 국가코드 
		String destinationPortCode 		= exportDto.getDestinationPortCode().equals("") 	== true ? destinationPortCode 	= "" : exportDto.getDestinationPortCode();				//도착지 항만코드 
		String destinationPortName 		= exportDto.getDestinationPortName().equals("") 	== true ? destinationPortName 	= "" : exportDto.getDestinationPortName().toString();	//도착지 이름 
		
		int contractAmount 				= exportDto.getContractAmount(); //수출계약 계약금액
		
		if(exportType.equals("")) 				{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ exportType ]이 존재하지 않습니다.");			return resultMap; 	}
		if(documentNumber.equals(""))			{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ documentNumber ]이 존재하지 않습니다.");		return resultMap; 	}
		if(currencyCode.equals("")) 			{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ currencyCode ]이 존재하지 않습니다.");			return resultMap; 	}
		if(contractDateTime.equals("")) 		{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ contractDateTime ]이 존재하지 않습니다."); 	return resultMap;	}
		if(priceTerms.equals("")) 				{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ priceTerms ]이 존재하지 않습니다."); 			return resultMap; 	}
		if(shipmentDueDateTime.equals("")) 		{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ shipmentDueDateTime ]이 존재하지 않습니다."); 	return resultMap; 	}
		if(exporterName.equals("")) 			{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ exporterName ]이 존재하지 않습니다."); 		return resultMap; 	}
		if(exporterSsn.equals("")) 				{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ exporterSsn ]이 존재하지 않습니다."); 			return resultMap; 	}
		if(importerName.equals("")) 			{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ importerName ]이 존재하지 않습니다."); 		return resultMap; 	}
		if(importerCountryCode.equals("")) 		{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ importerCountryCode ]이 존재하지 않습니다."); 	return resultMap; 	}
		if(shipmentCountryCode.equals("")) 		{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ shipmentCountryCode ]이 존재하지 않습니다."); 	return resultMap; 	}
		if(shipmentPortCode.equals("")) 		{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ shipmentPortCode ]이 존재하지 않습니다."); 	return resultMap; 	}
		if(shipmentPortName.equals("")) 		{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ shipmentPortName ]이 존재하지 않습니다."); 	return resultMap; 	}
		if(destinationCountryCode.equals("")) 	{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ destinationCountryCode ]이 존재하지 않습니다.");return resultMap; 	}
		if(destinationPortCode.equals("")) 		{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ destinationPortCode ]이 존재하지 않습니다."); 	return resultMap; 	}
		if(destinationPortName.equals("")) 		{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ destinationPortName ]이 존재하지 않습니다."); 	return resultMap; 	}
		if(contractAmount < 0)					{ resultMap.put("state", "WARNNING"); resultMap.put("stateDesc"	, "필수 입력 값인 [ contractAmount ]이 존재하지 않습니다."); 		return resultMap; 	}
		
		if(contractDateTime.equals("") == false) {
			exportDto.setContractDateTime(commonUtil.getDateFormat(contractDateTime,"-") + " 00:00:00");
		}
		if(shipmentDueDateTime.equals("") == false ) {
			exportDto.setShipmentDueDateTime(commonUtil.getDateFormat(shipmentDueDateTime,"-") + " 00:00:00");
		}
		
		
		boardMapper.insertExportContract(exportDto);
		
		resultMap.put("state"		, "SUCCESS");
		resultMap.put("stateDesc"	, "수출계약서류 입력을 완료하였습니다.");
		
		return resultMap;
	}
	
	
	@Override
	public Map<String, Object> updateExportContract(int contractIdx, ExportContractDto exportDto) throws Exception {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
	
		if(contractIdx < 0 || contractIdx == 0) {
			resultMap.put("state"		, "WARNNING");
			resultMap.put("stateDesc"	, "필수 입력 값인 [ contractIdx ]이 존재하지 않습니다.");
			return resultMap;
		} else {
			exportDto.setContractIdx(contractIdx);
		}
		
//		String exportType 				= exportDto.getExportType() 			== null ? exportType 			= "" : exportDto.getExportType().toString();			//수출유형코드 
//		String documentNumber 			= exportDto.getDocumentNumber()			== null ? documentNumber 		= "" : exportDto.getDocumentNumber().toString();		//수출계약 수출서류번호 
//		String currencyCode 			= exportDto.getCurrencyCode()			== null ? currencyCode 			= "" : exportDto.getCurrencyCode().toString();			//수출계약 계약금액의 통화코드 
//		String priceTerms 				= exportDto.getPriceTerms()			 	== null ? priceTerms 			= "" : exportDto.getPriceTerms().toString();			//수출계약 가격조건 
//		String exporterName 			= exportDto.getExporterName()			== null ? exporterName 			= "" : exportDto.getExporterName().toString();			//수출계약 수출자 상호 
//		String exporterSsn 				= exportDto.getExporterSsn()			== null ? exporterSsn 			= "" : exportDto.getExporterSsn().toString();			//수출계약 수출자 사업자번호 
//		String importerName 			= exportDto.getImporterName()			== null ? importerName 			= "" : exportDto.getImporterName().toString();			//수출계약 수입자 상호
//		String importerCountryCode 		= exportDto.getImporterCountryCode()	== null ? importerCountryCode 	= "" : exportDto.getImporterCountryCode().toString();	//수출계약 수입자 국가코드 
//		String shipmentCountryCode 		= exportDto.getShipmentCountryCode()	== null ? shipmentCountryCode 	= "" : exportDto.getShipmentCountryCode().toString();	//선적지 국가코드 
//		String shipmentPortCode 		= exportDto.getShipmentPortCode()		== null ? shipmentPortCode 		= "" : exportDto.getShipmentPortCode().toString();		//선적지 항만코드 
//		String shipmentPortName 		= exportDto.getShipmentPortName()		== null ? shipmentPortName 		= "" : exportDto.getShipmentPortName().toString();		//선적지 이름 
//		String destinationCountryCode 	= exportDto.getDestinationCountryCode()	== null ? destinationCountryCode= "" : exportDto.getDestinationCountryCode().toString();//도착지 국가코드 
//		String destinationPortCode 		= exportDto.getDestinationPortCode()	== null ? destinationPortCode 	= "" : exportDto.getDestinationPortCode();				//도착지 항만코드 
//		String destinationPortName 		= exportDto.getDestinationPortName()	== null ? destinationPortName 	= "" : exportDto.getDestinationPortName().toString();	//도착지 이름 
//		int contractAmount 				= exportDto.getContractAmount(); //수출계약 계약금액
		
		String contractDateTime 		= exportDto.getContractDateTime()		== null ? contractDateTime 		= "" : exportDto.getContractDateTime().toString();		//수출계약 계약일자
		String shipmentDueDateTime 		= exportDto.getShipmentDueDateTime()	== null ? shipmentDueDateTime 	= "" : exportDto.getShipmentDueDateTime().toString();	//수출계약 선적기일
		
		if(contractDateTime.equals("") == false) {
			exportDto.setContractDateTime(commonUtil.getDateFormat(contractDateTime,"-") + " 00:00:00");
		}
		if(shipmentDueDateTime.equals("") == false ) {
			exportDto.setShipmentDueDateTime(commonUtil.getDateFormat(shipmentDueDateTime,"-") + " 00:00:00");
		}
		
		
		boardMapper.updateExportContract(exportDto);
		
		resultMap.put("state"		, "SUCCESS");
		resultMap.put("stateDesc"	, "수출계약서류 수정을 완료하였습니다.");
		
		return resultMap;

	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	/*********************************************************************************************************/
	
//	@Override
//	public void deleteBoard(int boardIdx) throws Exception {
//		
//		boardMapper.deleteBoard(boardIdx);
//	}
//	

	/*********************************************************************************************************/
	
}	


