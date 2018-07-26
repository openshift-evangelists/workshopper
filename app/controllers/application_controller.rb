require 'securerandom'

class ApplicationController < ActionController::Base

  protect_from_forgery with: :exception

  before_action do
    session[:guest_id] ||= SecureRandom.uuid
  end



end
