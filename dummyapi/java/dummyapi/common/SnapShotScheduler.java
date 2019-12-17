package dummyapi.common;

import java.io.BufferedReader;
//import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


/*
 * by. 이한용 2019/10/29 스케줄러 Date 설정에 따라 특정 데이터를 DB에 저장  
 * 1. 일정주기별로 API를 호출하여 결과 값을 요청
 * 2. 리턴된 요청 결과 값이 DB에 특정 식별자 값으로 존재하는 지 판단
 * 3. 존재하지 않는다면 insert
 * @Scheduled(cron = "0(초) 0/30(시간/분단위) *(일) *(주)  *(월)  *(년)"
 */

@Slf4j
@Component
public class SnapShotScheduler {

//	@Value("${RestApiServerPath}")
//	private String serverPath;
//	
//	@Value("${RestApiExportContract}")
//	private String apiExport;
//	
//	@Value("${RestApiFile}")
//	private String apiFile;
//	
//	@Value("${RestApiTransaction}")
//	private String apiTransaction;
//	
//	@Autowired
//	private AdminService adminService;
	
	public JSONObject stringToJSONObject(StringBuffer sf) {
		
		JSONParser jsonParse = new JSONParser();
		JSONObject jsonObj = new JSONObject();
		
		try {
			jsonObj = (JSONObject)jsonParse.parse(sf.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObj;
	}
	
	public JSONArray stringToJSONArray(StringBuffer sf) {
		
		JSONParser jsonParse = new JSONParser();
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {
			jsonObj = (JSONObject)jsonParse.parse(sf.toString());
//			jsonArray = (JSONArray)jsonObj.get	
			//array로 파싱하기 위해서는 최상의 키 값이 있어야함. 추후에 정리
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return jsonArray;
	}


	public String dateTime(String dateStr) {
		
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date newDate = new Date();
		
		try {
			
			newDate = format.parse(dateStr);
			
			return newFormat.format(newDate);
			
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
		
	}

	@Scheduled(cron = "0 0/30 * * * *")
	public void run() {
		log.info("************BEGIN - Block/Transaction Search Scheduler************");
		
		loopCallBlockUtil("");
		
		log.info("************END - Block/Transaction Search Scheduler************");
	}
	

	public void loopCallBlockUtil(String blockNum) {
		
		StringBuffer sf = new StringBuffer();
		JSONObject jsonObj = new JSONObject();
		
		try {
			
			sf = callBlockInfo(blockNum);
			
			if(sf.length() > 0) {
				jsonObj = stringToJSONObject(sf);
			}
			
			if(jsonObj.isEmpty()==false) {

				//1. 가장 최근에 생성된 Block Data 조회가 성공일때
				if(jsonObj.get("resultCode").toString().equals("200")) {
					
					//2. 가장 최근에 생성된 Block Data JSON Object 생성
					JSONObject resultData = (JSONObject)jsonObj.get("resultData");
					
					//3. Block Data에서 블록정보 조회 
					//previousBlockHash	: 조회된 앞 노드 해쉬 값
					//currentBlockHash	: 조회된 블록 해쉬 값
					//currentBlockHeight: 조회된 블록 번호
					String previousBlockHash 	= resultData.containsKey("previousBlockHash") 	== true ? previousBlockHash	= resultData.get("previousBlockHash").toString() 	: "";
					String currentBlockHash 	= resultData.containsKey("currentBlockHash") 	== true ? currentBlockHash	= resultData.get("currentBlockHash").toString() 	: "";
					String currentBlockHeight 	= resultData.containsKey("currentBlockHeight") 	== true ? currentBlockHeight= resultData.get("currentBlockHeight").toString() 	: "";
					
					log.info("previousBlockHash : " + previousBlockHash);
					log.info("currentBlockHash : " + currentBlockHash);
					log.info("currentBlockHeight : " + currentBlockHeight);

					if(currentBlockHeight.equals("")==true) {
						return;
					} 
					
//					Map<String, Object> blockYn = adminService.checkBlockData(currentBlockHeight);
					Map<String, Object> blockYn = new HashMap<String, Object>();
					if(blockYn.get("state").equals("WARNING")==true) {
						//내부 DB에 해당 블록정보가 존재함.
						return;
						
					} else {
						
						//블록체인 서버에는 블록이 존재하나 내부 DB에 존재하지 않을 때 추가하기 위한 로직
						//4. 상세 Block Data 조회 
						JSONObject currentBlockInfo = (JSONObject)resultData.get("currentBlockInfo");
												
						/*************************** transactionInfo ***********************************/
						//5. Block의 Transaction List길이 생성
						String transactionCount	= currentBlockInfo.containsKey("transactionCount") 	== true ? transactionCount = currentBlockInfo.get("transactionCount").toString(): "";
						//6. Transaction List가 존재할 경우 List생성 객체 초기화
						JSONArray transactionInfoList = new JSONArray();
						//7. Transaction List의 길이가 0이 아니면 JsonArray 생성
						if(Integer.parseInt(transactionCount) > 0) {
							transactionInfoList = (JSONArray)currentBlockInfo.get("transactionInfo");
							log.info("transactionInfoList : " + transactionInfoList.toString());
							
							for(int i=0; i < transactionInfoList.size(); i++) {
								
								JSONObject transactionInfo = (JSONObject)transactionInfoList.get(i);
								log.info("transactionInfo : " + transactionInfo.toString());	
								
								String transactionID 		= transactionInfo.containsKey("transactionID") 			== true ? transactionID			= transactionInfo.get("transactionID").toString() 		: "";
								String transactionCreatorID = transactionInfo.containsKey("transactionCreatorID") 	== true ? transactionCreatorID	= transactionInfo.get("transactionCreatorID").toString(): "";
								String transactionTime 		= transactionInfo.containsKey("transactionTime") 		== true ? transactionTime 		= transactionInfo.get("transactionTime").toString() 	: "";
								String transactionNumber 	= transactionInfo.containsKey("transactionNumber") 		== true ? transactionNumber 	= transactionInfo.get("transactionNumber").toString() 	: "";
								
								log.info("transactionID : " 		+ transactionID);
								log.info("transactionCreatorID : " 	+ transactionCreatorID);
								log.info("transactionTime : " 		+ transactionTime);
								log.info("transactionNumber : " 	+ transactionNumber);
								
								String newDate = dateTime(transactionTime);
								log.info("newDate : " + newDate);
								
								
								/*************************** responsePayload ***********************************/
								//8. Block의 responsePayload 생성
								StringBuffer payLoadBuffer = new StringBuffer(); 
								JSONObject responsePayload = stringToJSONObject(payLoadBuffer.append(transactionInfo.get("responsePayload").toString()));
								log.info("responsePayload : " + responsePayload.toString());
								/******************************************************************************/
								
								
								/*************************** endorsementInfo ***********************************/
								//9. Block의 endorsementInfo List길이 생성
								String endorsementCount	= transactionInfo.containsKey("endorsementCount") 	== true ? endorsementCount = transactionInfo.get("endorsementCount").toString(): "";
								//10. endorsementInfo List가 존재할 경우 List생성 객체 초기화
								JSONArray endorsementInfo = new JSONArray();
								//11. endorsementInfo List의 길이가 0이 아니면 JsonArray 생성
								if(Integer.parseInt(endorsementCount) > 0) {
									endorsementInfo = (JSONArray)transactionInfo.get("endorsementInfo");
									log.info("endorsementInfo : " + endorsementInfo.toString());
								}
								/******************************************************************************/
								
								
								
								/*************************** chaincodeInputArgs ***********************************/
								//12. endorsementInfo List가 존재할 경우 List생성 객체 초기화
								JSONArray chaincodeInputArgs = (JSONArray)transactionInfo.get("chaincodeInputArgs");

								log.info("chaincodeInputArgs : " + chaincodeInputArgs.toString());
								/******************************************************************************/
								
								
								
								/******************************** readSet ***********************************/
								//13. readSet List가 존재할 경우 List생성 객체 초기화
								JSONArray readSet = (JSONArray)transactionInfo.get("readSet");

								log.info("readSet : " + readSet.toString());
								/******************************************************************************/
								
								/******************************** readSet ***********************************/
								//14. readSet List가 존재할 경우 List생성 객체 초기화
								JSONArray writeSet = (JSONArray)transactionInfo.get("writeSet");
								
								JSONObject writeSetJson = (JSONObject)writeSet.get(0);
								
								String[] writeTypeArray = writeSetJson.get("key").toString().split("\\^");
								String transactionType = writeTypeArray[0].toString();
								
								log.info("writeType : " + writeTypeArray[0].toString());
								log.info("writeSet : " + writeSet.toString());
								/******************************************************************************/

								Map<String, Object> param = new HashMap<String, Object>();
								
								param.put("blockNo"				, currentBlockHeight);
								param.put("txId"				, transactionID);
								param.put("previousBlockHash"	, previousBlockHash);
								param.put("currentBlockHash"	, currentBlockHash);
								
								param.put("transactionCreatorID", transactionCreatorID);
								param.put("transactionTime"		, newDate);
								param.put("transactionNumber"	, transactionNumber);
								param.put("transactionInfo"		, transactionInfo.toString());
								param.put("responsePayload"		, responsePayload.toString());
								
								
								param.put("endorsementCount"	, endorsementCount);
								param.put("endorsementInfo"		, endorsementInfo.toString());
								
								param.put("chaincodeInputArgs"	, chaincodeInputArgs.toString());
								param.put("type"				, transactionType.toString());
								param.put("readSet"				, readSet.toString());
								param.put("writeSet"			, writeSet.toString());
								
								
//								Map<String, Object> insertBlockYn = adminService.insertBlockData(param);
								
								
								
							}
							
						}
						/******************************************************************************/
						if(currentBlockHeight.equals("0")) {
							return;
						}
						loopCallBlockUtil(Integer.toString((Integer.parseInt(currentBlockHeight)-1)));
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

	public StringBuffer callBlockInfo(String blockNo) {
		
		String channelName 	= "bank-sc";
		String apiBlock = "api/v1/blocks";
		
//		String apiURL = serverPath+apiBlock+"?channelName="+channelName;
		String apiURL = "";
		StringBuffer sb = new StringBuffer();
		if(blockNo.equals("")==false) {
			sb.setLength(0);
			sb.append("&blockNumber=").append(blockNo);
			apiURL = apiURL.concat(sb.toString());
		}
		
		log.info(apiURL);
		
		StringBuffer response = new StringBuffer();      
		
		try {
			URL url = new URL(apiURL);
	
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("Content-Type", "application/json");
	        con.setDoOutput(true);
	
	        int responseCode = con.getResponseCode();
	        
	        BufferedReader br;
	        
	        if(responseCode>=200 && responseCode<=300) { // 정상 호출
	            br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
	        } else {  // 에러 발생
	            br = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8));
	        }
	        
	        String inputLine;
	        
	        while ((inputLine = br.readLine()) != null) {
	            response.append(inputLine);
	            System.out.println(response.toString());
	        }
	        br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return response;
		
	}
}
