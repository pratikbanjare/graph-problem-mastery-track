#!/usr/bin/env bash
# Verifies that this project's produced artifact can actually be consumed
# as a normal Maven dependency by a separate, independent project.
#
# Steps:
#   1. Installs this project's jar (+ sources/javadoc) into the local Maven repo.
#   2. Generates a throwaway consumer project with a dependency on the
#      groupId:artifactId:version of this project.
#   3. Compiles and runs a small program in the consumer project that imports
#      and exercises a public class from this library.
#
# Fails (non-zero exit) if the library cannot be resolved, imported, compiled,
# or executed as a dependency.
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$REPO_ROOT"

GROUP_ID=$(mvn -q -f pom.xml help:evaluate -Dexpression=project.groupId -DforceStdout)
ARTIFACT_ID=$(mvn -q -f pom.xml help:evaluate -Dexpression=project.artifactId -DforceStdout)
VERSION=$(mvn -q -f pom.xml help:evaluate -Dexpression=project.version -DforceStdout)

echo "Verifying library consumption for ${GROUP_ID}:${ARTIFACT_ID}:${VERSION}"

echo "==> Installing artifact into local Maven repository"
mvn -B -q install -DskipTests

CONSUMER_DIR=$(mktemp -d)
trap 'rm -rf "$CONSUMER_DIR"' EXIT

mkdir -p "$CONSUMER_DIR/src/main/java/consumer"

cat > "$CONSUMER_DIR/pom.xml" <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <groupId>consumer.check</groupId>
    <artifactId>library-consumer-check</artifactId>
    <version>1.0</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <exec.mainClass>consumer.ConsumerCheck</exec.mainClass>
    </properties>

    <dependencies>
        <dependency>
            <groupId>${GROUP_ID}</groupId>
            <artifactId>${ARTIFACT_ID}</artifactId>
            <version>${VERSION}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.3.0</version>
            </plugin>
        </plugins>
    </build>
</project>
EOF

cat > "$CONSUMER_DIR/src/main/java/consumer/ConsumerCheck.java" <<'EOF'
package consumer;

import practice.graph.Graph;
import practice.path.BFS;

import java.util.List;

/**
 * Standalone program that imports and uses a public class from the library
 * exactly as an external consumer would, to prove the artifact is usable
 * as a Maven dependency.
 */
public final class ConsumerCheck {
    public static void main(String[] args) {
        Graph graph = new Graph(4);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        List<Integer> path = new BFS().shortestDistancePath(graph, 1, 4);
        if (!List.of(1, 2, 3, 4).equals(path)) {
            throw new IllegalStateException("Library produced unexpected path: " + path);
        }
        System.out.println("Library import/usage check passed. Path = " + path);
    }
}
EOF

echo "==> Compiling and running consumer project against the published artifact"
mvn -B -q -f "$CONSUMER_DIR/pom.xml" compile exec:java

echo "==> Library consumption check succeeded"
