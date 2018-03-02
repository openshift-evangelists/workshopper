App.report_page_view = (path, guest_id) ->

  options = {
    channel: "PageViewChannel",
    path: path,
    guest: guest_id
  }

  App.page_view = App.cable.subscriptions.create options,
    connected: ->

    disconnected: ->

    received: (data) ->
