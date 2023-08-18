package org.signserver.metasign.service;

import org.signserver.common.InvalidWorkerIdException;
import org.signserver.common.WorkerIdentifier;
import org.signserver.common.util.PropertiesApplier;
import org.signserver.common.util.PropertiesParser;
import org.signserver.ejb.interfaces.GlobalConfigurationSessionLocal;
import org.signserver.ejb.interfaces.WorkerSessionLocal;
import org.signserver.metasign.exception.ErrorProcessingException;
import org.signserver.metasign.dto.response.BaseWorkerResponse;
import org.signserver.metasign.util.MetaSignPropertiesApplier;
import org.signserver.metasign.util.ResourceUtil;
import org.signserver.server.log.AdminInfo;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Stateless
public class WorkersServices {

    @EJB
    private WorkerSessionLocal workerSessionBean;
    @EJB
    private GlobalConfigurationSessionLocal globalSessionBean;
    private ResourceUtil resourceUtil = new ResourceUtil();
    private String templateFolderPath = "/org/signserver/metasign/template/workers/";

    public List<BaseWorkerResponse> getAllWorkers() {
        List<BaseWorkerResponse> responseList;

        // Get a list of all workers
        List<String> workerNames = workerSessionBean.getAllWorkerNames();
        responseList = workerNames.stream()
                .map(name -> {
                    try {
                        return new BaseWorkerResponse(
                                workerSessionBean.getWorkerId(name),
                                name,
                                BaseWorkerResponse.Status.ACTIVE,
                                null
                        );
                    } catch (InvalidWorkerIdException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        // Check every worker status if its OFFLINE or DISABLED and set implementationClass
        for (BaseWorkerResponse bwr : responseList) {
            try {
                // Workers with error(s)
                if (!workerSessionBean.getStatus(WorkerIdentifier.createFromIdOrName(bwr.getName()))
                        .getFatalErrors().isEmpty())
                    bwr.setStatus(BaseWorkerResponse.Status.OFFLINE);

                // Worker without crypto token
                /**
                 * If we create PDFSigner then remove the crypto worker
                 * the status of PDFSigner is still ACTIVE till it was configured
                 * Worker ID in SignServer starts from 1
                 */
                Properties props = workerSessionBean.exportWorkerConfig(bwr.getId());
                if (!props.getProperty("CRYPTOTOKEN", "").equals(""))
                    if (!workerNames.contains(props.getProperty("CRYPTOTOKEN")))
                        bwr.setStatus(BaseWorkerResponse.Status.OFFLINE);

                // Disabled worker
                if (props.getProperty("DISABLED", "FALSE").equalsIgnoreCase("TRUE"))
                    bwr.setStatus(BaseWorkerResponse.Status.DISABLED);

                // implementationClass
                String implementationClass = props.getProperty("CRYPTOTOKEN_IMPLEMENTATION_CLASS",
                        props.getProperty("IMPLEMENTATION_CLASS"));
                implementationClass = implementationClass
                        .substring(implementationClass.lastIndexOf('.') + 1);
                bwr.setImplementationClass(implementationClass);
            } catch (InvalidWorkerIdException e) {
                throw new RuntimeException(e);
            }

        }

        responseList.sort((o1, o2) -> o1.getId() - o2.getId());

        return responseList;
    }

    private String genWorkerName(String baseName) {

        int max = 0;
        List<Integer> workerIds = workerSessionBean.getAllWorkers();
        for (int id : workerIds)
            if (id > max)
                max = id;
        max++;

        List<String> workerNames = workerSessionBean.getAllWorkerNames();
        while (workerNames.contains(baseName + max))
            max++;

        return baseName + max;
    }

    public BaseWorkerResponse addWorker(X509Certificate adminCertificate, String implementationClass)
            throws ErrorProcessingException {
        String configuration;
        List<Integer> modifiedWorkers = null;
        Properties props = new Properties();
        String newWorkerName;

        try {
            String path = templateFolderPath + implementationClass.toLowerCase() + ".properties";
            configuration = resourceUtil.getConfiguration(path, "#");
            props.load(new ByteArrayInputStream(configuration.getBytes(StandardCharsets.ISO_8859_1)));
        } catch (IOException e) {
            throw new ErrorProcessingException(e.getMessage());
        }

        if (props.isEmpty())
            throw new ErrorProcessingException("No properties loaded");
        else {

            newWorkerName = genWorkerName(props.getProperty("WORKERGENID1.NAME", "Worker"));
            System.out.println(newWorkerName);
            props.setProperty("WORKERGENID1.NAME", newWorkerName);

            implementationClass = props.getProperty("WORKERGENID1.CRYPTOTOKEN_IMPLEMENTATION_CLASS",
                    props.getProperty("WORKERGENID1.IMPLEMENTATION_CLASS"));
            implementationClass = implementationClass
                    .substring(implementationClass.lastIndexOf('.') + 1);

            final PropertiesParser parser = new PropertiesParser();
            parser.process(props);

            if (parser.hasErrors()) {
                final List<String> errors = parser.getErrors();
                throw new ErrorProcessingException("Error parsing properties: " + errors.get(0));
            } else {
                final PropertiesApplier applier = new MetaSignPropertiesApplier(workerSessionBean, globalSessionBean, adminCertificate);

                applier.apply(parser);

                if (applier.hasError()) {
                    throw new ErrorProcessingException("Error applying properties: " + applier.getError());
                } else {
                    modifiedWorkers = applier.getWorkerIds();
                    for (final int id : modifiedWorkers)
                        workerSessionBean.reloadConfiguration(new AdminInfo(adminCertificate), id);
                }
            }
        }

        try {
            return new BaseWorkerResponse(
                    workerSessionBean.getWorkerId(newWorkerName),
                    newWorkerName,
                    null,
                    implementationClass);
        } catch (InvalidWorkerIdException e) {
            throw new RuntimeException(e);
        }
    }
}
