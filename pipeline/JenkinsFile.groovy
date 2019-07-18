#!groovy

node()
	{
		def mavenHome
		
		stage("Checkout SCM"){
			checkout scm
		}
		
		
		stage("Build&UT")
		{
		mavenHome = tool(name: 'maven 3.5', type: 'maven');
		sh"cd /devtest1"
		sh "${mavenHome}/bin/mvn install"
		}
		
		withEnv([

                'MAVEN_HOME=' + mavenHome,

                "PATH=${mavenHome}/bin:${env.PATH}"

		])
		
}	
 

