# Use OpenJDK 17 as base image (compatible with Scala 2.13 and Spark 3.5.3)
FROM openjdk:17-jdk-slim

# Set environment variables
ENV SCALA_VERSION=2.13
ENV SBT_VERSION=1.10.11
ENV SPARK_VERSION=3.5.3
ENV HADOOP_VERSION=3

# Install necessary packages
RUN apt-get update && \
    apt-get install -y wget curl bash procps && \
    rm -rf /var/lib/apt/lists/*

# Install SBT first (more stable, can be cached)
RUN wget -q "https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.tgz" && \
    tar -xzf sbt-${SBT_VERSION}.tgz && \
    mv sbt /opt/sbt && \
    rm sbt-${SBT_VERSION}.tgz

ENV PATH=$PATH:/opt/sbt/bin

# Set working directory
WORKDIR /app

# Copy lib directory with all dependencies and Spark first
COPY lib/ ./lib/

# Set Spark environment variables to use existing Spark installation
ENV SPARK_HOME=/app/lib/spark-3.5.3-bin-hadoop3-scala2.13
ENV PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin

# Copy SBT configuration files (for better caching)
COPY build.sbt .
COPY project/ ./project/

# Copy source code
COPY src/ ./src/

# Build the project (using existing dependencies in lib)
RUN sbt clean compile package

# Move the generated jar file to lib directory for easier access
RUN cp target/scala-2.13/milvus-spark-connector-example_2.13-0.1.0-SNAPSHOT.jar ./lib/

# Create spark-submit wrapper script with Java 17 compatibility
RUN echo '#!/bin/bash' > /app/spark-submit-wrapper.sh && \
    echo '' >> /app/spark-submit-wrapper.sh && \
    echo 'export SPARK_HOME=/app/lib/spark-3.5.3-bin-hadoop3-scala2.13' >> /app/spark-submit-wrapper.sh && \
    echo '' >> /app/spark-submit-wrapper.sh && \
    echo 'if [ ! -d "$SPARK_HOME" ]; then' >> /app/spark-submit-wrapper.sh && \
    echo '  echo "Error: SPARK_HOME directory does not exist: $SPARK_HOME"' >> /app/spark-submit-wrapper.sh && \
    echo '  exit 1' >> /app/spark-submit-wrapper.sh && \
    echo 'fi' >> /app/spark-submit-wrapper.sh && \
    echo '' >> /app/spark-submit-wrapper.sh && \
    echo 'SPARK_SUBMIT="$SPARK_HOME/bin/spark-submit"' >> /app/spark-submit-wrapper.sh && \
    echo 'if [ ! -f "$SPARK_SUBMIT" ]; then' >> /app/spark-submit-wrapper.sh && \
    echo '  echo "Error: spark-submit not found at: $SPARK_SUBMIT"' >> /app/spark-submit-wrapper.sh && \
    echo '  exit 1' >> /app/spark-submit-wrapper.sh && \
    echo 'fi' >> /app/spark-submit-wrapper.sh && \
    echo '' >> /app/spark-submit-wrapper.sh && \
    echo '# Add Java 17 compatibility options' >> /app/spark-submit-wrapper.sh && \
    echo 'export SPARK_SUBMIT_OPTS="--add-opens=java.base/sun.nio.ch=ALL-UNNAMED \\' >> /app/spark-submit-wrapper.sh && \
    echo '--add-opens=java.base/java.lang=ALL-UNNAMED \\' >> /app/spark-submit-wrapper.sh && \
    echo '--add-opens=java.base/java.lang.invoke=ALL-UNNAMED \\' >> /app/spark-submit-wrapper.sh && \
    echo '--add-opens=java.base/java.io=ALL-UNNAMED \\' >> /app/spark-submit-wrapper.sh && \
    echo '--add-opens=java.base/java.util=ALL-UNNAMED \\' >> /app/spark-submit-wrapper.sh && \
    echo '--add-opens=java.base/java.util.concurrent=ALL-UNNAMED \\' >> /app/spark-submit-wrapper.sh && \
    echo '--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED \\' >> /app/spark-submit-wrapper.sh && \
    echo '--add-opens=java.base/sun.nio.cs=ALL-UNNAMED \\' >> /app/spark-submit-wrapper.sh && \
    echo '--add-opens=java.base/sun.security.action=ALL-UNNAMED \\' >> /app/spark-submit-wrapper.sh && \
    echo '--add-opens=java.base/sun.util.calendar=ALL-UNNAMED \\' >> /app/spark-submit-wrapper.sh && \
    echo '--add-opens=java.security.jgss/sun.security.krb5=ALL-UNNAMED"' >> /app/spark-submit-wrapper.sh && \
    echo '' >> /app/spark-submit-wrapper.sh && \
    echo '# Auto-include the Milvus Spark connector jar' >> /app/spark-submit-wrapper.sh && \
    echo 'exec "$SPARK_SUBMIT" --jars ./lib/spark-connector-assembly-0.1.0-SNAPSHOT.jar "$@"' >> /app/spark-submit-wrapper.sh

RUN chmod +x /app/spark-submit-wrapper.sh

# Expose Spark UI port
EXPOSE 4040

# Default to bash for interactive use
CMD ["/bin/bash"] 