DOCKER_ORG=usgseros
LCMAP_REST_REPO=ubuntu-lcmap-rest
LCMAP_AUTH_REPO=ubuntu-lcmap-test-auth-server
LCMAP_NGINX_REPO=debian-lcmap-nginx
DOCKERHUB_LCMAP_REST = $(DOCKER_ORG)/$(LCMAP_REST_REPO):$(VERSION)
DOCKERHUB_LCMAP_TEST_AUTH = $(DOCKER_ORG)/$(LCMAP_AUTH_REPO):$(VERSION)
DOCKERHUB_LCMAP_NGINX = $(DOCKER_ORG)/$(LCMAP_NGINX_REPO):$(VERSION)
LCMAP_REST_DEPLOY = lcmap-rest-deploy:$(VERSION)

.PHONY: docker

docker: docker-auth-build docker-server-build docker-nginx-build

docker-server-build: CONTEXT=./docker/lcmap-rest-server
docker-server-build: BUILD_DIR=$(CONTEXT)/build
docker-server-build:
	@mkdir -p $(BUILD_DIR)
	@rm -rf $(BUILD_DIR)
	-@cp -r . $(BUILD_DIR)
	-@rm -rf \
	$(BUILD_DIR)/.* \
	$(BUILD_DIR)/downloads \
	$(BUILD_DIR)/target \
	$(BUILD_DIR)/docker \
	$(BUILD_DIR)/checkouts/*
	@cp -r ../lcmap-client-clj $(BUILD_DIR)/checkouts/
	@docker build -t $(DOCKERHUB_LCMAP_REST) $(CONTEXT)
	@rm -rf $(BUILD_DIR)

docker-deploy-build: CONTEXT=./docker/lcmap-rest-deploy
docker-deploy-build: BUILD_DIR=$(CONTEXT)/build
docker-deploy-build:
	-@mkdir -p $(BUILD_DIR)
	@rm -rf $(BUILD_DIR)/*
	@cp ~/.usgs/lcmap.ini $(BUILD_DIR)
	@docker build -t $(LCMAP_REST_DEPLOY) $(CONTEXT)
	@rm -rf $(BUILD_DIR)


docker-auth-build: CONTEXT=./docker/test-auth-server
docker-auth-build: BUILD_DIR=$(CONTEXT)/build
docker-auth-build:
	@mkdir -p $(BUILD_DIR)
	@rm -rf $(BUILD_DIR)
	-@cp -r test/support/auth-server $(BUILD_DIR)
	-@rm -rf $(BUILD_DIR)/target
	@docker build -t $(DOCKERHUB_LCMAP_TEST_AUTH) $(CONTEXT)
	@rm -rf $(BUILD_DIR)

docker-nginx-build: CONTEXT=./docker/nginx
docker-nginx-build:
	@docker build -t $(DOCKERHUB_LCMAP_NGINX) $(CONTEXT)

docker-server:
	@docker run \
	-e "LCMAP_SERVER_ENV_DB_HOSTS=$(CASSANDRA_HOST):" \
	-e "LCMAP_SERVER_ENV_AUTH_USGS_ENDPOINT=$(AUTH_ENDPOINT)" \
	-e "LCMAP_USERNAME=alice" \
	-e "LCMAP_PASSWORD=secret" \
	-e "LCMAP_ENDPOINT=http://localhost:1077" \
	-e "LCMAP_VERSION=0.0" \
	-e "LCMAP_LOG_LEVEL=debug" \
	-e "LCMAP_CONTENT_TYPE=json" \
	-t $(DOCKERHUB_LCMAP_REST)

docker-server-publish:
	@docker push $(DOCKERHUB_LCMAP_REST)

docker-server-bash:
	@docker run \
	-e "LCMAP_SERVER_ENV_DB_HOSTS=$(CASSANDRA_HOST)" \
	-e "LCMAP_USERNAME=alice" \
	-e "LCMAP_PASSWORD=secret" \
	-e "LCMAP_ENDPOINT=http://localhost:1077" \
	-e "LCMAP_VERSION=0.0" \
	-e "LCMAP_LOG_LEVEL=debug" \
	-e "LCMAP_CONTENT_TYPE=json" \
	-it --entrypoint=/bin/bash $(DOCKERHUB_LCMAP_REST) -s

docker-server-repl:
	@docker run \
	-e "LCMAP_SERVER_ENV_DB_HOSTS=$(CASSANDRA_HOST)" \
	-e "LCMAP_USERNAME=alice" \
	-e "LCMAP_PASSWORD=secret" \
	-e "LCMAP_ENDPOINT=http://localhost:1077" \
	-e "LCMAP_VERSION=0.0" \
	-e "LCMAP_LOG_LEVEL=debug" \
	-e "LCMAP_CONTENT_TYPE=json" \
	-it --entrypoint=/lcmap-rest/bin/repl \
	$(DOCKERHUB_LCMAP_REST)

docker-deploy:
	@docker run -t $(LCMAP_REST_DEPLOY)

docker-deploy-bash:
	@docker run -it --entrypoint=/bin/bash $(LCMAP_REST_DEPLOY) -s

docker-deploy-repl:
	@docker run -it --entrypoint=/lcmap-rest/bin/repl $(LCMAP_REST_DEPLOY)

docker-auth:
	@docker run -t $(DOCKERHUB_LCMAP_TEST_AUTH)

docker-auth-bash:
	@docker run -it --entrypoint=/bin/bash $(DOCKERHUB_LCMAP_TEST_AUTH) -s

docker-auth-publish:
	@docker push $(DOCKERHUB_LCMAP_TEST_AUTH)

docker-nginx:
	@docker run --privileged -t -p 80:80 $(DOCKERHUB_LCMAP_NGINX)

docker-nginx-bash:
	@docker run --privileged -it --entrypoint=/bin/bash $(DOCKERHUB_LCMAP_NGINX) -s

docker-nginx-publish:
	@docker push $(DOCKERHUB_LCMAP_NGINX)

docker-publish: docker-server-publish docker-auth-publish docker-nginx-publish

dockerhub: docker docker-publish

clean-docker:
	-@docker rm $(shell docker ps -a -q)
	-@docker rmi $(shell docker images -q --filter 'dangling=true')
