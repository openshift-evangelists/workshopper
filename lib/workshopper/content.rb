require 'yaml'

require 'workshopper/lab'
require 'workshopper/loader'

module Workshopper

  class Content

    def initialize(prefix)
      @prefix = prefix
      @data = Workshopper::Loader.get(File.join(@prefix, '_modules.yml'))
      @data = YAML.load(@data)
      @labs = {}
    end

    def lab(name)
      @labs[name] ||= Workshopper::Lab.new(self, name)
    end

    def prefix
      @prefix
    end

    def labs
      @data['modules']
    end

    def issues
      @data['config'] && @data['config']['issues']
    end

    def env
      @data['config']['vars'].inject({}) do |all, var|
        all[var['name']] = var['value']
        all
      end
    end

  end

end