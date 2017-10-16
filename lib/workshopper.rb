require 'workshopper/version'

require 'workshopper/workshop'
require 'workshopper/deployment'
require 'workshopper/loader'
require 'workshopper/loader/file'
require 'workshopper/loader/http'
require 'workshopper/module'
require 'workshopper/renderer/asciidoc'
require 'workshopper/renderer/markdown'
require 'workshopper/templates/liquid'
require 'workshopper/vars'
require 'workshopper/webapp'

require 'rack/mime'

Rack::Mime::MIME_TYPES['.adoc'] = 'text/asciidoc'
Rack::Mime::MIME_TYPES['.md'] = 'text/markdown'
