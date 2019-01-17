echo "THIS SCRIPT IS CREATING 5 DIFFERENT USERS!"

echo "USER 1 : "
curl -d '{"firstname":"Maxime", "lastname" : "Henry", "email" : "maxime@gmail.com", "nickname" : "king Henry", "country" : "France", "password" : "henry"}' -H "Content-Type: application/json" -w "%{http_code}" -X POST http://localhost:8585/user/create

echo "\nUSER 2 : "
curl -d '{"firstname":"James", "lastname" : "Pogba", "email" : "james@gmail.com", "nickname" : "Magic legs", "country" : "England", "password" : "pogba"}' -H "Content-Type: application/json" -X POST http://localhost:8585/user/create

echo "\nUSER 3 : "
curl -d '{"firstname":"Eric", "lastname" : "Martial", "email" : "eric@gmail.com", "nickname" : "fast and furious", "country" : "England", "password" : "eric"}' -H "Content-Type: application/json" -X POST http://localhost:8585/user/create

echo "\nUSER 4 : "
curl -d '{"firstname":"Adam", "lastname" : "Mbappe", "email" : "adam@gmail.com", "nickname" : "fire", "country" : "France", "password" : "adam"}' -H "Content-Type: application/json" -X POST http://localhost:8585/user/create

echo "\nUSER 5 : "
curl -d '{"firstname":"Scott", "lastname" : "Griezmann", "email" : "scott@gmail.com", "nickname" : "goleador", "country" : "Spain", "password" : "griezmann"}' -H "Content-Type: application/json" -X POST http://localhost:8585/user/create


echo "\nEND OF USER CREATION"
exit 1

