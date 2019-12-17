package dummyapi.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import dummyapi.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
//	@RequestMapping("/board/openBoardList.do")
//	public ModelAndView openBoardList() throws Exception{
//		
//		log.debug("openBoardList");
//		
//		ModelAndView mv = new ModelAndView("board/boardList");
//		
//		List<BoardDto> list = boardService.selectBoardList();
//		mv.addObject("list", list);
//		
//		return mv;
//	}
//	
//	@RequestMapping(value = "/board/openBoardWrite.do", method = {RequestMethod.POST, RequestMethod.GET})
//	public String openBoardWrite() throws Exception{
//		
//		return "board/boardWrite";
//	}
//	
//	@RequestMapping(value = "/board/insertBoard.do", method = {RequestMethod.POST})
//	public String insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
//		
//		boardService.insertBoard(board, multipartHttpServletRequest);
//		
//		return "redirect:/board/openBoardList.do";
//	}
//	
//	@RequestMapping("/board/openBoardDetail.do")
//	public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception{
//		
//		ModelAndView mv = new ModelAndView("board/boardDetail");
//		
//		BoardDto board = boardService.selectBoardDetail(boardIdx);
//		mv.addObject("board", board);
//		
//		return mv;
//	}
//	
//	@RequestMapping("/board/updateBoard.do")
//	public String updateBoard(BoardDto board) throws Exception{
//		
//		boardService.updateBoard(board);
//		return "redirect:/board/openBoardList.do";
//	}
//	
//	@RequestMapping("/board/deleteBoard.do")
//	public String deleteBoard(int boardIdx) throws Exception{
//		
//		boardService.deleteBoard(boardIdx);
//		return "redirect:/board/openBoardList.do";
//	}
//	
//	@RequestMapping("/board/downloadBoardFile.do")
//	public void downloadBoardFile(@RequestParam int idx, @RequestParam int boardIdx, HttpServletResponse response) throws Exception{
//		
//		BoardFileDto boardFile = boardService.selectBoardFileInformation(idx, boardIdx);
//		if(ObjectUtils.isEmpty(boardFile) == false) {
//			String fileName = boardFile.getOriginalFileName();
//			
//			byte[] files = FileUtils.readFileToByteArray(new File(boardFile.getStoredFilePath()));
//			
//			response.setContentType("application/octet-stream");
//			response.setContentLength(files.length);
//			response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName,"UTF-8")+"\";");
//			response.setHeader("Content-Transfer-Encoding", "binary");
//			
//			response.getOutputStream().write(files);
//			response.getOutputStream().flush();
//			response.getOutputStream().close();
//		}
//	}
	
}
