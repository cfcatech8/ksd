package sealTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cfca.com.itextpdf.text.pdf.security.TSAClientBouncyCastle;
import cfca.sadk.util.Base64;
import cfca.seal.sadk.DonePdfSeal;
import cfca.sealclient.servlet.SealClient;
import cfca.sealclient.util.IoUtil;
import cfca.sealclient.util.StringUtil;

/**
 * Servlet implementation class CompoundPdf
 */
@WebServlet("/CompoundPdf")
public class CompoundPdf extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CompoundPdf() {
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
		String url = "";// "http://127.0.0.1:18080/Seal" + SealClient.SLASH +
						// SealClient.DISPATCHER_SERVLET;
		try {
			FileInputStream fis = new FileInputStream(cfgFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			url = br.readLine() + SealClient.SLASH + SealClient.DISPATCHER_SERVLET;
			br.close();
			isr.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SealClient sealClient = new SealClient(url);
		String organizationCode = "root";
		String result = "";
		byte[] signature = null;
		String strTmp = request.getReader().readLine();
		String strSignature = strTmp.substring(strTmp.indexOf("\"signature\":\"") + "\"signature\":\"".length(),
				strTmp.indexOf("\",\"ID\""));
		String id = strTmp.substring(strTmp.indexOf("\",\"ID\":\"") + "\",\"ID\":\"".length(), strTmp.indexOf("\"}"));
		System.out.println(strTmp);
		System.out.println("{\"signature\":\"" + strSignature + "\",\"ID\":\"" + id + "\"}");
		signature = strSignature.getBytes();
		System.out.println("signature:" + new String(signature));
		// 合成签名
		try {
			result = sealClient.compoundPdfSealWithOuterSignature(signature, id, organizationCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("result=" + result);

		if ("200".equals(StringUtil.getNodeText(result, "Code"))) {

			System.out.println("CompoundPdfSealWithOuterSignature2Test:ok");

			String pdfString = StringUtil.getNodeText(result, "Pdf");

			if (StringUtil.isNotEmpty(pdfString)) {
				pdfString = IoUtil.urlDecode(pdfString);
				byte[] resultPdf = Base64.decode(pdfString.getBytes(DEFAULT_CHARSET));
				System.out.println("path=" + request.getSession().getServletContext().getRealPath(""));
				IoUtil.write(request.getSession().getServletContext().getRealPath("") + "/Seald.pdf", resultPdf);
			}

		} else {
			System.out.println("CompoundPdfSealWithOuterSignature2Test:wrong");
		}
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public static void testTSA() throws Exception {
		String tsaURL = "http://test1.tsa.cn/tsa";
		String tsaUserName = "tsademo";
		String tsaPassword = "tsademo";
		TSAClientBouncyCastle tsaClient = new TSAClientBouncyCastle(tsaURL, tsaUserName, tsaPassword);
		byte[] tsarsp = DonePdfSeal.testTSA(tsaClient);
		System.out.println(new String(tsarsp));
	}

}
