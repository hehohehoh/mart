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


@WebServlet("/buy2")
public class BuyServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		PrintWriter out = response.getWriter();
		
		ProductDAO dao = new ProductDAO();
		List<ProductVO> list = dao.listProducts();
		//product테이블의 p_name만 담는 list
		List<String> nameInTable = new ArrayList<String>();
		
		//Html에서 받아온 정보들을 담는 list
		List<String> nameInHtml = new ArrayList<String>();
		List<String> manuInHtml = new ArrayList<String>();
		List<Integer> quanInHtml = new ArrayList<Integer>();
		List<Integer> bpInHtml = new ArrayList<Integer>();
		List<Integer> spInHtml = new ArrayList<Integer>();

		out.print("<html><body>");
		// buy테이블에 이번 기록 insert
		int number = Integer.parseInt(request.getParameter("number"));
		dao.addToBuy(number);
		out.print("buy 테이블에 insert 완료<br>");

		//HTML에서 받은 정보(상품이름,제조회사,구매수량,구입가,판매가) 담는 arrayList
		for(int i=1; i<number+1; i++) {
			
			nameInHtml.add(request.getParameter("p_name"+i));
			manuInHtml.add(request.getParameter("manu"+i));
			quanInHtml.add(Integer.parseInt(request.getParameter("account"+i)));
			bpInHtml.add(Integer.parseInt(request.getParameter("buy_price"+i)));
			spInHtml.add(Integer.parseInt(request.getParameter("sell_price"+i)));
		}
		
		//기존 테이블에 있는 상품이름 담는 arrayList
		for(int i=0; i<list.size(); i++) {
			ProductVO vo = list.get(i);
			nameInTable.add(vo.getP_name());
		}
		
		
		for(int i=0; i<nameInHtml.size(); i++) {
			
			String name = nameInHtml.get(i);
			String manu = manuInHtml.get(i);
			int quantity = quanInHtml.get(i);
			int buy_price = bpInHtml.get(i);
			int sell_price = spInHtml.get(i);
			
			//이미 있는 상품이면 update 아니면 insert -> 제품id찾아서 -> buylist에 add
			if(nameInTable.contains(name)){
				dao.doUpdate(name, quantity);
				out.print("product 테이블에 " + name + "의 재고 update 완료<br>");
				int product_id = dao.getPid(name);
				out.print(name + "의 제품코드: " + product_id + "<br>");
				
				dao.addToBuyList(product_id, quantity, buy_price);
				out.print("buylist 테이블에 현재 제품 insert 완료<br>");
				
			}
			else{
				dao.doInsert(name, manu, quantity, buy_price, sell_price);
				out.print("product 테이블에 새 product에 대한 1 row insert 완료<br>");
				int product_id = dao.getPid(name);
				out.print(name + "의 제품코드: " + product_id + "<br>");
				
				dao.addToBuyList(product_id, quantity, buy_price);
				out.print("buylist 테이블에 현재 제품 insert 완료<br>");
			}	
		
		}
		// buy테이블에 total_price를 업데이트
		dao.setTotalBp();
		out.print("buy 테이블에 total_price 업데이트 완료");
		out.print("</body></html>");
		
		dao.disconnect();

		
		
	}

}
