require 'liquid'

module Workshopper
  module Templates
    class Liquid

      def render(content, env)
        template = ::Liquid::Template.parse(content)
        template.render(env)
      end

    end
  end
end