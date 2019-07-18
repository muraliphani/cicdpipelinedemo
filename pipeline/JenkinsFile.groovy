#!groovy

node()
	{
		def mavenHome
		
			withEnv([

                'MAVEN_HOME=' + mavenHome,

                "PATH=${mavenHome}/bin:${env.PATH}"

		])
		
		stage("Checkout SCM"){
			checkout scm
		sh"cd /devtest1"
		sh "${mavenHome}/bin/mvn install"
		}
		
	
		
}	
 

