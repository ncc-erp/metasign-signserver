package org.signserver.metasign.service;

import org.signserver.metasign.dto.response.PropertyResponse;
import org.signserver.metasign.exception.ErrorProcessingException;
import org.signserver.metasign.util.ResourceUtil;

import javax.ejb.Stateless;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class TemplatesServices {

    private String propertiesTemplatePath = "/org/signserver/metasign/template/properties/";
    private ResourceUtil resourceUtil = new ResourceUtil();

    public List<PropertyResponse> getWorkerPropertiesList(String implementationClass) throws ErrorProcessingException {
        String path = propertiesTemplatePath + implementationClass.toLowerCase() + ".properties";
        String lines[];
        List<PropertyResponse> propertiesList = new ArrayList<>();

        try {
            String template = resourceUtil.getConfiguration(path, "#");
            lines = template.split("\n");
        } catch (IOException e) {
            throw new ErrorProcessingException(e.getMessage());
        }

        for (String line : lines) {
            String temp[] = line.split("=");
            propertiesList.add(new PropertyResponse(temp[0], temp[1].equalsIgnoreCase("true")));
        }

        return propertiesList;
    }
}
