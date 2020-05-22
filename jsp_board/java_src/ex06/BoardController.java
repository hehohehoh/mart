package ex06;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;


@WebServlet("/board04/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	BoardService bs;
	ArticleVO vo;
	HttpSession session;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		bs = new BoardService();
		vo = new ArticleVO();
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		String nextPage = "";
		String action = request.getPathInfo();
		int articleNO;
		
		try {
			
			if(action == null || action.equals("/")) {
			
				String _section = request.getParameter("section");
				String _pageNum = request.getParameter("pageNum");
				int section = Integer.parseInt( ((_section == null)? "1" : _section));
				int pageNum = Integer.parseInt( ((_pageNum == null)? "1" : _pageNum));
				
				Map<String, Integer> pagingMap = new HashMap<String, Integer>();
				pagingMap.put("section", section);
				pagingMap.put("pageNum", pageNum);
				
				Map<String, Object> articlesMap = bs.listArticles(pagingMap);
				articlesMap.put("section", section);
				articlesMap.put("pageNum", pageNum);
				
				request.setAttribute("articlesMap", articlesMap);
				nextPage = "/board_jsp/listArticles.jsp";
				
			}else if(action.equals("/listArticles.do")) {
				String _section = request.getParameter("section");
				String _pageNum = request.getParameter("pageNum");
				int section = Integer.parseInt( ((_section == null)? "1" : _section));
				int pageNum = Integer.parseInt( ((_pageNum == null)? "1" : _pageNum));
				
				Map<String, Integer> pagingMap = new HashMap<String, Integer>();
				pagingMap.put("section", section);
				pagingMap.put("pageNum", pageNum);
				
				Map<String, Object> articlesMap = bs.listArticles(pagingMap);
				articlesMap.put("section", section);
				articlesMap.put("pageNum", pageNum);
				
				request.setAttribute("articlesMap", articlesMap);
				nextPage = "/board_jsp/listArticles.jsp";
				
			}else if(action.equals("/articleForm.do")) {
				nextPage = "/board_jsp/articleForm.jsp";
				
			}else if(action.equals("/addArticle.do")) {
				
				articleNO = 0;
				Map<String, String> articleMap = upload(request, response);
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				
				vo.setParentNO(0);
				vo.setTitle(title);
				vo.setContent(content);
				vo.setImageFileName(imageFileName);
				vo.setId("hong");
				
				// addArticle return articleNo
				articleNO = bs.addArticle(vo);
				
				if(imageFileName != null && imageFileName.length() != 0) {
					
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
				}
				
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "  alert('새 글을 추가했습니다.');" 
									+ "	 location.href='/pro17/board04/listArticles.do';	" +
						"</script>");
				
				bs.close();
				return;
				
			}else if(action.equals("/viewArticle.do")) {
				articleNO = Integer.parseInt(request.getParameter("articleNO"));
				vo = bs.viewArticle(articleNO);
				request.setAttribute("article", vo);
				nextPage = "/board_jsp/viewArticle.jsp";
				
			}else if(action.equals("/modArticle.do")) {
				Map<String, String> articleMap = upload(request, response);
				
				articleNO = Integer.parseInt(articleMap.get("articleNO"));
				vo.setArticleNO(articleNO);
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				
				vo.setParentNO(0);
				vo.setId("hong");
				vo.setTitle(title);
				vo.setContent(content);
				vo.setImageFileName(imageFileName);
				
				bs.modArticle(vo);
				if(imageFileName != null && imageFileName != "") {
					String originalFileName = articleMap.get("originalFileName");
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName );
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					
					File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + originalFileName);
					oldFile.delete();
				}
				
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "  alert('글이 수정 되었습니다.');" 
									+ "	 location.href='/pro17/board04/"
									+ "viewArticle.do?articleNO=" + articleNO + "';" +
						"</script>");
				
				return;
				
			}else if(action.equals("/removeArticle.do")) {
				
				articleNO = Integer.parseInt(request.getParameter("articleNO"));
				List<Integer> anoList = bs.removeArticle(articleNO);
				
				for(int ano : anoList) {
					File imgDir = new File(ARTICLE_IMAGE_REPO + "\\" + ano);
					if(imgDir.exists())
						FileUtils.deleteDirectory(imgDir);
				}
				
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "  alert('글이 삭제 되었습니다.');" 
									+ "	 location.href='/pro17/board04/listArticles.do';"
						+"</script>");
				
				return;
				
				
			}else if(action.equals("/replyForm.do")) {
				
				int parentNO = Integer.parseInt(request.getParameter("parentNO"));
				session = request.getSession();
				session.setAttribute("parentNO", parentNO);
				nextPage = "/board_jsp/replyForm.jsp";
				
			}else if(action.equals("/addReply.do")) {
				
				session = request.getSession();
				int parentNO = (Integer) session.getAttribute("parentNO");
				session.removeAttribute("parentNO");
				
				Map<String, String> articleMap = upload(request, response);
				String title = articleMap.get("title");
				String content = articleMap.get("content");
				String imageFileName = articleMap.get("imageFileName");
				
				vo.setParentNO(parentNO);
				vo.setId("lee");
				vo.setTitle(title);
				vo.setContent(content);
				vo.setImageFileName(imageFileName);
				
				articleNO = bs.addReply(vo);
				if(imageFileName != null && imageFileName.length() !=0) {
					
					File srcFile= new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
					File destDir= new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					
				}
				
				PrintWriter pw = response.getWriter();
				pw.print("<script>" + "  alert('답글이 추가 되었습니다.');" 
									+ "	 location.href='/pro17/board04/viewArticle.do?"
									+ "articleNO="+ articleNO + "';"
						+"</script>");
				
				return;
			}
			
			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map<String, String> map = new HashMap<String, String>();
		String encoding = "utf-8";
		File currentDirPath = new File(ARTICLE_IMAGE_REPO);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024 * 1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			List<FileItem> items = upload.parseRequest(request);
			
			for(int i=0; i<items.size(); i++) {
				FileItem item = (FileItem) items.get(i);
				if(item.isFormField()){
					map.put(item.getFieldName(), item.getString(encoding));
				}
				else {
				
					if(item.getSize()>0) {
						int idx = item.getName().lastIndexOf("\\");
						if(idx == -1)
							idx = item.getName().lastIndexOf("/");
						
						String fileName = item.getName().substring(idx+1);
						map.put(item.getFieldName(), fileName);
						File uploadFile = new File(currentDirPath + "\\temp\\" + fileName);
						File temp = new File(currentDirPath + "\\temp");
						if(!temp.exists()) {
							temp.mkdirs();
							item.write(uploadFile);
						}
						else
							item.write(uploadFile);
							
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
		
	}



}
