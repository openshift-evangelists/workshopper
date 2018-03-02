require 'asciidoctor'

module Workshopper
  class Renderer
    class Adoc

      def render(workshop, content)
        content = Asciidoctor::Document.new(content, attributes: {
            'icons' => 'font',
            'imagesdir' => "/workshop/#{workshop}/asset/images",
            'source-highlighter' => 'highlightjs',
        })
        content.convert
      end

    end
  end
end