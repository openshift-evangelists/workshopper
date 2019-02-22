# This file is used by Rack-based servers to start the application.

require_relative 'config/environment'

class FixupScriptName
    def initialize(app)
        @app = app
    end

    def call(env)
        env['SCRIPT_NAME'] = ActionController::Base.config.relative_url_root
        env['PATH_INFO'].delete_prefix!(env['SCRIPT_NAME'])
        @app.call(env)
    end
end

if ActionController::Base.config.relative_url_root
    use FixupScriptName
end

run Rails.application
