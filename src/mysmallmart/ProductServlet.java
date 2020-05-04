package mysmallmart;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/product")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		ProductDAO dao = new ProductDAO();
		List<ProductVO> list = dao.listProducts();
		
		out.print("<html><body><table style='margin-left:auto; margin-right:auto; width:60%; text-align:center' border=1>");
		out.print("<caption>제품 정보</caption>");
		out.print("<tr align='center' bgcolor='lightgreen'>");
		out.print("<td>제품코드</td><td>제품명</td><td>제조사</td><td>재고수량</td>"
				+ "<td>구입가격</td><td>판매가격</td></tr>");
		
		for(int i=0; i<list.size(); i++) {
			ProductVO vo = list.get(i);
			int id = vo.getProduct_id();
			String name = vo.getP_name();
			String manu = vo.getManufacture();
			int nowstock = vo.getNowStock();
			int buy = vo.getBuy_price();
			int sell = vo.getSell_price();
			
			out.println("<tr align='right'><td>" + id + "</td><td>" + name + "</td><td>"
					+ manu + "</td><td>" + nowstock + "</td><td>" + buy + "</td><td>"
					+ sell + "</td></tr>");
		}
	
		out.print("</table></body></html>");
		out.print("<a href='/mysmallmart/index.html'>처음으로</a>");
	}

}
