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
		def presetdir = pwd()
		echo $presetdir
		sh "${mvnHome}/bin/mvn clean"

		}
	}	
 

