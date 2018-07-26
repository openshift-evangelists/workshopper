Rails.application.routes.draw do

  mount ActionCable.server => '/cable'

  get '/dashboard', to: 'welcome#dashboard'
  get '/workshop/:workshop', to: 'welcome#workshop'
  get '/workshop/:workshop/lab/:lab', to: 'welcome#lab'
  get '/workshop/:workshop/complete', to: 'welcome#complete'
  get '/workshop/:workshop/asset/*path.:ext', to: 'welcome#asset'

  root 'welcome#index'
  
end
