require 'workshopper/workshop'

module Workshopper

  class Cache

    def self.workshops
      @workshops ||= {}
    end

    def self.add(url)
      workshop = Workshop.new(url)
      workshops[workshop.id] ||= workshop
    end

  end

end