plugins {
    java
}

group = "com.heroes"
version = "1.0"

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src"))
        }
    }
    test {
        java {
            srcDirs("test")
        }
    }
}

dependencies {
    implementation(files("libs/heroes_task_lib-1.0-SNAPSHOT.jar"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
}

tasks.jar {
    archiveBaseName.set("heroes_task")
    archiveVersion.set("1.0")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed", "standard_out", "standard_error")
        showExceptions = true
        showCauses = true
        showStackTraces = true
        showStandardStreams = true
    }
}

tasks.register<Copy>("deploy") {
    dependsOn("build")
    from(tasks.jar)
    into("")   // Нужно указать путь к игре
    rename { "obf.jar" }
    doLast { println("JAR скопирован в игру как obf.jar") }
}