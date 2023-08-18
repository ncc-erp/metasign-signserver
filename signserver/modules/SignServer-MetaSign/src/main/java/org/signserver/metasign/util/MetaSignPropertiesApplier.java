package org.signserver.metasign.util;

import org.signserver.common.*;
import org.signserver.common.util.PropertiesApplier;
import org.signserver.ejb.interfaces.GlobalConfigurationSessionLocal;
import org.signserver.ejb.interfaces.WorkerSessionLocal;
import org.signserver.server.log.AdminInfo;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Implementation of the properties applier using WS for the Admin GUI.
 *
 * @author Marcus Lundblad
 * @version $Id$
 *
 */
public class MetaSignPropertiesApplier extends PropertiesApplier {

    private final WorkerSessionLocal workerSessionBean;
    private final GlobalConfigurationSessionLocal globalSessionBean;
    private final X509Certificate adminCertificate;
    private AdminInfo adminInfo;

    public MetaSignPropertiesApplier(WorkerSessionLocal workerSessionBean, GlobalConfigurationSessionLocal globalSessionBean, X509Certificate adminCertificate) {
        this.workerSessionBean = workerSessionBean;
        this.globalSessionBean = globalSessionBean;
        this.adminCertificate = adminCertificate;
        adminInfo = new AdminInfo(adminCertificate);
    }

    @Override
    protected void setGlobalProperty(String scope, String key, String value) {
        globalSessionBean.setProperty(adminInfo, scope, key, value);
    }

    @Override
    protected void removeGlobalProperty(String scope, String key) {
        globalSessionBean.removeProperty(adminInfo, scope, key);
    }

    @Override
    protected void setWorkerProperty(int workerId, String key, String value) {
        workerSessionBean.setWorkerProperty(adminInfo, workerId, key, value);
    }

    @Override
    protected void removeWorkerProperty(int workerId, String key) {
        workerSessionBean.removeWorkerProperty(adminInfo, workerId, key);
    }

    @Override
    protected void uploadSignerCertificate(int workerId, byte[] signerCert) throws PropertiesApplierException {
        try {
            workerSessionBean.uploadSignerCertificate(adminInfo, workerId, signerCert, GlobalConfiguration.SCOPE_GLOBAL);
        } catch (CertificateException ex) {
            throw new PropertiesApplierException("Illegal request", ex);
        }

    }

    @Override
    protected void uploadSignerCertificateChain(int workerId, List<byte[]> signerCertChain) throws PropertiesApplierException {
        try {
            workerSessionBean.uploadSignerCertificateChain(adminInfo, workerId, signerCertChain, GlobalConfiguration.SCOPE_GLOBAL);
        } catch (CertificateException ex) {
            throw new PropertiesApplierException("Illegal request", ex);
        }
    }

    @Override
    protected int genFreeWorkerId() {
        final List<Integer> workerIds = workerSessionBean.getAllWorkers();
        int max = 0;

        for (final int workerId : workerIds) {
            if (workerId > max) {
                max = workerId;
            }
        }

        return max + 1;
    }

    @Override
    protected int getWorkerId(final String workerName) {
        try {
            return workerSessionBean.getWorkerId(workerName);
        } catch (InvalidWorkerIdException ex) {
            return 0;
        }
    }

    @Override
    protected void addAuthorizedClient(int workerId, AuthorizedClient authClient) {
        workerSessionBean.addAuthorizedClient(adminInfo, workerId, authClient);
    }
    
    @Override
    protected void addAuthorizedClientGen2(int workerId, CertificateMatchingRule authClient) {
        workerSessionBean.addAuthorizedClientGen2(adminInfo, workerId, authClient);
    }

    @Override
    protected void removeAuthorizedClient(int workerId, AuthorizedClient authClient) {
        workerSessionBean.removeAuthorizedClient(adminInfo, workerId, authClient);
    }
    
    @Override
    protected void removeAuthorizedClientGen2(int workerId, CertificateMatchingRule authClient) {
        workerSessionBean.removeAuthorizedClientGen2(adminInfo, workerId, authClient);
    }

    @Override
    protected void checkWorkerNamesAlreadyExists(List<String> workerNames, List<String> workerIds) {
        /**
         * Do nothing here because we've already generated a free worker name
         */
    }

}
