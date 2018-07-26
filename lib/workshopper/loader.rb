require 'uri'
require 'active_support/core_ext/string/inflections'

module Workshopper
  class Loader

    class << self

      def adapter(scheme)
        @adapters ||= {}
        require "workshopper/loader/#{scheme}"
        @adapters[scheme] ||= "Workshopper::Loader::#{scheme.classify}".constantize
      end

      def get(path)
        uri = URI(path)
        adapter(uri.scheme).get(path)
      end

    end

    def initialize(base)
      @uri = URI(base)
      @adapter = self.class.adapter(@uri.scheme).new(@uri)
    end

    def get(path)
      @adapter.get(path)
    end

  end
end