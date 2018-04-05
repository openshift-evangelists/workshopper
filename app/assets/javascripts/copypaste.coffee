copypastecode = ->
  alert('copypaste')
  clipboard = new ClipboardJS('.copypaste', {
    target: (trigger) -> trigger
  })

document.addEventListener("turbolinks:load", copypastecode)