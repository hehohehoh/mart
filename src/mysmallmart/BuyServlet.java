package mysmallmart;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/buy")
public class BuyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		int num = Integer.parseInt(request.getParameter("num"));
		
		out.print("<html><body><h3>" + num + "개의 상품 입고</h3>");
		out.print("<form method='post' action='buy2'>");

		
		for(int i=1; i<(num+1); i++) {
			out.print(i + "번째 상품에 대한 입고 정보 작성<br>");
			out.print("상품이름을 입력하세요: <input type='text' name='p_name" + i + "'><br>");
			out.print("제조회사를 입력하세요: <input type='text' name='manu" + i + "'><br>");
			out.print("구매수량을 입력하세요: <input type='number' name='account" + i + "'><br>");
			out.print("구매가격을 입력하세요: <input type='number' name='buy_price" + i + "'><br>");
			out.print("판매가격을 입력하세요: <input type='number' name='sell_price" + i + "'><br><br>");
		
		}
		out.print("버튼을 누르시면 구매가 완료됩니다. <input type='submit' value='구매'><br>");
		out.print("<input type='hidden' name='number' value=" + num + ">");
		out.print("</form></body></html>");
	}

}
