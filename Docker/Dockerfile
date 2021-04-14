FROM ubuntu:20.04

ENV DEBIAN_FRONTEND 'noninteractive' LANG='en_US.UTF-8' LANGUAGE='en_US:en' LC_ALL='en_US.UTF-8'

# LibreOffice && utils
RUN apt-get update && \
	apt-get -y -q --no-install-recommends install \
		libreoffice \
		libreoffice-writer \
		ure \
		libreoffice-java-common \
		libreoffice-core \
		libreoffice-common \
		fonts-opensymbol \
		hyphen-en-us \
		hyphen-ru \
		fonts-dejavu \
		fonts-dejavu-core \
		fonts-dejavu-extra \
		fonts-droid-fallback \
		fonts-dustin \
		fonts-f500 \
		fonts-fanwood \
		fonts-freefont-ttf \
		fonts-liberation \
		fonts-lmodern \
		fonts-lyx \
		fonts-sil-gentium \
		fonts-texgyre \
		fonts-tlwg-purisa \
		tzdata curl ca-certificates fontconfig locales && \
    echo "en_US.UTF-8 UTF-8" >> /etc/locale.gen && \
    locale-gen en_US.UTF-8 && \
	apt-get -y -q remove libreoffice-gnome && \
	apt-get -y autoremove && \
	rm -rf /var/lib/apt/lists/*

# AdoptOpenJDK 11 - https://github.com/AdoptOpenJDK/openjdk-docker/blob/820829025ee3eb66b5dc9e5fed0ee3171aababe9/11/jre/ubuntu/Dockerfile.hotspot.releases.full
ENV JAVA_VERSION jdk-11.0.10+9
RUN set -eux; \
	ESUM='25fdcf9427095ac27c8bdfc82096ad2e615693a3f6ea06c700fca7ffb271131a'; \
    BINARY_URL='https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.10%2B9/OpenJDK11U-jre_x64_linux_hotspot_11.0.10_9.tar.gz'; \
    curl -LfsSo /tmp/openjdk.tar.gz ${BINARY_URL}; \
    echo "${ESUM} */tmp/openjdk.tar.gz" | sha256sum -c -; \
    mkdir -p /opt/java/openjdk; \
    cd /opt/java/openjdk; \
    tar -xf /tmp/openjdk.tar.gz --strip-components=1; \
    rm -rf /tmp/openjdk.tar.gz;

ENV JAVA_HOME=/opt/java/openjdk \
    PATH="/opt/java/openjdk/bin:$PATH"

COPY  DocConverter.jar /app/DocConverter.jar
ENTRYPOINT ["java","-jar","/app/DocConverter.jar"]