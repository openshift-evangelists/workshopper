require 'faraday'

module Workshopper
  class Loader
    class Http

      class << self

        def get(path)
          Faraday.get(path).body
        end

      end

      def initialize(uri)
        @uri = uri
        @client = Faraday.new(url: "#{@uri.scheme}://#{@uri.host}") do |faraday|
          faraday.request  :url_encoded
          faraday.adapter  :excon
        end
      end

      def get(path)
        @client.get(::File.join(@uri.path, path)).body.force_encoding('UTF-8')
      end

    end
  end
end