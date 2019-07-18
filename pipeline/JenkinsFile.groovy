#!groovy

node()
	{
		def mavenHome
		
			
		
		stage("Checkout SCM"){
			checkout scm
			mavenHome = tool(name: 'maven-3.5.0', type: 'maven');
			
		}
		
		withEnv([

                'MAVEN_HOME=' + mavenHome,

                "PATH=${mavenHome}/bin:${env.PATH}"

		]){

		stage("Build & UT"){
		sh"cd devtest1"
		sh "${mavenHome}/bin/mvn install"
		}
		}
		
	
		
}	
 

