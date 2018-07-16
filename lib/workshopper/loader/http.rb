require 'faraday'
require 'digest'

module Workshopper
  class Loader
    class Http

      class << self

        def get(path)
          Faraday.get(path).body
        end

      end

      def cache?
        ENV['ENABLE_CONTENT_CACHE']
      end

      def initialize(uri)
        @uri = uri
        @client = Faraday.new(url: "#{@uri.scheme}://#{@uri.host}") do |faraday|
          faraday.request :url_encoded
          faraday.adapter :excon
        end
      end

      def download(path)
        response = @client.get(path)
        response.body
      end

      def get(path)
        full_path = ::File.join(@uri.path, path)
        hash = Digest::SHA256.hexdigest(full_path)

        Dir.mkdir('cache') unless ::File.directory?('cache')

        cache = "cache/#{hash}.cache"

        content = if cache? && ::File.exist?(cache)
          ::File.read(cache)
        else
          download(full_path)
        end

        ::File.write(cache, content) if cache?

        if Rack::Mime.match?(Rack::Mime.mime_type(::File.extname(path)), 'text/*')
          content = content.force_encoding('UTF-8')
        end

        content
      end

    end
  end
end