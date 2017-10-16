build:
	docker build -t osevg/workshopper:ruby .
release: build
	docker push osevg/workshopper:ruby
