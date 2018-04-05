require 'kramdown'

module Workshopper
  class Renderer
    class Markdown

      def render(workshop, content)
        content = Kramdown::Document.new(content)
        content.to_html
      end

    end
  end
end