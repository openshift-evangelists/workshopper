class PageViewChannel < ApplicationCable::Channel

  class << self

    def counts
      @counts ||= {}
    end

  end

  def subscribed
    @uri = params[:path]
    @guest = params[:guest]

    self.class.counts[@uri] ||= {}
    self.class.counts[@uri][@guest] ||= 0
    self.class.counts[@uri][@guest] += 1
  end

  def unsubscribed
    self.class.counts[@uri][@guest] -= 1
    count = self.class.counts[@uri].keys.inject(0) do |c, i|
      c + self.class.counts[@uri][i]
    end
    self.class.counts.delete(@uri) if count == 0
  end

end
