require 'yaml'

require 'workshopper/lab'
require 'workshopper/loader'

module Workshopper

  class Content

    def initialize(workshop)
      @workshop = workshop
      @data = Workshopper::Loader.get(File.join(prefix, '_modules.yml'))
      @data = YAML.load(@data)
      @data['config'] = {
        'renderer' => 'adoc',
        'vars' => []
      }.merge(@data['config'] || {})
      @labs = {}
      @env = @data['config']['vars'].inject({}) do |env, item|
        env[item['name']] = item['value']
        env
      end
    end

    def workshop
      @workshop
    end

    def lab(name)
      @labs[name] ||= Workshopper::Lab.new(self, name)
    end

    def prefix
      @workshop.prefix
    end

    def ext
      case @data['config']['renderer']
      when 'markdown'
        'md'
      else
        'adoc'
      end
    end

    def labs
      @data['modules']
    end

    def issues
      @data['config'] && @data['config']['issues']
    end

    def env
      @env
    end

  end

end