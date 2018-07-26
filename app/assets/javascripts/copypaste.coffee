copypastecode = ->
  clipboard = new ClipboardJS('.copypaste', {
    target: (trigger) -> trigger
  })

document.addEventListener("turbolinks:load", copypastecode)