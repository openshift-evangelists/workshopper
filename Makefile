build:
	docker build --no-cache -t osevg/workshopper:ruby .
release: build
	docker push osevg/workshopper:ruby
