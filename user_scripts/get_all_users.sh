echo "THIS SCRIPT IS GETTING ALL USERS WITHOUT SEARCH CRITERIA"

echo "USER 5 : "
curl -H "Content-Type: application/json" -w "%{http_code}"  http://localhost:8585/user/getAll

echo "\nEND OF USER SEARCH"
exit 1

