# Xenit testcontainers

[![Apache License 2](https://img.shields.io/badge/license-Apache%202-blue.svg)](LICENSE)

Extension on top of [testcontainers.org](https://www.testcontainers.org/), making it easy to startup Alfresco or Alfred 
containers in JUnit tests. 

Work in progress. 

## Alfresco testcontainers

### Available containers

#### AlfrescoContainer

* Constructing an Alfresco container based on Xenit images  
    ```AlfrescoContainer container = new AlfrescoContainer(Edition.COMMUNITY, "6.1")```

* Construction an Alfresco container based on Alfresco images  
    ```AlfrescoContainer container = new AlfrescoContainer("docker.io/alfresco/alfresco-content-repository:6.1.1.2")```

### Usage

The [testcontainers documentation](https://www.testcontainers.org/) indicates how the library can / should be used with 
e.g. JUnit 4 or JUnit 5. 

## Alfresc testcontainers

## License 
This project is licensed under the Apache V2 License - see the [LICENSE](LICENSE) file for details.