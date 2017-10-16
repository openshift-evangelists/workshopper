require 'kramdown'

module Workshopper
  module Renderer
    class Markdown

      def initialize(workshop, templates)
        @workshop = workshop
        @templates = templates
      end

      def extension
        'md'
      end

      def render(content, env)
        content = @templates.render(content, env)
        Kramdown::Document.new(content).to_html
      end

    end
  end
end