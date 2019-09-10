#!groovy

// Method to get changeSet using git diff 
def getChangeSet(branchCommit,masterCommit)
{
	def changeSets = sh( script: "git diff --name-only ${branchCommit} ${masterCommit}", returnStdout: true )
	changeSets = changeSets.replaceAll('^\\s+',"");
	String[] changedFileSets = changeSets.split("\\s+");

	return changedFileSets
}

/*
 * Method to identify the modules changed 
 */

def getModifiedModules(files)
{
	def modulesChanged = []
	for (file in files) 
	{
		echo "file : ${file}"
		def tokens = file.split('/')
		def tokensLength = tokens.length
		if(tokensLength == 1)
		{
			def parentFolder = tokens[0]
			modulesChanged.add(parentFolder)
		}
		else
		{
			def parentFolder = tokens[0]
		        def childFolder = tokens[1]
		        def folder = "${parentFolder}/${childFolder}"
		        modulesChanged.add(folder)
		}
								
	}	
	return modulesChanged.toSet()
}

 /*
 *Validate modules to be build
 */
 def validateModules(modulesChanged,serviceModule)  
{
	echo "changedModules : $modulesChanged"
	echo "serviceModule : $serviceModule"
	def serviceModuleChanged = []
	def isCrossSGModule = false
		
	for(module in modulesChanged)
	{
		
		for (services in serviceModule)
		{
			if(module.contains(services)) 
			{
				serviceModuleChanged.add(services)
				isCrossSGModule = false
				break;
			}
			else
			{
				isCrossSGModule = true
			}
		}
		if(isCrossSGModule)
		{
			echo "Cross Service Group PR"
			throw new Exception("PR contains changes in other Service Group")
		}
					
	}
	return serviceModuleChanged.toSet()
}

// Method to get changeSet using git diff 
def getChangeSet(branchCommit,masterCommit)
{
	def changeSets = sh( script: "git diff --name-only ${branchCommit} ${masterCommit}", returnStdout: true )
	changeSets = changeSets.replaceAll('^\\s+',"");
	String[] changedFileSets = changeSets.split("\\s+");

	return changedFileSets
}


/** Method to display build */
def setDisplayName(buildNum, appBase) {
    currentBuild.displayName = '#' + buildNum + ' ' + appBase
}

// To send email
def sendEmail(status,stageName,checkpoint)
{
	emailext (
					mimeType: 'text/html',
					subject: "${status}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
					body: """<p>${status}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' at stage ${stageName}</p>
							<p>Check console output at ${env.BUILD_URL}</p>
							<p>Trigger job using checkpoint '${checkpoint}'</p>""",
					to: "${ghprbTriggerAuthor}@in.ibm.com"
			)

}

node()
	{
		def mavenHome
		def currentModules
	String buildNum = currentBuild.number.toString()
	Map<String,String> previousVersions=new HashMap<>();
	Map<String,Integer> map=new HashMap<>();
	def repo
	def delimiter = "/"
	def stageName
	def commitHash
	def currentDir
	def MiscUtils
		
		stage("Checkout SCM"){
			checkout scm
			stageName = "Git clone and setup"
			currentDir = pwd()
			MiscUtils = load("${currentDir}/pipeline-scripts/utils/MiscUtils.groovy")
			// Get the commit hash of PR branch 
	def branchCommit = sh( script: "git rev-parse refs/remotes/${sha1}^{commit}", returnStdout: true )
	
	// Get the commit hash of Master branch
	def masterCommit = sh( script: "git rev-parse origin/${ghprbTargetBranch}^{commit}", returnStdout: true )
	
	commitHash = sh( script: "git rev-parse origin/${env.GIT_BRANCH}",returnStdout: true, )
	commitHash = commitHash.replaceAll("[\n\r]", "")
	branchCommit = branchCommit.replaceAll("[\n\r]", "")
	masterCommit = masterCommit.replaceAll("[\n\r]", "")
	
	def changeSet = getChangeSet(branchCommit,masterCommit)
	def changedModules = getModifiedModules(changeSet)
	//def serviceModules = moduleProp['DATA_MODULES']
	serviceModulesList = serviceModules.split(',')
	currentModules = validateModules(changedModules,serviceModulesList)
	setDisplayName(buildNum, currentModules)
			
		}
		
		/*stage("Build & UT"){
		def mvnHome = tool name: 'MAVEN_HOME', type: 'maven'
		def directory = "loginforum1"
		dir(directory){
		sh "${mvnHome}/bin/mvn install"
					}
			}*/
			
		stage("SonarQube analysis") {
		
				def directory = "loginforum1"
          dir(directory){
              withSonarQubeEnv('sonar') {
                 /*sh 'mvn clean package sonar:sonar'*/
				 sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
              }
			  }
          
      }
	  
	  stage('Check Quality Gate') {
        
                echo 'Checking quality gate...'
                timeout(time: 5, unit: 'MINUTES') {
                        def swait = waitForQualityGate()
                        if (swait.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${swait.status}"
                        }
                 }
		}
	  
	  
	  stage("App deployment"){
	  def mvnHome = tool name: 'MAVEN_HOME', type: 'maven'
	  def directory = "loginforum1"
	  dir(directory){
		sh "${mvnHome}/bin/mvn clean package"
		
	  
	  	   pushToCloudFoundry(
                   target: 'https://api.run.pivotal.io',
                   organization: 'cicdpipeline',
                   cloudSpace: 'development',
                   credentialsId: '89671dff-c2c4-4016-a6fe-49904b8563d5',
                   manifestChoice: [manifestFile: 'manifest.yml']
                   )
				 }
			}	 
	  
	  
	}	


