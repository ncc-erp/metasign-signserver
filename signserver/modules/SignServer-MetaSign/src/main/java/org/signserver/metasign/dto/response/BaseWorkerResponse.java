package org.signserver.metasign.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BaseWorkerResponse {
    private int id;
    private String name;
    private Status status;
    private String implementationClass;

    public enum Status {
        ACTIVE,
        OFFLINE,
        DISABLED
    }
}
