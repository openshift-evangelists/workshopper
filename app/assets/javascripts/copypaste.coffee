copypastecode = ->
  clipboard = new ClipboardJS('.copypaste', {
    target: (trigger) -> trigger
  })

$(copypastecode)