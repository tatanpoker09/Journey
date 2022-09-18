FROM python:3.6
LABEL Maintainer="tatanpoker09"


WORKDIR /usr/app/src
COPY . .
RUN apt update
RUN apt install -y portaudio19-dev python3-pyaudio libgirepository1.0-dev
RUN apt install -y pulseaudio ffmpeg sox
RUN pip install -r requirements.txt

RUN pip uninstall -y enum34

CMD [ "python", "./PorcupineMicrophone.py"]