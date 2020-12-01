package com.opsera.code.deployer.resources;

import lombok.Data;

@Data
public class ToolDetails {

    private String id;

    private String name;

    private String description;

    private String owner;

    private String[] type;

    private String[] tags;

    private Boolean active;

    private String status;

    private String toolIdentifier;

    private Configuration configuration;
}
