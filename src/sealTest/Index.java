package sealTest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetHash
 */
@WebServlet("/")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Index() {
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

		/*
		 * long timeStart = new Date().getTime();// 开始时间 /** HTTP方式
		 */
		File cfgFile = new File(request.getSession().getServletContext().getRealPath("") + "/sys.ini");
		if (!(cfgFile.exists() && cfgFile.isFile())) {
			response.sendRedirect("Init");
		} else {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=utf-8");

			String page = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n"
					+ "<html>\r\n" + "<head>\r\n" + "<title>SealTest</title>\r\n"
					+ "<script type=\"text/javascript\" src=\"js/index.js\"></script>\r\n" + "</head>\r\n"
					+ "<body>\r\n" + "<h1>请指定需签章的服务端PDF文件名称及其他参数</h1>\r\n" + "<label for=\"pdfName\">PDF名字</label>\r\n"
					+ "<input name=\"pdfName\"  id=\"pdfName\" type=\"text\" value=\"1.pdf\"/><br/>\r\n"
					+ "<label for=\"imgName\">印章图片名字</label>\r\n"
					+ "<input name=\"imgName\" id=\"imgName\" type=\"text\" value=\"1.png\"/><br/>\r\n"
					+ "<label for=\"kw\">关键字</label>\r\n"
					+ "<input name=\"kw\" id=\"kw\" type=\"text\" value=\"张庆安\"/><br/>\r\n"
					+ "<input type=\"button\" value=\"确定\" onclick=\"confirmInfo()\"/>\r\n" + "</body>\r\n" + "</html>";
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Here!!!POST");
		doGet(request, response);
	}

}
