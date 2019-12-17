package dummyapi.board.dto.exportcontract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="ExportContractHistoryDto", description = "수출계약서류 처리내역")
@Data
public class ExportContractHistoryDto {
	
	/******** 수출서류 처리내역 ********/
	@ApiModelProperty(value = "수출서류 식별 값")
	private int contractIdx;
	
	@ApiModelProperty(value = "트랜잭션 ID")
	private String transactionId;
	
	@ApiModelProperty(value = "무역업체 로그인 ID")
	private String committer;
	
	@ApiModelProperty(value = "처리상태")
	private String status;
	
	@ApiModelProperty(value = "처리일자")
	private String updateDateTime;
	/********************************/	
	
}
