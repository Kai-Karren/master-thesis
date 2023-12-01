docker run -d --user 1000 -v $(pwd):/app rasa/rasa:3.6.4-full train --domain domain.yml --data data --out models
