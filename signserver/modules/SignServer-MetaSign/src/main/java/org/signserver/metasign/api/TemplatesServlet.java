package org.signserver.metasign.api;

import org.signserver.metasign.dto.response.GenericResponse;
import org.signserver.metasign.exception.ErrorProcessingException;
import org.signserver.metasign.service.MetaSignServices;

import javax.ejb.EJB;
import javax.servlet.http.*;

public class TemplatesServlet extends BaseServlet {

    @EJB
    private MetaSignServices metaSignServices;

    @Override
    public void processGet(HttpServletRequest request, HttpServletResponse response) {
        String type;
        String implementationClass;
        GenericResponse result = null;

        try {
            type = request.getParameter("type").trim();
            implementationClass = request.getParameter("implementationClass").trim();
        } catch (NullPointerException ex) {
            returnResult(response, GenericResponse.fail("Missing parameter(s)"), HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (type.equalsIgnoreCase("properties")) {
            switch (implementationClass) {
                case "pdfsigner":
                    try {
                        result = GenericResponse.succeed(metaSignServices.getWorkerPropertiesList(adminCertificate, implementationClass));
                    } catch (ErrorProcessingException e) {
                        returnResult(response, GenericResponse.fail(e.getMessage()), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        return;
                    }
                    break;
                default:
                    returnResult(
                            response,
                            GenericResponse.fail(implementationClass.toLowerCase() + ".properties file not found!"),
                            HttpServletResponse.SC_NOT_FOUND);
            }
        }

        returnResult(response, result, HttpServletResponse.SC_OK);
    }

    @Override
    public void processPost(HttpServletRequest request, HttpServletResponse response) {
        handleUnsupportedMethod(response);
    }

    @Override
    public void processPut(HttpServletRequest request, HttpServletResponse response) {
        handleUnsupportedMethod(response);
    }

    @Override
    public void processDelete(HttpServletRequest request, HttpServletResponse response) {
        handleUnsupportedMethod(response);
    }
}
