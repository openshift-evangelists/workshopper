# Workshopper
This is a tool for creating nice Workshops developed by the OpenShift Evangelist team.

![Nice workshop example](./docs/images/Workshop-Example.png)

Please, read the [documentation](./docs/README.md)

# For Developers
> If you need to generate the `Gemfile.lock`

```shell
# build with this dockerfile
$ docker build -f DockerfileToGenerateGemfile_lock -t myworkshopper .

# get the generated Gemfile.lock
$ docker run --rm -it --user=0 --name workshopper --entrypoint "/bin/cat" myworkshopper Gemfile.lock > Gemfile.lock
```