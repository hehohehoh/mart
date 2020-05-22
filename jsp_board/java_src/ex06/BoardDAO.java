package ex06;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	
	private DataSource dataFactory;
	private Connection conn;
	private PreparedStatement ps;
	
	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle_hr");
			conn = dataFactory.getConnection();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<ArticleVO> selectAllArticles(){
		List<ArticleVO> articlesList = new ArrayList<ArticleVO>();
		
		try {
			String sql = "select level, articleNO, "
					+ "parentNO, title, content, id, writeDate  "
					+ "from t_board "
					+ "start with parentNO=0 "
					+ "connect by prior articleNO=parentNO "
					+ "order siblings by articleNO desc ";
			
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int level = rs.getInt(1);
				int articleNO = rs.getInt(2);
				int parentNO = rs.getInt(3);
				String title = rs.getString(4);
				String content = rs.getString(5);
				String id = rs.getString(6);
				Timestamp writeDate = rs.getTimestamp(7);
				
				ArticleVO vo = new ArticleVO(level, articleNO, parentNO, 
											title, content, id, writeDate);
				
				articlesList.add(vo);
			}
			
			rs.close();
			ps.close();
					
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return articlesList;
	}
	
	public List<ArticleVO> selectAllArticles(Map<String, Integer> pagingMap) {
		
		List<ArticleVO> alist = new ArrayList<ArticleVO>();
		
		int section = (Integer) pagingMap.get("section");
		int pageNum = (Integer) pagingMap.get("pageNum");
		
		try {
			String sql = "select * from ( " + "select ROWNUM as recNum, "
					+ "LVL, articleNO, parentNO, title, content, "
					+ "id, imageFileName, writeDate "
					+ "from ( select LEVEL as LVL, articleNO, parentNO, "
					+ "title, content, id, imageFileName, writeDate "
					+ "from t_board "
					+ "start with parentNO=0 "
					+ "connect by prior articleNO=parentNO "
					+ "order siblings by articleNO desc )"+ ") "
					+ "where recNum between "
					+ " (?-1)*100 + (?-1)*10 + 1 and (?-1)*100 + ?*10";
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, section);
			ps.setInt(2, pageNum);
			ps.setInt(3, section);
			ps.setInt(4, pageNum);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int level = rs.getInt(2);
				int ano = rs.getInt(3);
				int pno = rs.getInt(4);
				String title = rs.getString(5);
				String content = rs.getString(6);
				String id = rs.getString(7);
				String fname = rs.getString(8);
				Timestamp wd = rs.getTimestamp(9);
				
				ArticleVO vo = new ArticleVO(level, ano, pno, title, content,
						id, fname, wd);
				
				alist.add(vo);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return alist;
	}
	
	public int selectToArticles() {
		int result=0;
		
		try {
			String sql = "select count(articleNO) from t_board";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
				result = rs.getInt(1);
			else
				result = 0;
			
			rs.close();
			ps.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	public int insertNewArticle(ArticleVO vo) {
		
		int ano=0;
		try {
			int parentNO = vo.getParentNO();
			String title = vo.getTitle();
			String content = vo.getContent();
			String imageFileName = vo.getImageFileName();
			String id = vo.getId();
			
			String sql = "insert into t_board values(ano_seq.nextval, ?, ?, ?, ?, default, ?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, parentNO);
			ps.setString(2, title);
			ps.setString(3, content);
			ps.setString(4, imageFileName);
			ps.setString(5, id);
			
			ps.executeUpdate();
			
			String sql2 = "select ano_seq.currval from dual";
			ps = conn.prepareStatement(sql2);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
				ano = rs.getInt(1);
			else
				ano = 0;
			
			rs.close();
			ps.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return ano;
	}
	
	public ArticleVO selectArticle(int articleNO) {
		
		ArticleVO vo = new ArticleVO();
		
		try {
			String sql = "select articleNO, parentNO, title, content, "
					+ "imageFileName, id, writeDate from t_board "
					+ "where articleNO = ?";
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, articleNO);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int ano = rs.getInt(1);
			int pno = rs.getInt(2);
			String title = rs.getString(3);
			String content = rs.getString(4);
			String imageFileName = rs.getString(5);
			String id = rs.getString(6);
			Timestamp writeDate = rs.getTimestamp(7);
			
			vo.setArticleNO(ano);
			vo.setParentNO(pno);
			vo.setTitle(title);
			vo.setContent(content);
			vo.setImageFileName(imageFileName);
			vo.setId(id);
			vo.setWriteDate(writeDate);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return vo;
	}
	
	public void updateArticle(ArticleVO vo) {
		
		int articleNO = vo.getArticleNO();
		String title = vo.getTitle();
		String content = vo.getContent();
		String imageFileName = vo.getImageFileName();
		try {
			String sql = "update t_board set title=?, content=? ";
			if(imageFileName != null && imageFileName != "")
				sql = sql + ", imageFileName=? ";
			
			sql = sql + " where articleNO=? ";
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, title);
			ps.setString(2, content);
			if(imageFileName != null && imageFileName != "") {
				ps.setString(3, imageFileName);
				ps.setInt(4, articleNO);
			}else {
				ps.setInt(3, articleNO);
			}
				
			ps.executeUpdate();
			ps.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Integer> selectRemovedArticles(int ano){
		List<Integer> anoList = new ArrayList<Integer>();
		int temp;
		
		try {
			String sql = "select articleNO from t_board start with articleNO = ? "
					+ "connect by prior articleNO = parentNO";
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, ano);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				temp = rs.getInt(1);
				anoList.add(temp);
			}
			
			ps.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return anoList;
	}
	
	public void deleteArticle(int ano) {
		try {
			String sql = "delete from t_board where articleNO in ( "
					+ "select articleNO from t_board "
					+ "start with articleNO = ? "
					+ "connect by prior articleNO = parentNO )";
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, ano);
			ps.executeUpdate();
			ps.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
