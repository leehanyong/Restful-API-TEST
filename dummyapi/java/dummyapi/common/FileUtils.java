package dummyapi.common;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import dummyapi.board.dto.BoardFileDto;
import dummyapi.board.mapper.BoardMapper;

@Component
public class FileUtils {
	
	@Autowired
	private BoardMapper boardMapper;
	
	public List<BoardFileDto> parseFileInfo(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
		
		if(ObjectUtils.isEmpty(multipartHttpServletRequest)){
			return null;
		}
		
		List<BoardFileDto> fileList = new ArrayList<>();
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd"); 
    	ZonedDateTime current = ZonedDateTime.now();
    	String path = "images/"+current.format(format);
    	File file = new File(path);
    	
		if(file.exists() == false){
			
			file.mkdirs();
		}
		
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
		
		String newFileName, originalFileExtension, contentType;
		
		int idx = boardMapper.getfileIdx()+1;
		
		while(iterator.hasNext()){
			
			List<MultipartFile> list = multipartHttpServletRequest.getFiles(iterator.next());
			
			for (MultipartFile multipartFile : list){
				
				if(multipartFile.isEmpty() == false){
					
					contentType = multipartFile.getContentType();
					
					if(ObjectUtils.isEmpty(contentType)){
						
						break;
					}
					else{
						
						String orgFileName = multipartFile.getOriginalFilename();
						
						originalFileExtension = orgFileName.substring(orgFileName.lastIndexOf("."));
						
					}
					
					newFileName = Long.toString(System.nanoTime()) + originalFileExtension;
					BoardFileDto boardFile = new BoardFileDto();
					boardFile.setFileObjectIdentifier(idx);
					boardFile.setFileSize(multipartFile.getSize());
					boardFile.setOriginalFileName(multipartFile.getOriginalFilename());
					boardFile.setStoredFilePath(path + "/" + newFileName);
					fileList.add(boardFile);
					
					file = new File(path + "/" + newFileName);
					multipartFile.transferTo(file);
				}
			}
		}
		
		return fileList;
	}
	
	
}
