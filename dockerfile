#
# MIT License
# Copyright (c) 2019 Montana State University Software Engineering Labs
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#

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
RUN git checkout "cicd-test"
RUN mvn install -Dmaven.test.skip -Dlicense.skip=true
