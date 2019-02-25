require 'asciidoctor'

include Rails.application.routes.url_helpers

module Workshopper
  class Renderer
    class Adoc

      def render(workshop, content)
        imagesdir = url_for(controller: 'welcome', action: 'asset',
                            workshop: workshop, path: 'images', ext: 'ext',
                            only_path: true)
        imagesdir.delete_suffix!('.ext')

        content = Asciidoctor::Document.new(content, attributes: {
            'icons' => 'font',
            'imagesdir' => imagesdir,
            'source-highlighter' => 'coderay',
        })
        content.convert
      end

    end
  end
end
