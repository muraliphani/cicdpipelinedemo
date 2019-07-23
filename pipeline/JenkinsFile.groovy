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
		
				def directory = "devtest1"
          dir(directory){
              withSonarQubeEnv('sonar') {
                 sh 'mvn clean package sonar:sonar'
              }
			  }
          
      }

     
	}	
 

