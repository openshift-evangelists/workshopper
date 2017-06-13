require 'asciidoctor'

require 'liquid'
require 'liquid/tags/assign'
require 'liquid/tags/break'
require 'liquid/tags/capture'
require 'liquid/tags/case'
require 'liquid/tags/comment'
require 'liquid/tags/continue'
require 'liquid/tags/cycle'
require 'liquid/tags/decrement'
require 'liquid/tags/for'
require 'liquid/tags/if'
require 'liquid/tags/ifchanged'
require 'liquid/tags/include'
require 'liquid/tags/increment'
require 'liquid/tags/raw'
require 'liquid/tags/table_row'
require 'liquid/tags/unless'

require 'net/http'

class Wrapper

  def initialize
    @name = ''
    @data = ''
    @env = {}
  end

  def put(name, value)
    @env[name] = value
  end

  def render
    @template = Liquid::Template.parse(@data)
    @adoc = @template.render!(@env)

    @options = {
        'icons' => 'font',
        'imagesdir' => '/api/workshops/' + @name + '/content/assets/images',
        'source-highlighter' => 'highlightjs'
    }

    Asciidoctor::Document.new(@adoc, { :attributes => @options }).convert
  end

end