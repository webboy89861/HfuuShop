package src.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import src.dbHandle.UserHandle;
import src.tools.LoginVerify;
import src.vo.User;

/**
 * Servlet implementation class GoodsCheckServlet
 */
@MultipartConfig(maxFileSize = 1024 * 1024 * 10)
@WebServlet("/UpdateUserImgServlet")
public class UpdateUserImgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UpdateUserImgServlet() {
		super();
	}
	
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		// 处理文件
		if(!LoginVerify.isLogin(request)){
		    request.getRequestDispatcher("user/login.jsp?login-info="+java.net.URLEncoder.encode("你还没有登录！","UTF-8")).forward(request,response);
			return;
		}
		
		User user=(User)(request.getSession().getAttribute("loginUser"));
		Part part = request.getPart("file");
		if(part==null){
			response.sendRedirect("user/personal.jsp?tab=info&info="+java.net.URLEncoder.encode("请选择文件","UTF-8"));
			return;
		}
		String savePath = request.getServletContext().getRealPath("static/user_img");
		part.write(savePath+"/"+user.getId());
		UserHandle userHandle=new UserHandle();
		user.setImg("static/user_img/"+user.getId());
		try {
			userHandle.doUpdate(user);
			response.sendRedirect("user/personal.jsp?tab=info&info="+java.net.URLEncoder.encode("头像更新成功","UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.doGet(request, response);
	}
}