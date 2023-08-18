package org.signserver.metasign.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.cert.X509Certificate;

public class AuthenticationFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final X509Certificate[] certificates =
                (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
        if (certificates != null && certificates.length != 0)
            chain.doFilter(request, response);
        else {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED);
            response.getWriter().println("Client certificate authentication required");
        }
    }

    @Override
    public void destroy() {
    }
}
