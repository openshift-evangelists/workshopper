module Workshopper
  class Loader
    class File

      class << self

        def get(path)
          path = path.sub('file://', '')
          ::File.open(path, 'r:UTF-8', &:read)
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