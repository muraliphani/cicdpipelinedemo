#!groovy

node()
	{
		def mavenHome
		
		
		withEnv([

                'MAVEN_HOME=' + mavenHome,

                "PATH=${mavenHome}/bin:${env.PATH}"

		]) 
		stage("Build&UT")
		{
		checkout scm
		mavenHome = tool(name: 'maven 3.5', type: 'maven');
		sh"cd /devtest1"
		sh "${mavenHome}/bin/mvn install"
		}
	}	
 

