package dummyapi.board.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="BoardFileDto", description = "파일 객체")
@Data
public class BoardFileDto {
	
	@ApiModelProperty(value = "파일 식별 값")
	private int fileIdx;
	
	@ApiModelProperty(value = "수출서류 정보 테이블과 파일리스트의 매핑 식별 값")
	private int fileObjectIdentifier;
	
	@ApiModelProperty(value = "수출서류 번호")
	private String documentNumber;
	
	@ApiModelProperty(value = "본 파일 명")
	private String originalFileName;
	
	@ApiModelProperty(value = "업로드 파일 저장경로")
	private String storedFilePath;
	
	@ApiModelProperty(value = "파일 사이즈")
	private long fileSize;
}
