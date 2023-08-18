package org.signserver.metasign.api;

import org.signserver.metasign.dto.response.GenericResponse;
import org.signserver.metasign.dto.response.X509CertificateResponse;
import org.signserver.metasign.service.MetaSignServices;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CertificatesServlet extends BaseServlet {

    @EJB
    private MetaSignServices metaSignServices;

    @Override
    public void processGet(HttpServletRequest request, HttpServletResponse response) {
        GenericResponse<X509CertificateResponse> result;
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

        X509CertificateResponse payload = metaSignServices.getSignerCertificate(adminCertificate, workerId);
        result = GenericResponse.succeed(payload);
        if (payload == null)
            result.setMessage("Not a signer or this signer don't have certificate");
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
