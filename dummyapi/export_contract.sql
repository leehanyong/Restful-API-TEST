-- export_contract(수출계약서류 정보 테이블)
CREATE TABLE `export_contract` (
  `contractIdx` int(11) NOT NULL AUTO_INCREMENT COMMENT '식별번호',
  `exportType` varchar(5) DEFAULT NULL,
  `documentNumber` varchar(64) DEFAULT NULL COMMENT '수출계약 수출서류번호',
  `contractAmount` int(20) DEFAULT NULL COMMENT '수출계약 계약금액',
  `currencyCode` varchar(5) DEFAULT NULL COMMENT '수출계약 계약금액의 통화코드',
  `contractDateTime` datetime DEFAULT NULL COMMENT '수출계약 계약일자',
  `priceTerms` varchar(64) DEFAULT NULL COMMENT '수출계약 가격조건',
  `shipmentDueDateTime` datetime DEFAULT NULL COMMENT '수출계약 선적기일',
  `negoAmount` int(20) DEFAULT NULL COMMENT '매입금액 또는 매입신청금액',
  `balanceAmount` int(20) DEFAULT NULL COMMENT '한도잔액(매입신청가능금액)',
  `exporterName` varchar(64) DEFAULT NULL COMMENT '수출계약 수출자 상호',
  `exporterSsn` varchar(64) DEFAULT NULL COMMENT '수출계약 수출자 사업자번호',
  `exporterAddress` varchar(64) DEFAULT NULL COMMENT '수출계약 수출자 주소',
  `importerName` varchar(64) DEFAULT NULL COMMENT '수출계약 수입자 상호',
  `importerCountryCode` varchar(32) DEFAULT NULL COMMENT '수출계약 수입자 국가코드',
  `importerAddress` varchar(64) DEFAULT NULL COMMENT '수출계약 수입자 주소',
  `shipmentCountryCode` varchar(32) DEFAULT NULL COMMENT '선적지 국가코드',
  `shipmentPortCode` varchar(32) DEFAULT NULL COMMENT '선적지 항만코드',
  `shipmentPortName` varchar(64) DEFAULT NULL COMMENT '선적지 이름',
  `transShipmentCountryCode` varchar(32) DEFAULT NULL COMMENT '환적지 국가코드',
  `transShipmentPortCode` varchar(32) DEFAULT NULL COMMENT '환적지 항만코드',
  `transShipmentPortName` varchar(64) DEFAULT NULL COMMENT '환적지 이름',
  `destinationCountryCode` varchar(32) DEFAULT NULL COMMENT '도착지 국가코드',
  `destinationPortCode` varchar(32) DEFAULT NULL COMMENT '도착지 항만코드',
  `destinationPortName` varchar(64) DEFAULT NULL COMMENT '도착지 이름',
  `regDate` datetime DEFAULT NULL COMMENT '수출계약서류 등록일',
  `fileObjectIdentifier` int(11) DEFAULT '0' COMMENT '파일테이블 식별 키',
  PRIMARY KEY (`contractIdx`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8;




-- export_contract(수출계약서류 정보 테이블) Dummy Data
insert into export_contract (contractIdx, exportType, documentNumber, contractAmount, currencyCode, contractDateTime, priceTerms, shipmentDueDateTime, negoAmount, balanceAmount, exporterName, exporterSsn, exporterAddress, importerName, importerCountryCode, importerAddress, shipmentCountryCode, shipmentPortCode, shipmentPortName, transShipmentCountryCode, transShipmentPortCode, transShipmentPortName, destinationCountryCode, destinationPortCode, destinationPortName, regDate)
values (1, 'A', 'TEST001', 12300, 'ABC', now(), 'priceTerms', now(), 15000, 20000, 'exporterName', 'exporterSsn', 'exporterAddress', 'importerName', 'importerCountryCode', 'importerAddress', 'shipmentCountryCode', 'shipmentPortCode', 'shipmentPortName', 'transShipmentCountryCode', 'transShipmentPortCode', 'transShipmentPortName', 'destinationCountryCode', 'destinationPortCode', 'destinationPortName', now());

-- 파일업로드용 테이블
CREATE TABLE `t_file` (
  `file_idx` int(11) NOT NULL AUTO_INCREMENT COMMENT 'export_contract 테이블 fileObjectIdentifier 식별자 값',
  `fileObjectIdentifier` int(11) NOT NULL COMMENT '수출서류 게시물 업로드 파일 식별 값',
  `original_file_name` varchar(255) NOT NULL COMMENT '원본 파일 이름',
  `stored_file_path` varchar(500) NOT NULL COMMENT '파일 저장 경로',
  `file_size` int(15) unsigned NOT NULL COMMENT '파일 크기',
  `creator_id` varchar(50) NOT NULL COMMENT '작성자 아이디',
  `created_datetime` datetime NOT NULL COMMENT '작성시간',
  `updator_id` varchar(50) DEFAULT NULL COMMENT '수정자 아이디',
  `updated_datetime` datetime DEFAULT NULL COMMENT '수정시간',
  `deleted_yn` char(10) NOT NULL DEFAULT 'N' COMMENT '삭제 여부',
  PRIMARY KEY (`file_idx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- 처리내역 테이블
CREATE TABLE `export_history` (
  `contractIdx` int(11) NOT NULL COMMENT 'export_contract 테이블 일련번호',
  `transactionId` varchar(32) NOT NULL COMMENT '트랜잭션 ID',
  `committer` varchar(32) NOT NULL COMMENT '무역업체 로그인 ID',
  `status` varchar(32) NOT NULL COMMENT '처리상태',
  `updateDateTime` datetime NOT NULL COMMENT '처리일자',
  UNIQUE KEY `uq_transactionId` (`transactionId`),
  KEY `contractIdx` (`contractIdx`),
  CONSTRAINT `export_history_ibfk_1` FOREIGN KEY (`contractIdx`) REFERENCES `export_contract` (`contractIdx`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;