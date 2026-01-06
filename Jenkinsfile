pipeline {
    agent any

    environment {
        ACE_HOME = "/opt/IBM"
        APP_NAME = "CSV-COBOL"
        BAR_NAME = "TEST-${BUILD_NUMBER}.bar"
        DISPLAY = ":99"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/bmanoj27/cp4idevacerepo.git'
            }
        }
		
		stage('OWASP Dependency Check') {
			steps {
                withCredentials([string(credentialsId: 'nvd-api-key', variable: 'NVD_API_KEY')]) {
				dependencyCheck additionalArguments: '--scan "${WORKSPACE}" --nvdApiKey "${NVD_API_KEY}" --format HTML --format XML', odcInstallation: 'OWASP-DepCheck-12'
				dependencyCheckPublisher pattern: 'dependency-check-report.xml'
			}}
		}

        stage('Create BAR') {
            steps {
                sh '''
                # Start Xvfb only if not already running
                if ! pgrep -f "Xvfb ${DISPLAY}" > /dev/null; then
                  Xvfb ${DISPLAY} -screen 0 800x600x8 -nolisten tcp &
                  sleep 2
                fi
				
				if [ ! -d bars ]; then
                mkdir bars
                rm -rf bars/*
                fi
				
				APPS=$(find . -maxdepth 2 -name application.descriptor | awk -F/ '{print $(NF-1)}' | tr '\\n' ' ')
				
				for APP in ${APPS}; do
                ${ACE_HOME}/tools/mqsicreatebar \
                  -data ${WORKSPACE} \
                  -b bars/${APP}/${APP}-${BUILD_NUMBER}.bar \
                  -a ${APP}
				done
				
                '''
            }
        }

        stage('Upload') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'nexusrepo',
                    usernameVariable: 'ART_USER',
                    passwordVariable: 'ART_PASS'
                )]) {
                    sh '''
					APPS=$(find . -maxdepth 2 -name application.descriptor | awk -F/ '{print $(NF-1)}' | tr '\\n' ' ')
                    for APP in ${APPS}; do
					curl -u ${ART_USER}:${ART_PASS} \
                      --upload-file bars/${APP}/${APP}-${BUILD_NUMBER}.bar \
                      http://localhost:8081/repository/acenexusrepo/acebars/${APP}/${APP}-${BUILD_NUMBER}.bar
                    done
                    '''
                }
            }
        }
    }
}
