#!groovy

node()
	{
		def mavenHome
		stage("Checkout SCM"){
			checkout scm
		}
		
		withEnv(["JAVA_HOME=${ tool 'jdk-1.8.0_64bits' }", 
				"PATH+MAVEN=${tool 'maven-3.5.2'}/bin:${env.JAVA_HOME}/bin"
				])
				{
		stage("Build & UT"){
		sh"cd devtest1"
		sh "mvn --batch-mode -V -U -e clean deploy -Dsurefire.useFile=false"

		}
		}
		
	}	
 

