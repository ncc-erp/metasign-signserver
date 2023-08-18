package org.signserver.metasign.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@Data
public class ConfigWorkerRequest {
    private Integer workerId;
    private HashMap<String, String> propertiesAndValues;
    private List<String> propertiesToRemove;
}
