# Workshopper

Builds your workshops and enjoys it.
 
## Deployment

You can deploy wherever you want, but we test specifically for OpenShift
 
```
oc new-app wildfly~https://github.com/openshift-evangelists/workshopper.git -e <YOUR CONFIGURATION>
```

## Configuration using env variables

### CONTENT_URL_PREFIX (optional)

Where to look for content. The system will be looking for

* $CONTENT_URL_PREFIX/_config.yml for configuration
* $CONTENT_URL_PREFIX/_modules.yml for module definition
* $CONTENT_URL_PREFIX/{file name}.adoc for specific content files

If not specified, this variable is constructed from segments as described below.

### GITHUB_REPOSITORY & GITHUB_REF (optional)

If the $CONTENT_URL_PREFIX variable is not defined, constructs the $CONTENT_URL_PREFIX variable for Github repository 
as 

```
CONTENT_URL_PREFIX = https://raw.githubusercontent.com/$GITHUB_REPOSITORY/$GITHUB_REF
```

If the two variables are not defined then are defaulted to `osevg/workshopper-content` and `master` pointing to the 
latest content provided by the authors.

### WORKSHOP_URL, WORKSHOPS_URL, WORKSHOPS_URLS (required)

The system needs definition what the workshop should look like. The system can work with single or multiple.

For single workshop specify 

* $WORKSHOP_URL for single workshop URL

or
  
* $WORKSHOPS_URL for multiple workshops using single URL list file

or

* $WORKSHOPS_URLS for direct specification of workshop URLS

In case niether of these variables is specified, the $WORKSHOP_URL defaults to

```
https://raw.githubusercontent.com/osevg/workshopper-content/master/_default_workshop.yml
```

rendering sample workshop with all modules.

### DEFAULT_LAB (optional)

If the system has multiple workshops using the WORKSHOPS_URL the system can automacally redirect to specific workshop
specified using this variable.