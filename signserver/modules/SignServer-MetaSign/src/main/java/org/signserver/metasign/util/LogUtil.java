package org.signserver.metasign.util;

import org.apache.log4j.Logger;

import java.security.cert.X509Certificate;

public class LogUtil {

    private Logger LOG;
    private Class clazz;

    public LogUtil(Class clazz) {
        this.clazz = clazz;
        LOG = Logger.getLogger(clazz);
    }

    public void logRequestInfo(String title, X509Certificate certificate, String operation, String... args) {
        StringBuilder line = new StringBuilder()
                .append(title)
                .append("; ")

                .append("subjectDN=")
                .append(certificate.getSubjectX500Principal().getName())
                .append("; ")

                .append("serialNumber=")
                .append(certificate.getSerialNumber().toString(16))
                .append("; ")

                .append("issuerDN=")
                .append(certificate.getIssuerX500Principal().getName())
                .append("; ")

                .append("operation=")
                .append(operation)
                .append("; ")

                .append("arguments=");
        for (String arg : args) {
            line.append(arg).append(",");
        }
        line.append(";");
        LOG.info(line.toString());
    }
}
