#!groovy

node()
	{
		def mavenHome
		
		stage("Checkout SCM"){
			checkout scm
			
		}
		
		stage("Build & UT"){
		def mvnHome = tool name: 'MAVEN_HOME', type: 'maven'
		def directory = "loginforum1"
		dir(directory){
		sh "${mvnHome}/bin/mvn install"
					}
			}
			
		stage("SonarQube analysis") {
		
				def directory = "loginforum1"
          dir(directory){
              withSonarQubeEnv('sonar') {
                 sh 'mvn clean package sonar:sonar'
				 sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
              }
			  }
          
      }
	  
	  stage('Check Quality Gate') {
        
                echo 'Checking quality gate...'
                timeout(time: 5, unit: 'MINUTES') {
                        def swait = waitForQualityGate()
                        if (swait.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${swait.status}"
                        }
                 }
		}
	  
	  
	  stage("App deployment"){
	  def mvnHome = tool name: 'MAVEN_HOME', type: 'maven'
	  def directory = "loginforum1"
	  dir(directory){
		sh "${mvnHome}/bin/mvn clean package"
	  
	  	   pushToCloudFoundry(
                   target: 'https://api.run.pivotal.io',
                   organization: 'cicdpipeline',
                   cloudSpace: 'development',
                   credentialsId: '89671dff-c2c4-4016-a6fe-49904b8563d5',
                   manifestChoice: [manifestFile: 'manifest.yml']
                   )
				 }
			}	 
	  
	  
	}	


