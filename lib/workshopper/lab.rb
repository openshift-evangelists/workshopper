require 'asciidoctor'
require 'liquid'

require 'workshopper/loader'
require 'workshopper/renderer'

module Workshopper

  class Lab

    def initialize(content, id)
      @content = content
      @id = id
      @lab = @content.labs[@id]

      ::Liquid::Template.register_tag('module_path', ModulePath)
      ::Liquid::Template.register_tag('module_name', ModuleName)
      ::Liquid::Template.register_tag('image_path', ImagePath)
    end

    def name
      @lab['name']
    end

    def id
      @id
    end

    def duration
      @lab['duration']
    end

    def env(session)
      env = @content.env.dup
      @lab['vars'].each_pair do |key, value|
        env[key] = value if value && value != ''
      end if @lab['vars']
      env.each_key do |key|
        env[key] = ENV[key] if ENV[key]
      end.each_key do |key|
        env[key] = session[key] if session[key]
      end

      if env['DYNAMIC_USER_NAME'] || true
        @user_id ||= 0
        if env['NUM_USERS'] && env['NUM_USERS'] > 0 && @user_id >= Integer(env['NUM_USERS'])
          raise Exception.new('Not enough users in workshop environment')
        end
        env['USER_NAME'] = 'dev%02d'
        uid = session['user_id'] ||= (@user_id += 1)
        env['USER_NAME'] = env['USER_NAME'] % uid
      end

      env
    end

    def render(workshop, session)
      ext = 'adoc'
      @data ||= Workshopper::Loader.get(File.join(@content.prefix, "#{id}.#{ext}"))
      template = ::Liquid::Template.parse(@data)
      template = template.render(env(session))

      Workshopper::Renderer.get(ext).render(workshop, template)
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