module Workshopper
  class Renderer

    def self.get(id)
      case id
      when 'adoc', :adoc
        require 'workshopper/renderer/adoc'
        Adoc.new
      end
    end

  end
end