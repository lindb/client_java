PHONY: help

# Ref: https://gist.github.com/prwhite/8168133
help:  ## display help
	@awk 'BEGIN {FS = ":.*##"; printf "\nUsage:\n  make \033[36m<target>\033[0m\n\nTargets:\n"} \
		/^[a-zA-Z_-]+:.*?##/ { printf "  \033[36m%-10s\033[0m %s\n", $$1, $$2 }' $(MAKEFILE_LIST)

header: ## check and add license header.
	mvn license:format

test: ## run test cases
	# mvn test -Dtest=WriteImplTest
	mvn test

generate: ## generate proto code
	mvn generate-sources

deploy: ## deploy to mvn repo
	# install gpg https://docs.releng.linuxfoundation.org/en/latest/gpg.html
	# https://central.sonatype.org/publish/requirements/gpg/#listing-keys
	# export GPG_TTY=$(tty)
	mvn clean deploy

update: ## update project/classpath 
	mvn eclipse:clean eclipse:eclipse

run-write-point: ## run write point example
	mvn test-compile exec:java -Dexec.mainClass="io.lindb.client.example.WritePoint" -Dexec.classpathScope="test"
