node {
   def mvnHome
    def jdk = tool name: 'JDK 14'
    def jdk8=tool name:'JDK 1.8'
    env.JAVA_HOME = "${jdk}"
   stage('Preparation') { 
			checkout scm
            mvnHome = tool 'Maven'
            dir('Dentahl4J') {
                if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' clean"
            } else {
                bat(/"${mvnHome}\bin\mvn" clean/)
            }
            }
             
        }
   dir('Dentahl4J') {
        
		stage('Build') {
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' -DskipTests install"
            } else {
                bat(/"${mvnHome}\bin\mvn" -DskipTests install/)
            }
        }
        stage('Test') {
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore=true test"
            } else {
                bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore=true test/)
            }
        }

        stage('Build Native') {
            //dir('Dentahl4J-Server') {
             //   if (isUnix()) {
             //       sh "'${mvnHome}/bin/mvn' -DskipTests jfx:native"
             //   } else {
             //       bat(/"${mvnHome}\bin\mvn" -DskipTests jfx:native/)
             //   }
           // }
            dir('Dentahl4J-FX/target') {
                if (isUnix()) {
                    sh "'${jdk}/bin/jpackage' --name Dentahl4J --main-jar Dentahl4J-FX.jar"
                } else {
                    bat(/"${jdk}\bin\jpackage" -DskipTests jfx:native/)
                }
            }
        }

        stage('Reporting') {
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' -DskipTests site"
            } else {
                bat(/"${mvnHome}\bin\mvn" -DskipTests site/)
            }
        }
        stage('Results') {
            junit allowEmptyResults: true, testResults: '**/TEST-*.xml'
        }
		stage('Deployment') {
			if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' -DskipTests deploy"
            } else {
                bat(/"${mvnHome}\bin\mvn" -DskipTests deploy/)
            }
        }
   }
}