workshops = ENV['WORKSHOPS_URLS']

if workshops
  workshops = workshops.split(',')
  workshops.each do |workshop|
    Workshopper::Cache.add(workshop)
  end
end