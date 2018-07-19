require 'coderay'
require 'kramdown'

module Workshopper
  class Renderer
    class Markdown

      def self.markdown
      end

      def render(workshop, content)
        opts = {
          syntax_highlighter: 'coderay',
          syntax_highlighter_opts: {
            css: 'class',
            line_numbers: nil
          }
        }
        Kramdown::Document.new(content.force_encoding('UTF-8'), opts).to_html
      end

    end

  end
end