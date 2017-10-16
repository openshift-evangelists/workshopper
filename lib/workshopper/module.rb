module Workshopper
  class Module

    attr_accessor :vars
    attr_reader :id

    def initialize(id, data, vars, loader, renderer)
      @id = id
      @data = data || {}
      @loader = loader
      @renderer = renderer

      @data['requires'] ||= []
      @data['vars'] ||= {}

      @vars = Vars.new(vars)

      @data['vars'].each_key do |name|
        @vars[name] = @data['vars'][name] if @data['vars'][name]
      end
    end

    def render(env={})
      id = @id.gsub('_', '/')
      content = @loader.get("#{id}.#{@renderer.extension}")
      parent = Vars.new(@vars, env)
      keys = parent.keys

      sysenv = ENV.to_hash
      sysenv = sysenv.keys.inject({}) do |vars, var|
        if keys.include?(var)
          vars[var] = sysenv[var]
        end
        vars
      end
      env = Vars.new(parent, sysenv).merge

      @renderer.render(content, env)
    end

    def requires
      @data['requires']
    end

    def requires?(name)
      @data['requires'].include?(name)
    end

    def to_json(_)
      {
          id: @data['id'],
          name: @data['name'],
          requires: @data['requires']
      }.to_json
    end

  end
end