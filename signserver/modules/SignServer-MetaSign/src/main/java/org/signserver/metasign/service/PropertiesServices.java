package org.signserver.metasign.service;

import org.signserver.common.NoSuchWorkerException;
import org.signserver.ejb.interfaces.WorkerSessionLocal;
import org.signserver.metasign.dto.request.ConfigWorkerRequest;
import org.signserver.server.log.AdminInfo;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.security.cert.X509Certificate;
import java.util.*;

@Stateless
public class PropertiesServices {

    @EJB
    private WorkerSessionLocal workerSessionBean;

    public Properties getWorkerProperties(int workerId)
            throws NoSuchWorkerException {
        Properties result = workerSessionBean.exportWorkerConfig(workerId);
        if (result.isEmpty() || result == null)
            throw new NoSuchWorkerException(String.valueOf(workerId));
        return result;
    }

    public void configureWorkerProperties(X509Certificate adminCertificate, ConfigWorkerRequest cwr)
            throws NoSuchWorkerException {

        // Check if worker is existed
        Properties props = workerSessionBean.exportWorkerConfig(cwr.getWorkerId());
        if (props == null || props.isEmpty())
            throw new NoSuchWorkerException(String.valueOf(cwr.getWorkerId()));

        final AdminInfo adminInfo = new AdminInfo(adminCertificate);

        // Change all keys to UPPERCASE
        HashMap<String, String> pav = new HashMap<>();
        for (Map.Entry<String, String> entry : cwr.getPropertiesAndValues().entrySet())
            pav.put(entry.getKey().toUpperCase(), entry.getValue());
        cwr.setPropertiesAndValues(pav);

        List<String> ptr = new ArrayList<>();
        for (String key : cwr.getPropertiesToRemove())
            ptr.add(key.toUpperCase());
        cwr.setPropertiesToRemove(ptr);

        workerSessionBean.updateWorkerProperties(
                adminInfo,
                cwr.getWorkerId(),
                cwr.getPropertiesAndValues(),
                cwr.getPropertiesToRemove()
        );

        workerSessionBean.reloadConfiguration(adminInfo, cwr.getWorkerId());
    }
}
