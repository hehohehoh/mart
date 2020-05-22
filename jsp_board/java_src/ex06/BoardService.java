package ex06;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardService {

	BoardDAO dao;
	
	public BoardService(){
		dao = new BoardDAO();
	}
	
	public List<ArticleVO> listArticles(){
		
		List<ArticleVO> articlesList = dao.selectAllArticles();
		return articlesList;
	}
	
	public Map<String, Object> listArticles(Map<String, Integer> pagingMap) {
		Map<String, Object> articlesMap = new HashMap<String, Object>();
		List<ArticleVO> alist = dao.selectAllArticles(pagingMap);
		
		int totArticles = dao.selectToArticles();
		articlesMap.put("articlesList", alist);
		articlesMap.put("totArticles", totArticles);
		
		return articlesMap;
	}
	
	public int addArticle(ArticleVO vo) {
		return dao.insertNewArticle(vo);
	}
	
	public ArticleVO viewArticle(int articleNO) {
		ArticleVO vo = null;
		vo = dao.selectArticle(articleNO);
		return vo;
	}
	
	public void modArticle(ArticleVO vo) {
		dao.updateArticle(vo);
	}
	
	public List<Integer> removeArticle(int ano){
		List<Integer> anoList = dao.selectRemovedArticles(ano);
		dao.deleteArticle(ano);
				
		return anoList;
	}
	
	public int addReply(ArticleVO vo) {
		return dao.insertNewArticle(vo);
	}
	public void close() {
		dao.close();
	}
}
