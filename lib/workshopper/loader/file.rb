module Workshopper
  class Loader
    class File

      class << self

        def get(path)
          path = path.sub('file://', '')
          ::File.read(path)
        end

      end

      def initialize(uri)
        @uri = uri
      end

      def get(path)
        self.class.get(::File.join(@uri.path, path))
      end

    end
  end
end