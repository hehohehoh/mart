package mysmallmart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ProductDAO {

	private Connection con;
	private PreparedStatement ps;
	private DataSource dataFactory;
	
	
	//DB_Connection
	public ProductDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context)ctx.lookup("java:/comp/env");
			dataFactory = (DataSource)envContext.lookup("jdbc/oracle_smuser01");
			con = dataFactory.getConnection();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//product table의 list 반환
	public List<ProductVO> listProducts(){
		List<ProductVO> list = new ArrayList<ProductVO>();
		
		try {
			String query = "select * from gttsm.product";
			ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int product_id = rs.getInt("product_id");
				String p_name = rs.getString("p_name");
				String manufacture = rs.getString("manufacture");
				int nowStock = rs.getInt("nowStock");
				int buy_price = rs.getInt("buy_price");
				int sell_price = rs.getInt("sell_price");
				
				ProductVO vo = new ProductVO();
				vo.setProduct_id(product_id);
				vo.setP_name(p_name);
				vo.setManufacture(manufacture);
				vo.setNowStock(nowStock);
				vo.setBuy_price(buy_price);
				vo.setSell_price(sell_price);
				
				list.add(vo);
			}
			
			Collections.sort(list);
			
			rs.close();
			ps.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	// buy테이블에 insert(BuyServlet2 - 1)
	public void addToBuy(int number) {
		
		try {
							
			String query = "insert into gttsm.buy values "
					+ "(buy_seq.nextval, sysdate, ?, null)";
				
			ps = con.prepareStatement(query);
			ps.setInt(1, number);
			ps.execute();
			
			ps.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	//name이 같은거 찾아서 재고를 업데이트 (BuyServlet2 - 2)
	public void doUpdate(String name, int quan) {
			
		try {
			String query = "update gttsm.product "
					+ "set nowstock = nowstock + ? "   
					+ " where p_name = ? ";
			
			ps = con.prepareStatement(query);
			ps.setInt(1, quan);
			ps.setString(2, name);
			ps.execute();
			
			
			ps.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	
	// 처음입고하는 제품은 product테이블에 Insert(BuyServlet2 - 2)
	public void doInsert(String name, String manu, int quan, int bp, int sp) {
		
		try {				
			String query = "insert into gttsm.product values "
						+ "(product_seq.nextval, ?, ?, ?, ?, ?)";
					
			ps = con.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, manu);
			ps.setInt(3, quan);
			ps.setInt(4, bp);
			ps.setInt(5, sp);
			ps.execute();
				
			ps.close();
				
		}catch(Exception e) {
			e.printStackTrace();
		}
			
	}
		
	
	//name으로 name에 해당하는 제품id 반환(BuyServlet2 - 3)
	public int getPid(String name) {
		int pid=0;
		
		try {
						
			String query = "select * from gttsm.product "
						+ "where p_name = ? ";
				
			ps = con.prepareStatement(query);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				pid = rs.getInt("product_id");
			}
			
			ps.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return pid;
	}

	//buylist테이블에 insert(BuyServlet2 - 4)
	public void addToBuyList(int pid, int quan, int bp) {
		
		try {
			String query = "insert into gttsm.buylist values "
					+ "(buy_seq.currval, ?, ?, ?) ";   
						
			ps = con.prepareStatement(query);
			ps.setInt(1, pid);
			ps.setInt(2, quan);
			int price = quan * bp;
			ps.setInt(3, price);
			ps.execute();
			
			
			ps.close();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}	
	
	
	// buy테이블의  total_price를 업데이트 (BuyServlet2 - 5)
	public void setTotalBp() {
		int total_bp=0;
		int buy_no=0;
		
		try {
		
			String q = "select buy_seq.currval as buy_no from dual";
			ps = con.prepareStatement(q);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				buy_no = rs.getInt("buy_no");
			}
			
			String query1 = "select sum(this_price) as total_bp "
						+ "from gttsm.buylist "
						+ "where buy_no = ? ";
		
			ps = con.prepareStatement(query1);
			ps.setInt(1, buy_no);
			ResultSet rs2 = ps.executeQuery();
			
			while(rs2.next()) {
				total_bp = rs2.getInt("total_bp");
			}
			
			String query2 = "update gttsm.buy "
					+ "set total_price = ? "
					+ "where buy_no = ? ";
			
			ps = con.prepareStatement(query2);
			ps.setInt(1, total_bp);
			ps.setInt(2, buy_no);
			ps.execute();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	// sell 테이블에 insert(SellServlet2 - 1)
	public void addToSell(int number) {
		
		try {					
			String query = "insert into gttsm.sell values "
					+ "(sell_seq.nextval, sysdate, ?, null)";
				
			ps = con.prepareStatement(query);
			ps.setInt(1, number);
			ps.execute();
			
			ps.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	
	//name으로 name에 해당하는 제품 판매가 반환(SellServlet2 - 3) // getPid는 위에 한거
	public int getSP(String name) {
		int sp=0;
		
		try {
			
			String query = "select * from gttsm.product "
						+ "where p_name = ? ";
				
			ps = con.prepareStatement(query);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				sp = rs.getInt("sell_price");
			}
			
			ps.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return sp;
	}


	
	//name으로 name에 해당하는 제품 재고 반환(SellServlet2 - 4) // getPid는 위에 한거
	public int getStock(String name) {
		int stock=0;
		
		try {
					
			String query = "select * from gttsm.product "
						+ "where p_name = ? ";
				
			ps = con.prepareStatement(query);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				stock = rs.getInt("nowstock");
			}
			
			ps.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return stock;
	}	

	//SellList테이블에 insert(SellServlet2 - 4)
	public void addToSellList(int pid, int quan, int sp) {
		
		try {
			String query = "insert into gttsm.selllist values "
					+ "(sell_seq.currval, ?, ?, ?) ";   
						
			ps = con.prepareStatement(query);
			ps.setInt(1, pid);
			ps.setInt(2, quan);
			ps.setInt(3, sp);
			ps.execute();
			
			
			ps.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}

	//Product 테이블에 재고 줄이기(SellServlet2 - 5)
	public void cutStock(int pid, int amount) {
		
		try {
			String query = "update gttsm.product "
					+ "set nowstock = nowstock - ? "
					+ " where product_id = ? ";
			
			ps = con.prepareStatement(query);
			ps.setInt(1, amount);
			ps.setInt(2, pid);
			ps.execute();
			
			ps.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	
	// sell테이블의  total_price를 업데이트 (SellServlet2 - 5)
	public void setTotalSp() {
		int total_sp=0;
		int sell_no=0;
		
		try {
			
			String q = "select sell_seq.currval as sell_no from dual";
			ps = con.prepareStatement(q);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				sell_no = rs.getInt("sell_no");
			}
			
			String query1 = "select sum(this_price) as total_sp "
						+ "from gttsm.selllist "
						+ "where sell_no = ? ";
		
			ps = con.prepareStatement(query1);
			ps.setInt(1, sell_no);
			ResultSet rs2 = ps.executeQuery();
			
			while(rs2.next()) {
				total_sp = rs2.getInt("total_sp");
			}
			
			String query2 = "update gttsm.sell "
					+ "set total_price = ? "
					+ "where sell_no = ? ";
			
			ps = con.prepareStatement(query2);
			ps.setInt(1, total_sp);
			ps.setInt(2, sell_no);
			ps.execute();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void disconnect() {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
