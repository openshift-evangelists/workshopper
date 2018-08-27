# Workshop Configuration
Workshop configuration is a yaml file that holds all specific configuration for a workshop. The file can have any name with *.yml extension `*.yml` and the structure and possible contents of this file can be seen [here](_workshop.yml). The contents for this file will depend on the content for the workshop, as it will contain all the specific variables that will be used as well as the labs/modules to be used in the workshop.

[Full reference example](_workshop.yml)

## General configuration

```yaml
id: "workshop"
name: "My awesome workshop"
```

## Content configuration

```yaml
content:
  url: https://raw.githubusercontent.com/openshift-evangelists/workshopper-template/master
```

## Variables configuration

```yaml
vars:
  OPENSHIFT_DOCS_BASE: "https://docs.openshift.com/container-platform/3.10"
  OPENSHIFT_USERNAME: "developer"
  OPENSHIFT_PASSWORD: "password"
```

## Labs configuration

```yaml
modules:
  activate:
  - lab1
  - lab2
  - lab3
```