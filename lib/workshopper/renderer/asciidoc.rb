require 'asciidoctor'

module Workshopper
  module Renderer
    class Asciidoc

      def initialize(workshop, templates)
        @workshop = workshop
        @templates = templates
      end

      def extension
        'adoc'
      end

      def render(content, env)
        content = @templates.render(content, env)
        content = Asciidoctor::Document.new(content, attributes: {
            'icons' => 'font',
            'imagesdir' => '/api/workshops/' + @workshop + '/content/assets/images',
            'source-highlighter' => 'highlightjs',
        })
        content.convert
      end

    end
  end
end