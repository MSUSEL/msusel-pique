FROM alpine:3.20.3

ARG MAVEN_VERSION=3.9.6

RUN apk update && apk upgrade && apk add \
    git openjdk11

#pip install jep numpy pandas seaborn matplotlib scipy scikit-learn IPython

# move to home for a fresh start
WORKDIR "/home"

# maven install - install in opt
WORKDIR "/opt"
RUN wget "https://dlcdn.apache.org/maven/maven-3/"$MAVEN_VERSION"/binaries/apache-maven-"$MAVEN_VERSION"-bin.tar.gz"
RUN tar xzvf "apache-maven-"$MAVEN_VERSION"-bin.tar.gz"
RUN rm "apache-maven-"$MAVEN_VERSION"-bin.tar.gz"
ENV PATH "/opt/apache-maven-"$MAVEN_VERSION"/bin:$PATH"

# pique core install
WORKDIR "/home"
RUN git clone https://github.com/MSUSEL/msusel-pique.git
WORKDIR "/home/msusel-pique"
RUN git checkout "tags/v1.0.1"
RUN mvn install -Dmaven.test.skip -Dlicense.skip=true