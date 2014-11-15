name := "mock-app"

version := "1.0-SNAPSHOT"

resolvers += "Sonatype OSS Snapshots" at "https://oss.snoatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  cache,
  javaCore,
  "com.typesafe.slick" %% "slick"           % "1.0.1",
  "com.typesafe.play"  %% "play-slick"      % "0.5.0.8",
  "postgresql"          % "postgresql"      % "9.1-901-1.jdbc4",
  "org.virtuslab"      %% "unicorn"         % "0.4",
  "jp.t2v"             %% "play2-auth"      % "0.11.0",
  "jp.t2v"             %% "play2-auth-test" % "0.11.0"     % "test",
  "org.mindrot"         % "jbcrypt" % "0.3m"
)

play.Project.playScalaSettings
