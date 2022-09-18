#Deriving the latest base image
FROM python:3.6


#Labels as key value pair
LABEL Maintainer="tatanpoker09"


# Any working directory can be chosen as per choice like '/' or '/home' etc
# i have chosen /usr/app/src
WORKDIR /usr/app/src

#to COPY the remote file at working directory in container
COPY ./journey_integrations_executer/app.py ./
COPY ./journey_integrations_executer/entrypoint.sh ./entrypoint.sh
COPY ./journey_integrations_executer/requirements.txt requirements.txt
COPY ./journey_integrations_executer/scripts ./scripts
COPY ./integrations/*/*.py ./scripts/
COPY ./integrations ./integrations
RUN cat ./integrations/*/requirements.txt >> ./requirements.txt
RUN cat ./integrations/*/apt-requirements.txt >> ./apt-requirements.txt

# Now the structure looks like this '/usr/app/src/test.py'
RUN apt-get update -y && xargs -a apt-requirements.txt -n 1 -L 1 apt-get install -y
RUN python -m pip install --upgrade pip
RUN cat requirements.txt | xargs -n 1 -L 1 pip install --upgrade
EXPOSE 9090
#CMD instruction should be used to run the software
#contained by your image, along with any arguments.

ENTRYPOINT [ "./entrypoint.sh" ]