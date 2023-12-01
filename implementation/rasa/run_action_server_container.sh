docker run -d -p 5055:5055 -v $(pwd)/actions:/app/actions --net host --name action-server custom-action-server
