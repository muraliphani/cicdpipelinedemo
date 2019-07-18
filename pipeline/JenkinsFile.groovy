#!groovy

node()
	{
		def mvnHome
		
		stage("Build&UT")
		{
		checkout scm
		mvnHome = tool(name: 'maven 3.5', type: 'maven');
		sh"cd /devtest1"
		sh "${mvnHome}/bin/mvn install"
		}
	}	
 

