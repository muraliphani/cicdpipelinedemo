#!groovy

node()
	{
		def mavenHome
		
			
		
		stage("Checkout SCM"){
			checkout scm
			
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
 

