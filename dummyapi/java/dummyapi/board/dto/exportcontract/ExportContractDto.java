package dummyapi.board.dto.exportcontract;

import java.util.List;

import dummyapi.board.dto.BoardFileDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="ExportContractDto", description = "수출계약서류 내용")
@Data
public class ExportContractDto {
	
	/******** 수출서류조회 검색조건 ********/
	@ApiModelProperty(value = "등록일자 시작 일자")
	private String startDateTime;
	@ApiModelProperty(value = "등록일자 끝 일자")
	private String endDateTime;
	/********************************/
	
	/******** 수출서류조회 검색조건 ********/
	@ApiModelProperty(value = "라인 번호")
	private int rnum;
	
	@ApiModelProperty(value = "수출서류 식별 값")
	private int contractIdx;
	
	@ApiModelProperty(value = "수출유형코드")
	private String exportType;
	
	@ApiModelProperty(value = "수출계약 수출서류번호")
	private String documentNumber;
	
	@ApiModelProperty(value = "수출계약 계약금액")
	private int contractAmount;
	
	@ApiModelProperty(value = "수출계약 계약금액의 통화코드")
	private String currencyCode;
	
	@ApiModelProperty(value = "수출계약 계약일자")
	private String contractDateTime;
	
	@ApiModelProperty(value = "수출계약 가격조건")
	private String priceTerms;
	
	@ApiModelProperty(value = "수출계약 선적기일")
	private String shipmentDueDateTime;
	
	@ApiModelProperty(value = "매입금액 또는 매입신청금액")
	private int negoAmount;
	
	@ApiModelProperty(value = "한도잔액(매입신청가능금액)")
	private int balanceAmount;
	
	@ApiModelProperty(value = "수출계약 수출자 상호")
	private String exporterName;
	
	@ApiModelProperty(value = "수출계약 수출자 사업자번호")
	private String exporterSsn;
	
	@ApiModelProperty(value = "수출계약 수출자 주소")
	private String exporterAddress;
	
	@ApiModelProperty(value = "수출계약 수입자 상호")
	private String importerName;
	
	@ApiModelProperty(value = "수출계약 수입자 국가코드")
	private String importerCountryCode;
	
	@ApiModelProperty(value = "수출계약 수입자 주소")
	private String importerAddress;
	
	@ApiModelProperty(value = "선적지 국가코드")
	private String shipmentCountryCode;
	
	@ApiModelProperty(value = "선적지 항만코드")
	private String shipmentPortCode;
	
	@ApiModelProperty(value = "선적지 이름")
	private String shipmentPortName;
	
	@ApiModelProperty(value = "환적지 국가코드")
	private String transShipmentCountryCode;
	
	@ApiModelProperty(value = "환적지 항만코드")
	private String transShipmentPortCode;
	
	@ApiModelProperty(value = "환적지 이름")
	private String transShipmentPortName;
	
	@ApiModelProperty(value = "도착지 국가코드")
	private String destinationCountryCode;
	
	@ApiModelProperty(value = "도착지 항만코드")
	private String destinationPortCode;
	
	@ApiModelProperty(value = "도착지 이름")
	private String destinationPortName;
	
	@ApiModelProperty(value = "수출서류 데이터 등록일자")
	private String regDate;
	
	@ApiModelProperty(value = "수출서류 처리상태")
	private String status;
	
	
	@ApiModelProperty(value = "첨부파일 리스트")
	private List<BoardFileDto> fileList;
	
	@ApiModelProperty(value = "파일리스트 식별 키")
	private int fileObjectIdentifier;
	/********************************/	
	
}
