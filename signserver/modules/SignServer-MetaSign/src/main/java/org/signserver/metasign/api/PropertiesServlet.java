package org.signserver.metasign.api;

import com.google.gson.Gson;
import org.signserver.common.NoSuchWorkerException;
import org.signserver.metasign.dto.request.ConfigWorkerRequest;
import org.signserver.metasign.dto.response.GenericResponse;
import org.signserver.metasign.service.MetaSignServices;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

public class PropertiesServlet extends BaseServlet {

    @EJB
    private MetaSignServices metaSignServices;

    @Override
    public void processGet(HttpServletRequest request, HttpServletResponse response) {
        GenericResponse<Properties> result;
        String errMsg = "";

        int workerId = 0;
        try {
            workerId = Integer.parseInt(request.getParameter("workerId").trim());
        } catch (NumberFormatException ex) {
            errMsg = "Invalid workerId";
        } catch (NullPointerException ex) {
            errMsg = "Missing workerId parameter";
        } finally {
            if (!errMsg.isEmpty()) {
                returnResult(response, GenericResponse.fail(errMsg), HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        Properties payload = null;
        try {
            payload = metaSignServices.getWorkerProperties(adminCertificate, workerId);
        } catch (NoSuchWorkerException e) {
            returnResult(response, GenericResponse.fail("Worker Id " + workerId + " not found"), HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        result = GenericResponse.succeed(payload);
        returnResult(response, result, HttpServletResponse.SC_OK);
    }

    @Override
    public void processPost(HttpServletRequest request, HttpServletResponse response) {
        GenericResponse result;
        String requestData;
        ConfigWorkerRequest cwr = null;

        try {
            requestData = request.getReader().lines().collect(Collectors.joining());
            cwr = new Gson().fromJson(requestData, ConfigWorkerRequest.class);

            metaSignServices.configWorkerProperties(adminCertificate, cwr);
            result = GenericResponse.succeed(null);
            returnResult(response, result, HttpServletResponse.SC_OK);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchWorkerException e) {
            returnResult(response, GenericResponse.fail("Worker Id " + cwr.getWorkerId() + " not found"), HttpServletResponse.SC_NOT_FOUND);
        }
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
