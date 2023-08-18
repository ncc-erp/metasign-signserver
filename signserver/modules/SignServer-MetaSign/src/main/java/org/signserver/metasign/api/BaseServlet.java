package org.signserver.metasign.api;

import org.signserver.metasign.dto.response.GenericResponse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.cert.X509Certificate;

public abstract class BaseServlet extends HttpServlet {

    protected X509Certificate adminCertificate;

    public void getAdminCertificate(HttpServletRequest request) {
        adminCertificate = ((X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate"))[0];
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        getAdminCertificate(request);
        processGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        getAdminCertificate(request);
        processPost(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        getAdminCertificate(request);
        processPut(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        getAdminCertificate(request);
        processDelete(request, response);
    }

    public abstract void processGet(HttpServletRequest request, HttpServletResponse response);

    public abstract void processPost(HttpServletRequest request, HttpServletResponse response);

    public abstract void processPut(HttpServletRequest request, HttpServletResponse response);

    public abstract void processDelete(HttpServletRequest request, HttpServletResponse response);

    protected void handleUnsupportedMethod(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        try {
            response.getWriter().println("405 Not Allowed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void returnResult(HttpServletResponse response, GenericResponse result, int statusCode) {
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(statusCode);
            out.print(result.toJSON());
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
