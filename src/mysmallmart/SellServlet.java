package mysmallmart;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/sell")
public class SellServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		int num = Integer.parseInt(request.getParameter("num"));
		
		out.print("<html><body>" + num + "개의 상품 판매");
		out.print("<form method='post' action='sell2' >");
		
		for(int i=1; i<(num+1); i++) {
			
			out.print(i + "번째 상품에 대한 판매 정보 <br>");
			out.print("상품 이름: <input type='text' name='p_name" + i + "'><br>");
			out.print("판매 갯수: <input type='number' name='amount" + i + "'><br><br>");
		}
		
		out.print("버튼을 누르시면 판매가 완료됩니다. <input type='submit' value='판매' >");
		out.print("<input type='hidden' name='num' value=" + num + ">");
		out.print("</form></body></html>");
	}

}
