package sealTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetHash
 */
@WebServlet("/Init")
public class Init extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Init() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		File cfgFile = new File(request.getSession().getServletContext().getRealPath("") + "/sys.ini");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");

		String page = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n"
				+ "<html>\r\n" + "<head>\r\n" + "<title>初始化</title>\r\n" + "</head>\r\n" + "<body>\r\n"
				+ "<h1>系统未初始化，请设置SealServer地址</h1>\r\n" + "<h3>如：http://127.0.0.1:8080/Seal</h3>\r\n"
				+ "<form method=\"post\" action=\"Init\">\r\n"
				+ "<label for=\"SealServerURL\">SealServerURL:</label>\r\n"
				+ "<input type=\"text\" name=\"SealServerURL\" id=\"SealServerURL\"/>\r\n"
				+ "<input type=\"submit\" value=\"确定\" />\r\n" + "</form>\r\n" + "</body>\r\n" + "</html>";
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(page);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		// System.out.println("@@@@@@@@@@@@@@@@@@" +
		// request.getParameter("SealServerURL"));
		File cfgFile = new File(request.getSession().getServletContext().getRealPath("") + "/sys.ini");
		if (cfgFile.exists() && cfgFile.isFile()) {
			cfgFile.delete();
			cfgFile.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(cfgFile);
		PrintStream ps = new PrintStream(fos);
		ps.println(request.getParameter("SealServerURL"));// 往文件里写入字符串
		ps.close();
		fos.close();
		response.sendRedirect("Index");
	}

}
