class WelcomeController < ApplicationController

  def index
    if Workshopper::Cache.workshops.keys.length == 1
      id = Workshopper::Cache.workshops.keys.first
      return redirect_to "/workshop/#{id}/"
    end

    @workshops = Workshopper::Cache.workshops.keys.map do |id|
      w = Workshopper::Cache.workshops[id]
      {
          id: w.id,
          name: w.name
      }
    end
  end

  def workshop
    @workshop = Workshopper::Cache.workshops[params[:workshop]]
    lab = @workshop.active_labs.first
    redirect_to "/workshop/#{params[:workshop]}/lab/#{lab}"
  end

  def lab
    @workshop = Workshopper::Cache.workshops[params[:workshop]]
    session[:completed_labs] ||= []
    session[:completed_labs] << params[:lab] unless session[:completed_labs].include?(params[:lab])
    @completed = session[:completed_labs]
    @lab = @workshop.lab(params[:lab])
  end

  def complete
    @workshop = Workshopper::Cache.workshops[params[:workshop]]
  end

  def asset
    @workshop = Workshopper::Cache.workshops[params[:workshop]]
    path = File.join(@workshop.prefix, params[:path])
    path += ".#{params[:ext]}"
    #return render plain: path
    send_data(Workshopper::Loader.get(path))
  end

  def dashboard

  end

end
