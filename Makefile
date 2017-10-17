build:
	docker build -t osevg/workshopper:latest .
release: build
	docker push osevg/workshopper:latest
