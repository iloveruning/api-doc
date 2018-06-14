package io.github.llchen.apidoc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author llchen12
 * @date 2018/6/11
 */
@Data
@ConfigurationProperties(prefix = "api-doc")
public class ApiDocProperties {

    public static final String PREFIX="api-doc";

    /**
     * enable to open api-doc
     */
    private boolean enable=false;

    /**
     * the package of controller is used to scan api
     */
    private String controllerPackage;

    /**
     * the package of model is used to scan model
     */
    private String modelPackage;

    /**
     * the api version
     */
    private String version;

    /**
     * the api host address
     */
    private String host;


    @NestedConfigurationProperty
    private Contact contact;


    @Data
    public static class Contact{
        /**
         * the name of the contact
         */
        private String name;
        /**
         * the email of the contact
         */
        private String email;
    }

}
