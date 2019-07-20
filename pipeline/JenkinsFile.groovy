#!groovy

node()
	{
		def mavenHome
		stage("Checkout SCM"){
			checkout scm
			sh "cd devtest1"
		}
		stage("Build & UT"){
		def mvnHome = tool name: 'MAVEN_HOME', type: 'maven'
		def presetdir = pwd()
		echo "$presetdir"
		sh "cd ${presetdir}/devtest1"
		def dire = pwd()
		echo "$dire"
		sh "${mvnHome}/bin/mvn clean"
		
		}
	}	
 

