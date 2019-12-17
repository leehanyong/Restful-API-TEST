package dummyapi.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

/*
 * by. 이한용 2019/8/29 Rest API에서 REQUEST METHOD = GET으로 요청을 받아들였을 경우
 * 	+ "검색조건 포함(queryString 예시 : q(검색조건)=documentNumber(수출계약서류번호)=value,exportType(수출유형)=value,startDateTime(등록일자 시작일)=20190101,endDateTime(등록일자 종료일)=20191231"
	+ "&current_page=1(현재요청페이지)"
	+ "&cnt_per_page=10(한 페이지 출력 수)")
 */

public class CommonUtil {

	/****************** 요청request 파싱 공통함수 *********************/
	public Map<String, String> queryToParam(String query)
	{
		Map<String, String> result = new HashMap<String, String>();
	    for (String param : query.split("&")) {
	    	if(param.substring(0,2).equals("q=")==true) {
	    		param = param.substring(2);
	    		result.put("q", param.toString());
	    	} else {
	    		String pair[] = param.split("=");
		        if (pair.length>1) {
		            result.put(pair[0], pair[1]);
		        }else{
		            result.put(pair[0], "");
		        }	
	    	}
	    }
	    return result;
	}
	
	public Map<String, String> queryToMap(String query)
	{
	    Map<String, String> result = new HashMap<String, String>();
	    for (String param : query.split("&")) {
	        String pair[] = param.split("=");
	        if (pair.length>1) {
	            result.put(pair[0], pair[1]);
	        }else{
	            result.put(pair[0], "");
	        }
	    }
	    return result;
	}
	
	public Map<String, String> detailConditionToParam(String val)
	{
	    Map<String, String> result = new HashMap<String, String>();
	    for (String param : val.split(",")) {
	        String pair[] = param.split("=");
	        if (pair.length>1) {
	            result.put(pair[0], pair[1]);
	        }else{
	            result.put(pair[0], "");
	        }
	    }
	    return result;
	}
	/****************** 요청request 파싱 공통함수 *********************/

	/*
	 * 날짜 검색조건 파싱 공통함수
	 */
	public String getDateFormat(String dateVal, String format) {

        String result = dateVal;
        if(result.length() ==8){
            String year = result.substring(0, 4) ;
            String month = result.substring(4, 6) ;
            String date = result.substring(6, 8) ;
            result = year + format + month + format + date;
        }else if(result.length()>8){

        	result = result.replaceAll("/", "");
        	result = result.replaceAll("-", "");
            String year = result.substring(0, 4) ;
            String month = result.substring(4, 6) ;
            String date = result.substring(6, 8) ;
            result = year + format + month + format + date;
        }
        return result ;
    }
	
	
	public JSONObject getJSONObject(Object f_Object)throws Exception{

		BinaryConvert binaryConvert = null; 
		
	  	JSONObject jSONObject = new JSONObject();

		@SuppressWarnings("rawtypes")
		Class ClassObject = f_Object.getClass();
		String ClassName = ClassObject.getCanonicalName();
		ClassName = ClassName+".";
		Method[] member_Method = ClassObject.getMethods();

		int i=0;		int index = 0;		int getterCnt = 0;		int getterIndex = 0;
		
		String[] FieldType = null;
		String[] FieldName = null;
		Method[] getterMethod = null;
		
		for(i=0;i<member_Method.length;i++){
			String[] temp_member_Method_Data = member_Method[i].toString().split(" ");
			temp_member_Method_Data[2] = temp_member_Method_Data[2].replaceAll(ClassName, "");

			index = temp_member_Method_Data[2].indexOf("(");
			if(index > 0){
				temp_member_Method_Data[2] = temp_member_Method_Data[2].substring(0,index);
			}

			//getter의 수
			if(temp_member_Method_Data[2].substring(0,3).equals("get") == true ){
				getterCnt++;
			}

		}
		getterMethod = new Method[getterCnt];
		FieldType = new String[getterCnt];
		FieldName = new String[getterCnt];

		getterIndex = 0;
		for(i=0;i<member_Method.length;i++){
			String[] temp_member_Method_Data = member_Method[i].toString().split(" ");
			//[0] [public , protected ,private]
			//[1] return type
			//[2] method name
			temp_member_Method_Data[2] = temp_member_Method_Data[2].replaceAll(ClassName, "");
			index = temp_member_Method_Data[2].indexOf("(");
			if(index > 0){
				temp_member_Method_Data[2] = temp_member_Method_Data[2].substring(0,index);
			}

			//getter 저장
			if(temp_member_Method_Data[2].substring(0,3).equals("get") == true ){
				getterMethod[getterIndex] = member_Method[i];
				FieldName[getterIndex]           = temp_member_Method_Data[2].substring(3);

				if(FieldName[getterIndex].length()>=1){
				    FieldName[getterIndex] = FieldName[getterIndex].substring(0, 1).toLowerCase()+FieldName[getterIndex].substring(1);
				}

				FieldType[getterIndex]           = temp_member_Method_Data[1];
				getterIndex++;
			}
		}

		for(i=0;i<getterCnt;i++){
			if(getterMethod[i].invoke(f_Object)==null){
				jSONObject.put(FieldName[i], "");
			}else{

			    if(FieldType[i].equals("byte[]") == true){
	                byte []temp = (byte[]) getterMethod[i].invoke(f_Object);
	                String byteString = binaryConvert.convertStringToBinary(temp);
	                jSONObject.put(FieldName[i], ""+byteString.trim());
	            }else{
	            	jSONObject.put(FieldName[i], ""+getterMethod[i].invoke(f_Object));
	            	
	            }
			}
		}

		return jSONObject;
	}
	
}