# Rasa Citizen Complaint Management

## Pyenv

### Create a virtual environment with pyenv

```bash
pyenv virtualenv 3.10.8 rasa_3.6
```

```bash
pyenv local
```

### Activate the pyenv virtual environment

```bash
pyenv activate rasa_3.6
```

## Train the DE model

### Force Changes

Sometimes it is necessary to force Rasa not to use cached data in the training e.g. when you want to
override default actions with custom ones.

```bash
rasa train --force
```

## Rasa

### Important Commands

```bash

rasa train

rasa shell

rasa run

rasa run actions

rasa test -s tests

```

## PyTest for the Custom Actions

Inspired by https://github.com/RasaHQ/financial-demo/tree/db/tests

```bash
pytest -s
```

-s to also get stdout

Uses https://requests-mock.readthedocs.io/en/latest/index.html to mock requests

## Deployment
Manual

### Run Action Server Container

```bash
sudo ./build_action_server_container.sh
```

```bash
sudo ./run_action_server_container.sh
```

### Run Rasa Container

```bash
sudo ./train_in_container.sh
```
Alternatively, train the model locally, and copy it to the remote host, etc.

```bash
sudo ./run_rasa_container.sh
```
This will mount the current directory in the container to make all necessary files available like the configs and the model.
