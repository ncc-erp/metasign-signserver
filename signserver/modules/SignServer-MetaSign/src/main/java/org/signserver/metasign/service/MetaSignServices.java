package org.signserver.metasign.service;

import org.signserver.common.NoSuchWorkerException;
import org.signserver.metasign.dto.response.PropertyResponse;
import org.signserver.metasign.exception.ErrorProcessingException;
import org.signserver.metasign.dto.request.ConfigWorkerRequest;
import org.signserver.metasign.dto.response.BaseWorkerResponse;
import org.signserver.metasign.dto.response.X509CertificateResponse;
import org.signserver.metasign.util.LogUtil;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Properties;

@Stateless
public class MetaSignServices {

    /** LogUtil for this class */
    private LogUtil logUtil = new LogUtil(this.getClass());
    private final String METASIGN_SERVICE = "MetaSign API";

    @EJB
    private WorkersServices workersServices;
    @EJB
    private PropertiesServices propertiesServices;
    @EJB
    private CertificatesServices certificatesServices;
    @EJB
    private TemplatesServices templatesServices;

    /**
     * Workers services
     */
    public List<BaseWorkerResponse> getAllWorkers(final X509Certificate adminCertificate) {
        logUtil.logRequestInfo(METASIGN_SERVICE, adminCertificate,
                "getAllWorkers", "");
        return workersServices.getAllWorkers();
    }

    public synchronized BaseWorkerResponse addWorker(final X509Certificate adminCertificate, String implementationClass)
            throws ErrorProcessingException {
        logUtil.logRequestInfo(METASIGN_SERVICE, adminCertificate,
                "addWorker", "type=" + implementationClass);
        return workersServices.addWorker(adminCertificate, implementationClass);
    }

    /**
     * Properties services
     */
    public Properties getWorkerProperties(final X509Certificate adminCertificate, int workerId)
            throws NoSuchWorkerException {
        logUtil.logRequestInfo(METASIGN_SERVICE, adminCertificate,
                "getWorkerProperties", "workerId=" + String.valueOf(workerId));
        return propertiesServices.getWorkerProperties(workerId);
    }

    public void configWorkerProperties(final X509Certificate adminCertificate, ConfigWorkerRequest cwr)
            throws NoSuchWorkerException {
        logUtil.logRequestInfo(METASIGN_SERVICE, adminCertificate,
                "configWorkerProperties", "workerId=" + cwr.getWorkerId());
        propertiesServices.configureWorkerProperties(adminCertificate, cwr);
    }

    /**
     * Certificates services
     */
    public X509CertificateResponse getSignerCertificate(final X509Certificate adminCertificate, int workerId) {
        logUtil.logRequestInfo(METASIGN_SERVICE, adminCertificate,
                "getSignerCertificate", "workerId=" + workerId);
        return certificatesServices.getSignerCertificate(workerId);
    }

    /**
     * Templates services
     */
    public List<PropertyResponse> getWorkerPropertiesList(
            final X509Certificate adminCertificate,
            String worker
    ) throws ErrorProcessingException {
        logUtil.logRequestInfo(METASIGN_SERVICE, adminCertificate, "getWorkerPropertiesList");
        return templatesServices.getWorkerPropertiesList(worker);
    }
}
