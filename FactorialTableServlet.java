import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Exercise37_01") // Match the URL from the book
public class FactorialTableServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	//getting the factorial to display
    private long factorial(int n) {
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Factorial Table</title></head>");
        out.println("<body>");
        out.println("<h2>Factorials from 0 to 10</h2>");
        out.println("<table border='1' cellpadding='5' cellspacing='0' style='text-align:left;'>");
        out.println("<tr><th>Number</th><th>Factorial</th></tr>");

        //display the factorial
		for (int i = 0; i <= 10; i++) {
            out.println("<tr><td>" + i + "</td><td>" + factorial(i) + "</td></tr>");
        }

        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }
}
