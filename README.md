# Bulb Maven Plugin

## Introduction
Bulb Maven pluging is a maven plugin what exports XML test report in a format compatible with Bulb for JIRA plugin.

## Usage

Document your tests like this:
```java
```

Add following to your pom.xml file:
```xml
...
<build>
    <plugins>
        <plugin>
            <groupId>org.bulbit.maven</groupId>
            <artifactId>bulb-maven-plugin</artifactId>
            <version>1.0.0</version>
            <configuration>
            </configuration>
            <executions>
                <execution>
                    <phase>test</phase>
                    <goals>
                        <goal>testreport</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
...
```

Run:
```
mvn clean install
```

Use report under target/trackerTestReport.xml with Bulb for JIRA plugin.
