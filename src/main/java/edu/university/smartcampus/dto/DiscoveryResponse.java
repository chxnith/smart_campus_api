package edu.university.smartcampus.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class DiscoveryResponse {
    private String apiName;
    private String version;
    private String contact;
    private Map<String, String> resources = new LinkedHashMap<>();
    private Map<String, String> links = new LinkedHashMap<>();

    public DiscoveryResponse() {
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Map<String, String> getResources() {
        return resources;
    }

    public void setResources(Map<String, String> resources) {
        this.resources = resources;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }
}
