workshops = ENV['WORKSHOPS_URLS']

require 'workshopper'

if workshops
  workshops = workshops.split(',')
  workshops.each do |workshop|
    Workshopper::Cache.add(workshop)
  end
end

