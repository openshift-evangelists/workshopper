# Module Configuration
Module configuration is a yaml file that holds all the possible configuration
for the labs in a repository. The file has to be named `_modules.yml` and the structure and possible contents of this file can be seen [here](_modules.yml). The contents for this file will depend on the content for the workshop, as it will contain all the possible variables that can be used as well as the labs/modules.

[Full reference example](_modules.yml)

## General configuration

```yaml
config:
  renderer: markdown
  ...
```

### Variables configuration

```yaml
config:
  ...
  vars:
    - name: OPENSHIFT_USER
      desc: OpenShift user
      value: "openshift"
    - name: OPENSHIFT_PASSWORD
      desc: OpenShift password
      value: "openshift"
```

## Labs configuration

```yaml
modules:
  lab1:
    name: Introduction
  lab2:
    name: Connect
    vars:
      OPENSHIFT_USER: "student"
  lab3:
    name: Getting started
    requires:
      - lab3
```