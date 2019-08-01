#!groovy

node()
	{
		def mavenHome
		
		stage("Checkout SCM"){
			checkout scm
			
				}
		
		stage("Build & UT"){
		def mvnHome = tool name: 'MAVEN_HOME', type: 'maven'
		def directory = "Banking"
		dir(directory){
		sh "${mvnHome}/bin/mvn install"
					}
			}
			
		stage("build & SonarQube analysis") {
		
				def directory = "Banking"
          dir(directory){
              withSonarQubeEnv('sonar') {
                 sh 'mvn clean package sonar:sonar'
				 sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
              }
			  }
          
      }
	  
	  
	  stage("App deployment"){
	  def mvnHome = tool name: 'MAVEN_HOME', type: 'maven'
	  def directory = "Banking"
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
 

