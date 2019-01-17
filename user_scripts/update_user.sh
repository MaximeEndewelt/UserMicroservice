echo "THIS SCRIPT IS UPDATING USERS 1 and 2"

echo "UPDATE ON USER 1 : "
curl -d '{"firstname":"Maxime", "lastname" : "Henry", "email" : "maxime@gmail.com", "nickname" : "king Henry", "country" : "England", "password" : "henry"}' -H "Content-Type: application/json" -w "%{http_code}" -X POST http://localhost:8585/user/update

echo "\nUPDATE ON USER 2 : "
curl -d '{"firstname":"Paul", "lastname" : "Pogba", "email" : "james@gmail.com", "nickname" : "Magic legs", "country" : "England", "password" : "pogba"}' -H "Content-Type: application/json" -w "%{http_code}"  -X POST http://localhost:8585/user/update

echo "\nEND OF USER UPDATE"
exit 1

