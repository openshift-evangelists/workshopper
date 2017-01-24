# Workshopper

Builds your workshops and enjoys it.
 
## Deployment

You can deploy wherever you want, but we test specifically for OpenShift
 
```
oc new-app wildfly~https://github.com/openshift-evangelists/workshopper.git -e <YOUR CONFIGURATION>
```

## Configuration using env variables

### CONTENT_URL_PREFIX (optional)

Content repository where to look for modules. The system will look for

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

* $WORKSHOPS_URLS for direct specification of workshop URLS using comma separated list

In case niether of these variables is specified, the $WORKSHOP_URL defaults to

```
https://raw.githubusercontent.com/osevg/workshopper-content/master/_default_workshop.yml
```

rendering sample workshop with all modules.

### DEFAULT_LAB (optional)

If the system has multiple workshops using the WORKSHOPS_URL the system can automacally redirect to specific workshop
specified using this variable.

## Files and file formats

### $WORKSHOPS_URLS

Comma separated list of urls
 
 ```
 WORKSHOPS_URLS = "<URL1>,<URL2>,<URL3>" 
 ```

### $WORKSHOPS_URL

$WORKSHOPS_URL points to yaml file that contains list of URLs contain workshop definition as described in $WORKSHOP_URL 
section.

### $WORKSHOP_URL

$WORKSHOP_URL points to yaml file describing specific workshop. Workshop pull together several (or all) content modules
to build a "learning experience" for the end user.

The main sections are

* `name` - display name of the workshop
* `logo` - logo to display on the workshop page
* `vars` - defines variable values to use in modules 
* `revision` - activate this revision for all modules unless specified for the module
* `modules` - configure modules in this workshop

for example

```yaml
name: Testing workshop
logo: test.png
vars:
  VARIABLE: value
revision: <revision name>
modules:
  <module configuration>
```

The module configuration has two sections

* `activate` - list modules to include in the workshop (all modules are included unless specified)
* `revisions` - set per-module revision

```yaml
name: Testing workshop
logo: test.png
revision: revision1
modules:
  activate:
    - module2
  revisions:
    module1: revisionxy
```

Let's assume that the module definition has 2 modules (`module1` and `module2`) with `module2` having dependency on 
`module1`. This workshop activates `module2` and as it has dependency on `module1`, both are going to be included in 
the workshop. `module1` is going to have `revisionxy` while `module2` will have `revision1`.  

### _config.yml

`_config.yml` contains basic configuration fo the system. Currently these is only one section 
allowed in this file called `vars` that defines most generic fallback values for variables used in the modules. 

```yaml
vars:
  VARIABLE_X: something
```

### _modules.yml

`_modules.yml` defines modules available in the content repository. The file has one main `modules` section that 
contains the key-value pair, where the key is module id and value is module definition.

```yaml
modules:
  module1:
    module_definition
```

The module definition contains metadata describing the module

* `name` - display name of the module
* `requires` - list of other module ids that are required to understand before starting this module
* `vars` - defines variable values to use in the module
* `revisions` - configures content revisions

```yaml
modules:
  module1:
    name: Module 1
    vars:
      VARIABLE1: value1
  module2:
    name: Module 2
    vars:
      VARIABLE2: value2
    requires:
      - module1
```

Revisions allow choosing between different content versions either by changing variable values or by choosing 
completely different content.
  
```yaml
modules:
  module1:
    name: Module 1
    vars:
      VARIABLE1: value1
  module2:
    name: Module 2
    vars:
      VARIABLE2: value2
    requires:
      - module1
    revisions:
      revision_name:
        vars:
          VARIABLE2: value22
```

If no revision is chosen, then the value of `$VARIABLE2` is "value2", However when revisoin `revision_name` is active, 
variable $VARIABLE2 value is "value22".

### Content files

The content file is located in the root of the content repository and follows the naming scheme `module-id.adoc`. When
the content should be in a subdirectory, the module name reflects that by using the `_` characted.

```yaml
modules:
  module1:
    name: Module 1
  sub_module2:
    name: Module 2
```

This modules definition would have two asciidoc files

```
  ├── _config.yml
  ├── _modules.yml
  ├── module1.adoc
  └── sub
        └── module2.adoc
```

## Module variable precedence

Variable for substitution in modules can be defined in these places
 
* _config.yml
* _modules.yml
* $WORKSHOP_URL / $WORKSHOPS_URL / $WORKSHOPS_URLS
* Environment variables

where the priority is

```
_config.yml < _modules.yml < $WORKSHOP_URL / $WORKSHOPS_URL / $WORKSHOPS_URLS < Environment variables
```

i.e. environment variables override all the other definitions.