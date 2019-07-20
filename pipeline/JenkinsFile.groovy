#!groovy

node()
	{
		def mavenHome
		stage("Checkout SCM"){
			checkout scm
		}
		stage("Build & UT"){
		def mvnHome = tool name: 'MAVEN_HOME', type: 'maven'
		sh"cd devtest1"
		sh "${mvnHome}/bin/mvn install"

		}
		
		
	}	
 

