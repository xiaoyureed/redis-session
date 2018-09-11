package io.github.xiaoyureed.servlet;

import io.github.xiaoyureed.model.RestResponse;
import io.github.xiaoyureed.util.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * login servlet
 */
public class LoginServlet extends HttpServlet {

    private String name;
    private String pass;

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String pass = req.getParameter("pass");

        RestResponse<String> restResp = new RestResponse<String>();

        // login success
        if (name.equals(this.name)
                && pass.equals(this.pass)) {
            // setup session
            req.getSession().setAttribute("name", name);
            //resp.sendRedirect(req.getContextPath() + "/hello");

            restResp.setCode("200");

        }
        // login fail
        else {
            restResp.setCode("100");
            restResp.setMsg("user name or password is wroong.");
        }
        writeJson(resp, restResp);

    }

    private void writeJson(HttpServletResponse resp, RestResponse<String> restResp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(JsonUtils.toJson(restResp));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    public void init() throws ServletException {
        this.name = this.readLoginInitParam("name");
        this.pass = this.readLoginInitParam("pass");
    }

    /**
     * return login servlet init param
     * @param key
     * @return
     */
    private String readLoginInitParam(String key) {
        return this.getInitParameter(key);
    }

}
