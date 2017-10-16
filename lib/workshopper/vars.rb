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

    def keys
      ((@parent ? @parent.keys : []) + @data.keys).uniq
    end

    def merge
      base = @parent ? @parent.merge : {}
      base.merge(@data || {})
    end

    def to_json
      merge.to_json
    end

    def to_s(indent=0)
      out = ''
      @data.keys.each do |key|
        out << (' ' * indent) + "#{key} = #{@data[key]}\n"
      end
      out << @parent.to_s(indent + 2) if @parent
      out
    end

  end
end