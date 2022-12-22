FROM debian:10

RUN apt-get update
RUN apt-get install -y curl tar make jq

RUN mkdir -p /app
WORKDIR /app

# curl コマンドでリモートサーバーからファイルをダウンロードし、tar コマンドで展開する
RUN curl -L https://github.com/stripe/stripe-cli/releases/download/v1.13.6/stripe_1.13.6_linux_x86_64.tar.gz | tar xz
RUN mkdir -p /app/bin && mv stripe /app/bin/stripe

ENV PATH /app/bin:${PATH}

ENTRYPOINT [ "/app/bin/stripe" ]
