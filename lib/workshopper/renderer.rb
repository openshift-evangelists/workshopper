module Workshopper
  class Renderer

    def self.get(id)
      case id
      when 'adoc', :adoc
        require 'workshopper/renderer/adoc'
        Adoc.new
      when 'md', :markdown
        require 'workshopper/renderer/markdown'
        Markdown.new
      end
    end

  end
end