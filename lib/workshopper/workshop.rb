require 'yaml'
require 'digest'
require 'active_support/core_ext/string/inflections'

module Workshopper
  class Workshop

    def initialize(url)
      @url = url
      @data = YAML.load(Loader.get(@url))
      normalize
      setup
    end

    def normalize
      @data['id'] ||= Digest::SHA256.hexdigest(@url)
      @data['name'] ||= ''
      @data['content'] ||= {}
      @data['content']['url'] ||= ENV['CONTENT_URL_PREFIX']
      @data['vars'] ||= {}
      @data['modules'] ||= {}
      @data['modules']['activate'] ||= []

      @renderer = nil
    end

    def setup
      @loader = Loader.new(@data['content']['url'])

      @modules_data = YAML.load(@loader.get('_modules.yml'))

      @modules_data['config'] ||= {}
      @modules_data['config']['vars'] ||= {}

      @data['content']['templates'] ||= (@modules_data['config']['templates'] || 'liquid')
      @data['content']['renderer'] ||= (@modules_data['config']['renderer'] || 'asciidoc')

      @vars = Vars.new(nil, @modules_data['config']['vars'].inject({}) do |vars, var|
        vars[var['name']] = var['value']
        vars
      end)

      @vars = Vars.new(@vars, @data['vars'])

      resolve
    end

    def resolve
      if @data['modules']['activate'].empty?
        @data['modules']['activate'] = @modules_data['modules'].keys
      end

      @modules = @data['modules']['activate'].inject([]) do |modules, name|
        modules << name
        if @modules_data['modules'][name]['requires']
          @modules_data['modules'][name]['requires'].map do |req|
            modules << req unless modules.include?(req)
          end
        end
        modules
      end

      (0..@modules.length-1).each do |index|
        name = @modules[index]
        @modules_data['modules'][name]['requires'].each do |req|
          rindex = @modules.index(req)
          if rindex > index
            @modules[index], @modules[rindex] = @modules[rindex], @modules[index]
          end
        end if @modules_data['modules'][name] && @modules_data['modules'][name]['requires']
      end

      @modules = @modules.map do |name|
        Module.new(name, @modules_data['modules'][name], @vars, @loader, renderer)
      end

      @modules = @modules.inject({}) do |mods, mod|
        mods[mod.id] = mod
        mods
      end

    end

    def renderer
      return @renderer if @renderer
      templates = "Workshopper::Templates::#{@data['content']['templates'].classify}".constantize.new
      @renderer = "Workshopper::Renderer::#{@data['content']['renderer'].classify}".constantize.new(id, templates)
    end

    def asset(path)
      @loader.get(path)
    end

    def id
      @data['id']
    end

    def name
      @data['name']
    end

    def modules
      @modules
    end

    def to_json
      {
          id: @data['id'],
          name: @data['name']
      }.to_json
    end

  end
end