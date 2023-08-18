package org.signserver.metasign.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class X509CertificateResponse {
    private String subjectDN;
    private String serialNumber;
    private String issuerDN;
    private Date notBefore;
    private Date notAfter;
}
