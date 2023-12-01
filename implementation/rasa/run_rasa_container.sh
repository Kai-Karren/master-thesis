sudo docker run --user 1000 -p 5005:5005 --net host  -v $(pwd):/app --name rasa-complaints -d rasa/rasa:3.6.4-full run --connector rest
