module Workshopper
  class Vars

    attr_accessor :parent

    def initialize(parent=nil, data={})
      @parent = parent
      @data = data
    end

    def [](name)
      @data[name] || (@parent ? @parent[name] : nil)
    end

    def []=(name, value)
      @data[name] = value
    end

    def merge
      base = @parent ? @parent.merge : {}
      base.merge(@data || {})
    end

    def to_json
      merge.to_json
    end

  end
end