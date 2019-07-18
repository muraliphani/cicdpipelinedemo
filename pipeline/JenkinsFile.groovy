#!groovy

node()
	{
		def mavenHome
		
		stage("Build&UT")
		{
		checkout scm
		mavenHome = tool(name: 'maven 3.6', type: 'maven');
		sh"cd /devtest1"
		sh "${mvnHome}/bin/mvn install"
		}
	}	
 

