#!groovy

node()
	{
		def mavenHome
		
		stage("Checkout SCM"){
			checkout scm
			
				}
		
		stage("Build & UT"){
		def mvnHome = tool name: 'MAVEN_HOME', type: 'maven'
		def directory = "devtest1"
		dir(directory){
		sh "${mvnHome}/bin/mvn install"
					}
			}
			
		stage("build & SonarQube analysis") {
          
              withSonarQubeEnv('sonar') {
                 sh 'mvn clean package sonar:sonar'
              }
          
      }

      stage("Quality Gate"){
          timeout(time: 1, unit: 'HOURS') {
              def qg = waitForQualityGate()
              if (qg.status != 'OK') {
                  error "Pipeline aborted due to quality gate failure: ${qg.status}"
              }
          }
      }
			
	}	
 

