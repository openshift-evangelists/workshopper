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

    def env(workshop, session)
      env = {
        'WORKSHOP_NAME' => workshop
      }

      @content.env.each_pair do |name, value|
        env[name] = value
      end if @content.env

      @content.workshop.vars.each_pair do |name, value|
        env[name] = value
      end if @content.workshop.vars

      @lab['vars'].each_key do |key|
        env[key] = @lab['vars'][key] if @lab['vars'][key]
      end if @lab['vars']

      env.each_key do |key|
        env[key] = ENV[key] if ENV[key]
      end

      env.each_key do |key|
        env[key] = session[key] if session[key]
      end

      if ENV['DYNAMIC_USER_NAME']
        @user_id ||= 0
        if ENV['NUM_USERS'] && Integer(ENV['NUM_USERS']) > 0 && @user_id >= Integer(ENV['NUM_USERS'])
          raise Exception.new('Not enough users in workshop environment')
        end
        ENV['USER_NAME'] ||= 'user%d' #'user%02d'
        uid = (session['user_id'] ||= (@user_id += 1))
        env['USER_NAME'] = ENV['USER_NAME'] % uid
      end

      env
    end

    def render(workshop, session)
      @data ||= Workshopper::Loader.get(File.join(@content.prefix, "#{id}.#{@content.ext}"))
      template = ::Liquid::Template.parse(@data)
      template = template.render(env(workshop, session))

      Workshopper::Renderer.get(@content.ext).render(workshop, template)
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
        File.join("/workshop/#{ws}/asset/images", @path)
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