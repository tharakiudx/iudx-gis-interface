pipeline {
  environment {
    devRegistry = 'ghcr.io/karun-singh/gis-dev'
    deplRegistry = 'ghcr.io/karun-singh/gis-depl'
    testRegistry = 'ghcr.io/karun-singh/gis-test:latest'
    registryUri = 'https://ghcr.io'
    registryCredential = 'karun-ghcr'
    GIT_HASH = GIT_COMMIT.take(7)
  }
  agent { 
    node {
      label 'slave1' 
    }
  }
  stages {

    stage('Building images') {
      steps{
        script {
          echo 'Pulled - ' + env.GIT_BRANCH
          // devImage = docker.build( devRegistry, "-f ./docker/dev.dockerfile .")
          // deplImage = docker.build( deplRegistry, "-f ./docker/depl.dockerfile .")
          testImage = docker.build( testRegistry, "-f ./docker/test.dockerfile .")
        }
      }
    }

    stage('Run Unit Tests and CodeCoverage test'){
      steps{
        script{
          sh 'docker-compose -f docker-compose.test.yml up test'
        }
      }
    }

    stage('Capture Unit Test results'){
      steps{
        xunit (
          thresholds: [ skipped(failureThreshold: '1'), failed(failureThreshold: '5') ],
          tools: [ JUnit(pattern: 'target/surefire-reports/*.xml') ]
        )
      }
      post{
        failure{
          error "Test failure. Stopping pipeline execution!"
        }
      }
    }

    stage('Capture Code Coverage'){
      steps{
        jacoco classPattern: 'target/classes', execPattern: 'target/jacoco.exec', sourcePattern: 'src/main/java'
      }
    }

    stage('Run GIS interface server'){
      steps{
        script{
            sh 'scp src/test/resources/IUDX-GIS-SERVER.postman_collection.json jenkins@jenkins-master:/var/lib/jenkins/iudx/gis/Newman/'
            sh 'docker-compose -f docker-compose.test.yml up -d perfTest'
            sh 'sleep 45'
        }
      }
    }

    stage('Integration Tests & ZAP pen test'){
      steps{
        node('master') {
          script{
            startZap ([host: 'localhost', port: 8090, zapHome: '/var/lib/jenkins/tools/com.cloudbees.jenkins.plugins.customtools.CustomTool/OWASP_ZAP/ZAP_2.11.0'])
              sh 'curl http://127.0.0.1:8090/JSON/pscan/action/disableScanners/?ids=10096'
              sh 'HTTP_PROXY=\'127.0.0.1:8090\' newman run /var/lib/jenkins/iudx/di/Newman/IUDX-GIS-SERVER.postman_collection.json -e /home/ubuntu/configs/gis-postman-env.json --insecure -r htmlextra --reporter-htmlextra-export /var/lib/jenkins/iudx/gis/Newman/report/report.html'
            runZapAttack()
          }
        }
      }
      post{
        always{
          node('master') {
            script{
              archiveZap failHighAlerts: 1, failMediumAlerts: 1, failLowAlerts: 15 
            }  
            publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: '/var/lib/jenkins/iudx/gis/Newman/report/', reportFiles: 'report.html', reportName: 'HTML Report', reportTitles: '', reportName: 'Integration Test Report'])
          }
          script{
             sh 'docker-compose -f docker-compose.test.yml down --remove-orphans'
          }
        }
      }
    }

    stage('Push Image') {
      when{
        expression {
          return env.GIT_BRANCH == 'origin/master';
        }
      }
      steps{
        script {
          docker.withRegistry( registryUri, registryCredential ) {
            devImage.push("3.0-${env.GIT_HASH}")
            deplImage.push("3.0-${env.GIT_HASH}")
          }
        }
      }
    }
  }
}