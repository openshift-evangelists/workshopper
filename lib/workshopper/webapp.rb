require 'sinatra/base'
require 'json'

module Workshopper
  class Webapp < Sinatra::Base

    enable :static
    set :root, File.dirname(__FILE__)
    set :public_folder, 'public'

    helpers do
      def deployment
        @deployment ||= Workshopper::Deployment.new
      end
    end

    get '/' do
      redirect('/index.html')
    end

    get '/api/config' do
      content_type :json
      {}.to_json
    end

    get '/api/reload' do
      content_type :json
      {}.to_json
    end

    get '/api/workshops' do
      content_type :json
      deployment.workshops.values.map { |w| { id: w.id, name: w.name }}.to_json
    end

    get '/api/workshops/:name' do
      content_type :json
      modules = deployment.workshops[params[:name]] ? deployment.workshops[params[:name]].modules.values.map(&:id) : []
      {
        id: params[:name],
        modules: modules,
        name: deployment.workshops[params[:name]].name
      }.to_json
    end

    get '/api/workshops/:name/modules' do
      content_type :json
      deployment.workshops[params[:name]].modules.to_json
    end

    get '/api/workshops/:name/content/all' do
      content_type :json
    end

    get '/api/workshops/:name/content/module/:module' do
      content_type :html
      params
      deployment.workshops[params[:name]].modules[params[:module]].render
    end

    get '/api/workshops/:name/content/assets/*' do
      file = params[:splat].first
      deployment.workshops[params[:name]].asset(file)
    end

  end
end