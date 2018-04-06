require 'redcarpet'
require 'rouge'
require 'rouge/plugins/redcarpet'

module Workshopper
  class Renderer
    class Markdown < Redcarpet::Render::HTML

      include Rouge::Plugins::Redcarpet

      def self.markdown
        @markdown ||= Redcarpet::Markdown.new(Markdown, extensions = {
          tables: true,
          fenced_code_blocks: true,
          autolink: true,
          strikethrough: true,
          superscript: true,
          underline: true,
          highlight: true,
          quote: true,
          footnotes: true
        })
      end

      def render(workshop, content)
        Markdown.markdown.render(content)
      end

    end
  end
end