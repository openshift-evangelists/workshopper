require 'yaml'

require 'workshopper/lab'
require 'workshopper/loader'

module Workshopper

  class Content

    def initialize(prefix)
      @prefix = prefix
      @data = Workshopper::Loader.get(File.join(@prefix, '_modules.yml'))
      @data = YAML.load(@data)
      @data['config'] ||= {}
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
      @env ||= @data['config']['vars'] || {}
    end

  end

end