require 'liquid'

module Workshopper
  module Templates
    class Liquid
      def render(content, env)
        ::Liquid::Template.register_tag('module_path', ModulePath)
        ::Liquid::Template.register_tag('module_name', ModuleName)
        ::Liquid::Template.register_tag('image_path', ImagePath)
        template = ::Liquid::Template.parse(content)
        template.render(env)
      end
    end

    class ModulePath < ::Liquid::Tag
      def initialize(tag_name, name, context)
        super
        @module_name = name.to_s.strip
      end

      def render(context)
        context.environments.first
        ws = context.environments.first['WORKSHOP_NAME']
        "/index.html#/workshop/#{ws}/module/#{@module_name}"
      end
    end

    class ImagePath < ::Liquid::Tag
      def initialize(tag_name, path, context)
        super
        @path = path.to_s.strip
      end

      def render(context)
        ws = context.environments.first['WORKSHOP_NAME']
        File.join("/api/workshops/#{ws}/content/assets/images", @path)
      end
    end

    class ModuleName < ::Liquid::Tag
      def initialize(tag_name, name, context)
        super
        @module_name = name.to_s.strip
      end

      def render(context)
        context.environments.first['modules'][@module_name]
      end
    end

  end
end
