echo "Updating repositories"
sudo apt-get update
echo "Installing prerequisites"
sudo apt install python-is-python3
bash -c "$(curl -s  https://raw.githubusercontent.com/cisco/mindmeld/master/scripts/mindmeld_lite_init.sh)"
echo "Starting virtualenv"
python -m virtualenv venv
python3 -m ensurepip --upgrade
echo "Installing elasticsearch"
if [[ -d "elasticsearch-7.8.0" ]]
then
    echo "elasticsearch exists on your filesystem."
else
	echo "Installing elasticsearch"
	curl https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.16.0-linux-x86_64.tar.gz -o elasticsearch-7.16.0.tar.gz
	tar -zxvf elasticsearch-7.16.0.tar.gz
fi
source venv/bin/activate
echo "Installing requirements"
pip install -r requirements.txt
pip install -r journey_bot/requirements.txt
sudo apt install default-jdk