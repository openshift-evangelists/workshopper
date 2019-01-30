source 'https://rubygems.org'

gem 'rails', '~> 5.2'
gem 'puma', '~> 3.7'
gem 'sass-rails', '~> 5.0'
gem 'uglifier', '>= 1.3.0'
gem 'coffee-rails', '~> 4.2'
gem 'turbolinks', '~> 5'
gem 'jbuilder', '~> 2.5'
gem 'redis', '~> 4.0'
gem 'bcrypt', '~> 3.1.7'
#gem 'autoprefixer-rails', '~> 8.0'
gem 'asciidoctor'
gem 'kramdown'
gem 'coderay'
gem 'liquid'

gem 'faraday'
gem 'excon'

gem 'font-awesome-rails'
gem 'bootstrap', '~> 4.0'
gem 'jquery-rails'

platforms :ruby do 
  gem 'sqlite3'
  gem 'mini_racer'
end

platforms :jruby do 
  gem 'activerecord-jdbcsqlite3-adapter'
end

source 'https://rails-assets.org' do
  gem 'rails-assets-chartjs'
end

group :development, :test do
  gem 'byebug', platforms: [:mri, :mingw, :x64_mingw]
end

group :development do
  gem 'web-console', '>= 3.3.0'
  gem 'listen', '>= 3.0.5', '< 3.2'
  gem 'spring'
  gem 'spring-watcher-listen', '~> 2.0.0'
end

gem 'tzinfo-data', platforms: [:mingw, :mswin, :x64_mingw, :jruby]
