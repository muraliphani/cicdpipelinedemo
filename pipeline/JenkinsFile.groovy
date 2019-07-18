#!groovy

node()
	{
		def mavenHome
		
		stage("Checkout SCM"){
			checkout scm
		mavenHome = tool(name: 'maven 3.5', type: 'maven');
		sh"cd /devtest1"
		sh "${mavenHome}/bin/mvn install"
		}
		
	
		
}	
 

