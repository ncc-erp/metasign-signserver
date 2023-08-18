package org.signserver.metasign.service;

import org.signserver.common.CryptoTokenOfflineException;
import org.signserver.common.WorkerIdentifier;
import org.signserver.ejb.interfaces.WorkerSessionLocal;
import org.signserver.metasign.dto.response.X509CertificateResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.security.cert.X509Certificate;

@Stateless
public class CertificatesServices {

    @EJB
    private WorkerSessionLocal workerSessionBean;

    public X509CertificateResponse getSignerCertificate(int workerId) {
        X509CertificateResponse result = null;

        try {
            X509Certificate certificate = (X509Certificate)
                    workerSessionBean.getSignerCertificate(WorkerIdentifier.createFromIdOrName(String.valueOf(workerId)));
            if (certificate != null) {
                result = new X509CertificateResponse();
                result.setSubjectDN(certificate.getSubjectDN().toString());
                result.setSerialNumber(certificate.getSerialNumber().toString(16));
                result.setIssuerDN(certificate.getIssuerDN().toString());
                result.setNotBefore(certificate.getNotBefore());
                result.setNotAfter(certificate.getNotAfter());
            }
        } catch (CryptoTokenOfflineException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
