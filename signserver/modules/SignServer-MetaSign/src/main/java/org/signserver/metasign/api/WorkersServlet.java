package org.signserver.metasign.api;

import org.signserver.metasign.exception.ErrorProcessingException;
import org.signserver.metasign.dto.response.BaseWorkerResponse;
import org.signserver.metasign.dto.response.GenericResponse;
import org.signserver.metasign.service.MetaSignServices;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class WorkersServlet extends BaseServlet {

    @EJB
    private MetaSignServices metaSignServices;

    @Override
    public void processGet(HttpServletRequest request, HttpServletResponse response) {
        GenericResponse<List<BaseWorkerResponse>> result;
        int statusCode;

        List<BaseWorkerResponse> workersList = metaSignServices.getAllWorkers(adminCertificate);
        result = GenericResponse.succeed(workersList);
        statusCode = HttpServletResponse.SC_OK;
        returnResult(response, result, statusCode);
    }

    @Override
    public void processPost(HttpServletRequest request, HttpServletResponse response) {
        GenericResponse result;
        String implementationClass;

        try {
            implementationClass = request.getParameter("implementationClass").trim();
        } catch (NullPointerException ex) {
            returnResult(
                    response,
                    GenericResponse.fail("Missing implementationClass parameter"),
                    HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            BaseWorkerResponse payload = metaSignServices.addWorker(adminCertificate, implementationClass);
            result = GenericResponse.succeed(payload);
        } catch (ErrorProcessingException e) {
            returnResult(response, GenericResponse.fail(e.getMessage()), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        returnResult(response, result, HttpServletResponse.SC_CREATED);
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
