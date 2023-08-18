package org.signserver.metasign.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PropertyResponse {
    private String key;
    private boolean editable;
}
