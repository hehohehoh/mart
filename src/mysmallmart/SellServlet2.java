package mysmallmart;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/sell2")
public class SellServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();	
		ProductDAO dao = new ProductDAO();
		
		List<ProductVO> listTable = dao.listProducts();
		List<String> nameTable = new ArrayList<String>();
		
		int number = Integer.parseInt(request.getParameter("num"));
		List<String> nameHtml = new ArrayList<String>();
		List<Integer> amountHtml = new ArrayList<Integer>();
		
		dao.addToSell(number);
		out.print("<html><body>sell 테이블에 Insert 완료<br>");
		
		for(int i=0; i<listTable.size(); i++) {
			nameTable.add(listTable.get(i).getP_name());
		}
		
		for(int i=1; i<(number + 1); i++) {
			nameHtml.add(request.getParameter("p_name"+i));
			amountHtml.add(Integer.parseInt(request.getParameter("amount"+i)));
		}
		
		for(int i=0; i<number; i++) {
			String name = nameHtml.get(i);
			int amount = amountHtml.get(i);
			int stock = dao.getStock(name);
					
			if(!nameTable.contains(name)) {
				out.print("판매하고자 하는 제품이 입고되어 있지 않습니다.<br>");
				out.print("처음으로 돌아가 먼제 제품을 입고해주세요.<br>");
				break;
			}
			
			if(amount > stock) {
				out.print("재고가 부족합니다<br>");
				out.print("처음으로 돌아가 먼제 제품을 입고해주세요.<br>");
				break;
			}
			
			int pid = dao.getPid(name);
			int sp = dao.getSP(name);
			int this_sp = amount * sp;
			
			dao.addToSellList(pid, amount, this_sp);
			out.print("sellList 테이블에 Insert 완료<br>");

			dao.cutStock(pid, amount);
			out.print("product 테이블에 재고 update 완료<br>");

		}
		
		dao.setTotalSp();
		out.print("sell 테이블에 total_price 추가 완료<br>");
		out.print("</body></html>");
		
		dao.disconnect();
	}

}
