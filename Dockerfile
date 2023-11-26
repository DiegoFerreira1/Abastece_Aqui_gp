FROM openjdk:11-jdk

# Instalar dependências
RUN apt-get update && \
    apt-get install -y \
    unzip \
    wget

# Baixar e instalar o Android SDK
ENV ANDROID_SDK_ROOT=/usr/local/android-sdk
RUN wget -q https://dl.google.com/android/repository/commandlinetools-linux-7583922_latest.zip -O tools.zip \
    && mkdir -p $ANDROID_SDK_ROOT/cmdline-tools \
    && unzip -q tools.zip -d $ANDROID_SDK_ROOT/cmdline-tools \
    && rm tools.zip

# Adicionar o SDK do Android ao PATH
ENV PATH="${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/tools/bin"

# Adicionar as licenças automaticamente (hashes das licenças)
RUN mkdir -p $ANDROID_SDK_ROOT/licenses/ \
    && echo -e "\n8933bad161af4178b1185d1a37fbf41ea5269c55\n" > $ANDROID_SDK_ROOT/licenses/android-sdk-license \
    && echo -e "\n84831b9409646a918e30573bab4c9c91346d8abd\n" > $ANDROID_SDK_ROOT/licenses/android-sdk-preview-license \
    && echo -e "\nd975f751698a77b662f1254ddbeed3901e976f5a\n" > $ANDROID_SDK_ROOT/licenses/intel-android-extra-license

# Copiar o código fonte do aplicativo para o contêiner
COPY . /app

# Definir o diretório de trabalho
WORKDIR /app

# Comando para compilar o projeto durante a construção da imagem
RUN ./gradlew assembleDebug

# O comando CMD é executado quando o contêiner é iniciado
CMD ["./gradlew", "installDebug"]
