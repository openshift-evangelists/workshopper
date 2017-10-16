module Workshopper
  class Deployment

    attr_reader :workshops

    def initialize
      @workshops = {}

      if ENV['WORKSHOPS_URLS']
        ENV['WORKSHOPS_URLS'].split(',').each do |url|
          add(url)
        end
      end


      if ENV['WORKSHOPS_LIST_URL']
        YAML.load(Loader.get(ENV['WORKSHOPS_LIST_URL'])).each do |url|
          add(url)
        end
      end

    end

    def add(url)
      workshop = Workshop.new(url)
      @workshops[workshop.id] = workshop
    end

  end
end